package com.drzk.parkingguide.camera;

import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.SpringApplicationContextUtil;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraCommunicationUtil {

    private static Logger log=LoggerFactory.getLogger(CameraCommunicationUtil.class);

    private static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;

    public static Map<String, Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl>> m_loginMap = new HashMap<>();//所有相机登录句柄
    public static Map<String, NetSDKLib.LLong> m_attachMap = new HashMap<>();//所有相机注册事件句柄
    public static List<fAnalyzerDataCallBackImpl> callBackList = new ArrayList<>();//登录前所有构建的相机事件回调对象

    private CameraCommunicationUtil(){}


    public static boolean initSDK(Map<String, Object> config, NetSDKLib.fDisConnect fDisConnect, NetSDKLib.fHaveReConnect fHaveReConnect){
        //初始化SDK
        boolean bInit = netsdk.CLIENT_Init(fDisConnect, null);
        if (!bInit){
            return false;
        }


        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        netsdk.CLIENT_SetAutoReconnect(fHaveReConnect, null);

        //设置登录超时时间和尝试次数，可选
        //int waitTime = 5000; //登录请求响应超时时间设置为5S
        int waitTime = (Integer) config.get("waitTime");//登录请求响应超时时间设置为5S
        //int tryTimes = 1;    //登录时尝试建立链接1次
        int tryTimes = (Integer)config.get("tryTimes");    //登录时尝试建立链接1次
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);

        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        //netParam.nConnectTime = 10000;      // 登录时尝试建立链接的超时时间
        netParam.nConnectTime = (Integer) config.get("nConnectTime");      // 登录时尝试建立链接的超时时间
        //netParam.nGetConnInfoTime = 3000;   // 设置子连接的超时时间
        netParam.nGetConnInfoTime = (Integer) config.get("nGetConnInfoTime");   // 设置子连接的超时时间
        netsdk.CLIENT_SetNetworkParam(netParam);

        return true;
    }


    public static void loginAndSubscribe(List<fAnalyzerDataCallBackImpl> fAnalyzerDataCallBackList) {
        //登录注册之前先取消注册事件和退出登录
        List<String> camIdList = new ArrayList<>();
        for (fAnalyzerDataCallBackImpl cb : fAnalyzerDataCallBackList) {
            camIdList.add(cb.getCamera().getId());
        }
        unsubscribeAndLogout(camIdList);

        //登录相机并订阅相机事件
        List<String> aaa = new ArrayList<>();//登录成功的相机
        List<String> bbb = new ArrayList<>();//登录失败的相机
        for (int i = 0; i < fAnalyzerDataCallBackList.size(); i++) {
            fAnalyzerDataCallBackImpl cb = fAnalyzerDataCallBackList.get(i);
            fAnalyzerDataCallBackImpl.Camera camera = cb.getCamera();
            NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
            IntByReference nError = new IntByReference(0);

            //登录相机
            NetSDKLib.LLong m_hLoginHandle = netsdk.CLIENT_LoginEx2(camera.getCameraIp(), camera.getPort(), camera.getAccount(), camera.getPassword(), 0, null, m_stDeviceInfo, nError);
            if (m_hLoginHandle.longValue() > 0) {
                log.debug("登录相机(id:[{}], ip:[{}])成功！", camera.getId(), camera.getCameraIp());

                //订阅相机事件
                NetSDKLib.LLong m_hAttachHandle = netsdk.CLIENT_RealLoadPictureEx(m_hLoginHandle, 0, NetSDKLib.EVENT_IVS_ALL, 0, cb, null, null);
                if (m_hAttachHandle.longValue() > 0) {
                    log.debug("订阅相机(id:[{}], ip:[{}])事件成功！", camera.getId(), camera.getCameraIp());

                    //登录和订阅都成功后，保存相关句柄信息
                    Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> m = new HashMap<>();
                    m.put(m_hLoginHandle, cb);
                    m_loginMap.put(camera.getId(), m);//保存相关的登录句柄
                    m_attachMap.put(camera.getId(), m_hAttachHandle);//保存相关的句柄信息
                    ParkingDeviceDao deviceDao = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingDeviceDao.class);
                    ParkingDeviceVo status = new ParkingDeviceVo(cb.getCamera().getId());
                    status.setStatus("1");
                    deviceDao.updateCameraConnStatusById(status);
                    aaa.add(camera.getCameraIp());
                } else {
                    log.debug("订阅相机(id:[{}], ip:[{}])事件失败！", camera.getId(), camera.getCameraIp());
                    bbb.add(camera.getCameraIp());
                }

            } else {
                log.debug("登录相机(id:[{}], ip:[{}])失败！", camera.getId(), camera.getCameraIp());
                bbb.add(camera.getCameraIp());
            }
        }

        log.info("登录成功的相机{}", JsonUtils.objectToJson(aaa));
        log.info("登录失败的相机{}", JsonUtils.objectToJson(bbb));

    }

    public static int loginAndSubscribe(fAnalyzerDataCallBackImpl cb){
        //登录注册之前先取消注册事件和退出登录
        unsubscribeAndLogout(new ParkingDeviceVo(cb.getCamera().getId()));

        fAnalyzerDataCallBackImpl.Camera camera = cb.getCamera();
        NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
        IntByReference nError = new IntByReference(0);

        //登录相机
        NetSDKLib.LLong m_hLoginHandle = netsdk.CLIENT_LoginEx2(camera.getCameraIp(), camera.getPort(), camera.getAccount(), camera.getPassword(), 0, null, m_stDeviceInfo, nError);
        if (m_hLoginHandle.longValue() > 0) {
            log.info("登录相机(id:[{}], ip:[{}])成功！", camera.getId(), camera.getCameraIp());

            //订阅相机事件
            NetSDKLib.LLong m_hAttachHandle = netsdk.CLIENT_RealLoadPictureEx(m_hLoginHandle, 0, NetSDKLib.EVENT_IVS_ALL, 0, cb, null, null);
            if (m_hAttachHandle.longValue() > 0) {
                log.info("订阅相机(id:[{}], ip:[{}])事件成功！", camera.getId(), camera.getCameraIp());

                //登录和订阅都成功后，保存相关句柄信息
                Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> m = new HashMap<>();
                m.put(m_hLoginHandle, cb);
                m_loginMap.put(camera.getId(), m);//保存相关的登录句柄
                m_attachMap.put(camera.getId(), m_hAttachHandle);//保存相关的句柄信息
                ParkingDeviceDao deviceDao = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingDeviceDao.class);
                ParkingDeviceVo status = new ParkingDeviceVo(cb.getCamera().getId());
                status.setStatus("1");
                deviceDao.updateCameraConnStatusById(status);
                return 1;
            } else {
                log.debug("订阅相机(id:[{}], ip:[{}])事件失败！", camera.getId(), camera.getCameraIp());
                return 0;
            }

        } else {
            log.debug("登录相机(id:[{}], ip:[{}])失败！", camera.getId(), camera.getCameraIp());
            return 0;
        }
    }

    private static void unsubscribeAll(){
        m_attachMap.forEach((camId, m_hAttachHandle)->{
            boolean b = netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
            log.info("取消订阅相机事件-->相机id:{}, 操作结果:{}", camId, b);
        });
        m_loginMap.forEach((camId, map)->{
            boolean b = netsdk.CLIENT_Logout(map.keySet().iterator().next());
            log.info("退出相机登录-->相机id:{}, 操作结果:{}", camId, b);
        });
    }

    public static void unsubscribeAndLogout(List<String> cameraIdList){

        for (String camId : cameraIdList) {
            NetSDKLib.LLong m_hAttachHandle = m_attachMap.get(camId);
            if (m_hAttachHandle != null) {
                boolean b = netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
                log.info("取消订阅相机事件-->相机id:{}, 操作结果:{}", camId, b);
            }
            m_attachMap.remove(camId);
            Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = m_loginMap.get(camId);
            if (map!=null){
                NetSDKLib.LLong m_hLoginHandle = map.keySet().iterator().next();
                if (m_hLoginHandle != null) {
                    boolean b = netsdk.CLIENT_Logout(m_hLoginHandle);
                    log.info("退出相机登录-->相机id:{}, 操作结果:{}", camId, b);
                }
                m_loginMap.remove(camId);
                ParkingDeviceDao deviceDao = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingDeviceDao.class);
                ParkingDeviceVo camera = new ParkingDeviceVo(camId);
                camera.setStatus("0");
                deviceDao.updateCameraConnStatusById(camera);
            }
        }
    }

    public static void unsubscribeAndLogout(ParkingDeviceVo camera){

        String camId = camera.getId();
        NetSDKLib.LLong m_hAttachHandle = m_attachMap.get(camId);
        if (m_hAttachHandle != null) {
            boolean b = netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
            log.info("取消订阅相机事件-->相机id:{}, 操作结果:{}", camId, b);
        }
        m_attachMap.remove(camId);
        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = m_loginMap.get(camId);
        if (map!=null){
            NetSDKLib.LLong m_hLoginHandle = map.keySet().iterator().next();
            if (m_hLoginHandle != null) {
                boolean b = netsdk.CLIENT_Logout(m_hLoginHandle);
                log.info("退出相机登录-->相机id:{}, 操作结果:{}", camId, b);
            }
            m_loginMap.remove(camId);
        }
        ParkingDeviceDao deviceDao = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingDeviceDao.class);
        camera.setStatus("0");
        deviceDao.updateCameraConnStatusById(camera);
    }

    public static void cleanup(){
        unsubscribeAll();
        netsdk.CLIENT_Cleanup();
        log.info("释放相机SDK资源。");
    }

}
