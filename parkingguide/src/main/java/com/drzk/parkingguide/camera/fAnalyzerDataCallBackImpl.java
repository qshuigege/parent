package com.drzk.parkingguide.camera;

import com.drzk.parkingguide.bo.RemainPlaces;
import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.service.ParkingBusinessService;
import com.drzk.parkingguide.util.ProjConstants;
import com.drzk.parkingguide.util.SpringApplicationContextUtil;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class fAnalyzerDataCallBackImpl implements NetSDKLib.fAnalyzerDataCallBack {

    private Logger log = LoggerFactory.getLogger(fAnalyzerDataCallBackImpl.class);

    private static ParkingBusinessService busiService = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingBusinessService.class);

    private Camera camera;

    private List<Led> leds;

    public fAnalyzerDataCallBackImpl(Camera camera, List<Led> leds){
        this.camera = camera;
        this.leds = leds;
    }

    @Override
    public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize, Pointer dwUser, int nSequence, Pointer reserved) {
        try {

            if (pAlarmInfo == null) {
                return -1;
            }

            if (dwAlarmType == NetSDKLib.EVENT_IVS_TRAFFICJUNCTION//交通卡口事件
                    || dwAlarmType == NetSDKLib.EVENT_IVS_TRAFFICGATE//老版本交通卡口事件
                 /*|| dwAlarmType==NetSDKLib.EVENT_IVS_TRAFFIC_MANUALSNAP*//*手动抓拍事件*/) {

                log.debug("相机事件值：{}", dwAlarmType);
                //提取事件数据
                NetSDKLib.DEV_EVENT_TRAFFICJUNCTION_INFO msg = new NetSDKLib.DEV_EVENT_TRAFFICJUNCTION_INFO();

                ToolKits.GetPointerData(pAlarmInfo, msg);//提取数据
                String m_PlateNumber = new String(msg.stuObject.szText, "GBK").trim();//车牌号
                String m_PlateType = new String(msg.stTrafficCar.szPlateType).trim();//车牌类型
                String m_PlateColor = new String(msg.stTrafficCar.szPlateColor).trim();//车牌颜色
                String m_VehicleColor = new String(msg.stTrafficCar.szVehicleColor).trim();//车身颜色
                String m_VehicleType = new String(msg.stuVehicle.szObjectSubType).trim();//车身类型(车系)
                String m_VehicleSize = Res.string().getTrafficSize(msg.stTrafficCar.nVehicleSize);//车辆大小

                log.info("相机({}{})识别到车辆，车牌号：{}，车牌类型：{}，车牌颜色：{}，车身颜色：{}，车系：{}，车辆大小：{}。", camera.getCameraType() ,camera.getCameraIp(), m_PlateNumber, m_PlateType, m_PlateColor, m_VehicleColor, m_VehicleType, m_VehicleSize);

                //更新数据库剩余车位数
                RemainPlaces remainPlaces;
                if ("入口相机".equals(camera.getCameraType())){
                    //如果是入口相机，剩余车位数减1
                    remainPlaces = busiService.updateRemainPlaces(camera, 1);
                }else if ("出口相机".equals(camera.getCameraType())){
                    //出口相机，剩余车位数加1
                    remainPlaces = busiService.updateRemainPlaces(camera, 2);
                }else {
                    log.error("id为{}的相机类型在数据库中配置错误！-->{}", camera.getId(), camera.getCameraType());
                    return -1;
                }


                //保存车辆流水
                ParkingSnapInfoPo snapPo = new ParkingSnapInfoPo();
                snapPo.setCameraId(camera.getId());
                snapPo.setCameraIp(camera.getCameraIp());
                snapPo.setCameraType(camera.getCameraType());
                snapPo.setChannelId(camera.getChannelId());
                snapPo.setPlateNo(m_PlateNumber);
                snapPo.setPlateType(m_PlateType);
                snapPo.setPlateColor(m_PlateColor);
                snapPo.setVehicleColor(m_VehicleColor);
                snapPo.setVehicleType(m_VehicleType);
                snapPo.setVehicleSize(m_VehicleSize);
                int rows = busiService.saveVehicleSnapInfo(snapPo);
                log.debug("已保存{}条车辆流水信息。", rows);


                //更新引导屏上显示的车位数量
                for (Led led : leds) {
                    if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                        if (led.getPublishStatus()==1) {
                            try {
                                led.setRownum(1);
                                LEDCommunicationUtil.setLedParkingPlaceNum(led.getRownum(), remainPlaces.getRegionRemainPlaces(), led.getLedIp(), led.getPort());
                            } catch (IOException e) {
                                log.error("往区域余位屏({})推送信息异常！-->{}",led.getLedIp(), e);
                                //e.printStackTrace();
                            }
                        }
                    }else if(ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                        if (led.getPublishStatus()==1) {
                            try {
                                LEDCommunicationUtil.setLedParkingPlaceNum17Bytes(0, led.getRownum(), remainPlaces.getFloorRemainPlaces(), led.getLedIp(), led.getPort());
                            } catch (IOException e) {
                                log.error("往楼层余位屏({})推送信息异常！-->{}",led.getLedIp(), e);
                                //e.printStackTrace();
                            }
                        }
                    }else{
                        log.error("id为{}的LED屏类型(led_type)在数据库中配置错误！-->{}", led.getId(), led.getLedType());
                    }
                }




            }else {
                log.debug("未订阅的相机事件值：{}", dwAlarmType);
            }
            return 0;
        }catch (Exception e){
            log.error("相机事件处理异常！-->{}", e);
            e.printStackTrace();
            return -1;
        }
    }











    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<Led> getLeds() {
        return leds;
    }

    public void setLeds(List<Led> leds) {
        this.leds = leds;
    }

    public static class Camera{
        private String id;
        private String account;
        private String password;
        private Integer port;
        private String cameraIp;
        private String cameraType;
        private String channelId;
        private String regionId;
        private String floorId;
        private Integer regionRemainPlaces;
        private Integer floorRemainPlaces;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getCameraIp() {
            return cameraIp;
        }

        public void setCameraIp(String cameraIp) {
            this.cameraIp = cameraIp;
        }

        public String getCameraType() {
            return cameraType;
        }

        public void setCameraType(String cameraType) {
            this.cameraType = cameraType;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getFloorId() {
            return floorId;
        }

        public void setFloorId(String floorId) {
            this.floorId = floorId;
        }

        public Integer getRegionRemainPlaces() {
            return regionRemainPlaces;
        }

        public void setRegionRemainPlaces(Integer regionRemainPlaces) {
            this.regionRemainPlaces = regionRemainPlaces;
        }

        public Integer getFloorRemainPlaces() {
            return floorRemainPlaces;
        }

        public void setFloorRemainPlaces(Integer floorRemainPlaces) {
            this.floorRemainPlaces = floorRemainPlaces;
        }
    }



    public static class Led{
        private String id;
        private String ledIp;
        private Integer port;
        private Integer rownum;
        private String ledType;
        private Integer publishStatus;
        private Integer publishType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLedIp() {
            return ledIp;
        }

        public void setLedIp(String ledIp) {
            this.ledIp = ledIp;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer getRownum() {
            return rownum;
        }

        public void setRownum(Integer rownum) {
            this.rownum = rownum;
        }

        public String getLedType() {
            return ledType;
        }

        public void setLedType(String ledType) {
            this.ledType = ledType;
        }

        public Integer getPublishStatus() {
            return publishStatus;
        }

        public void setPublishStatus(Integer publishStatus) {
            this.publishStatus = publishStatus;
        }

        public Integer getPublishType() {
            return publishType;
        }

        public void setPublishType(Integer publishType) {
            this.publishType = publishType;
        }
    }
}
