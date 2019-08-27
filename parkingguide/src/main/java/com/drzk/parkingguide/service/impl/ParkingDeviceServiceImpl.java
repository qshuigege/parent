package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import com.drzk.parkingguide.camera.NetSDKLib;
import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.dao.ParkingChannelDao;
import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.dao.ParkingPublishDao;
import com.drzk.parkingguide.dao.ParkingRegionDao;
import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.service.ParkingDeviceService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.util.ProjConstants;
import com.drzk.parkingguide.vo.ParkingChannelVo;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ParkingDeviceServiceImpl implements ParkingDeviceService {

    private Logger log = LoggerFactory.getLogger(ParkingDeviceServiceImpl.class);

    @Autowired
    private ParkingDeviceDao dao;

    @Autowired
    private ParkingRegionDao regionDao;

    @Autowired
    private ParkingPublishDao publishDao;

    @Autowired
    private ParkingChannelDao channelDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionAddParkingDevice(ParkingDeviceVo deviceVo, HttpServletRequest request) throws Exception{
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"deviceType"}, deviceVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "deviceType参数不能为空！");
        }
        if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
            String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "deviceAcc", "devicePwd"};
            ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
            if (!validationResult.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
            }
            deviceVo.setCameraType(null);
            deviceVo.setLedType(null);
            deviceVo.setLedGroupId(null);
            deviceVo.setLedGroupName(null);
            deviceVo.setLedPartNum(null);
            deviceVo.setScreenBrightness(null);
            deviceVo.setLedRownum(null);
            deviceVo.setRegionId(null);
            deviceVo.setFloorId(null);
            deviceVo.setStatus("0");
        }else if (ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){
            if (ProjConstants.LED_FLOORLED.equals(deviceVo.getLedType())){//楼层余位屏regionId为空
                String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "ledPartNum", "ledType", "screenBrightness"};
                ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
                if (!validationResult.isSuccess()){
                    return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
                }
                deviceVo.setChannelId(null);
                deviceVo.setRegionId(null);
                deviceVo.setFloorId(null);

            }else if (ProjConstants.LED_REGIONLED.equals(deviceVo.getLedType())){//区域余位屏regionId、floorId都不能为空
                String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "floorId", "regionId", "ledType", "screenBrightness"};
                ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
                if (!validationResult.isSuccess()){
                    return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
                }
                //检查参数中floorId和regionId是否配套（检查数据库中regionId是否属于floorId）
                ParkingRegionVo regionVo = new ParkingRegionVo();
                regionVo.setId(deviceVo.getRegionId());
                regionVo.setFloorId(deviceVo.getFloorId());
                regionVo = regionDao.queryByIdAndOtherCols(regionVo);
                if (null == regionVo){
                    return JsonResponse.fail("002", "所选区域不属于所选楼层！");
                }

            }else {
                return JsonResponse.fail("003", "ledType参数值错误！只能是楼层余位屏或区域余位屏！");
            }

            switch (deviceVo.getScreenBrightness()){
                case 1:
                    deviceVo.setScreenBrightness(3);
                    break;
                case 2:
                    deviceVo.setScreenBrightness(4);
                    break;
                case 3:
                    deviceVo.setScreenBrightness(9);
                    break;
                default:
                    deviceVo.setScreenBrightness(4);
                    break;
            }
            deviceVo.setCameraType(null);
            deviceVo.setDeviceAcc(null);
            deviceVo.setDevicePwd(null);
            deviceVo.setChannelId(null);


        }else {
            return JsonResponse.fail("004", "deviceType参数值("+deviceVo.getDeviceType()+")错误！只能是相机或LED屏！");
        }

        int rows = dao.queryByDeviceIp(deviceVo);
        if (rows>0){
            return JsonResponse.fail("005", "ip为"+deviceVo.getDeviceIp()+"的设备已存在，请勿重复添加！");
        }

        String id = UUID.randomUUID().toString();
        deviceVo.setId(id);
        int rows2 = dao.addParkingDevice(deviceVo);
        if(ProjConstants.LED_REGIONLED.equals(deviceVo.getLedType())) {
            //新添加区域余位屏，将该屏加入该屏所属区域下的所有相机的事件对象的ledList中
            //1. 查询该区域下的所有相机
            ParkingDeviceVo p = new ParkingDeviceVo();
            p.setRegionId(deviceVo.getRegionId());
            p.setDeviceType(ProjConstants.DEVICE_CAMERA);
            List<ParkingDeviceVo> camList = dao.queryDevicesByBelongToRegionId(p);
            for (ParkingDeviceVo camVo : camList) {
                Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                if (map != null) {//如果map为空，说明该相机是用户新增的或者登陆失败的
                    fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                    List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                    fAnalyzerDataCallBackImpl.Led led = new fAnalyzerDataCallBackImpl.Led();
                    led.setId(id);
                    led.setLedType(deviceVo.getLedType());
                    led.setPort(deviceVo.getDevicePort());
                    led.setLedIp(deviceVo.getDeviceIp());
                    led.setRownum(1);
                    led.setPublishType(2);
                    //查询该屏所属区域的发布状态
                    ParkingPublishVo publishVo = new ParkingPublishVo();
                    publishVo.setRegionId(deviceVo.getRegionId());
                    publishVo.setPublishType(2);
                    publishVo = publishDao.queryByRegionId(publishVo);
                    if (publishVo!=null) {
                        led.setPublishStatus(publishVo.getStatus());
                    }else {
                        led.setPublishStatus(0);
                    }
                    leds.add(led);
                }
            }
        }
        if (rows2>0) {
            return JsonResponse.success("成功新增1条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }

    }

    @Override
    public JsonResponse queryParkingDevicePage(ParkingDeviceVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingDeviceVo> lst = dao.queryParkingDevicePage(vo);
        PageInfo<ParkingDeviceVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionDeleteById(List<ParkingDevicePo> list, HttpServletRequest request) {

        //删除设备时，如果是删除相机，删除前要先取消订阅和退出登录；如果是删除LED屏，要根据屏的类型相应的将屏信息从相机事件对象的ledList中移除
        int rows = 0;
        for (ParkingDevicePo devicePo : list) {
            ParkingDeviceVo deviceVo = dao.queryById(devicePo);
            if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
                if (deviceVo.getCameraType()!=null){//如果相机绑定到了通道，删除之前需要更新所属通道的inRecogCamId或outRecogCamId为null，取消订阅和退出登录
                    List<String> cameraIds = new ArrayList<>();
                    cameraIds.add(deviceVo.getId());
                    CameraCommunicationUtil.unsubscribeAndLogout(cameraIds);//删除前先取消订阅和退出登录

                    ParkingChannelVo channelVo = new ParkingChannelVo();
                    channelVo.setId(deviceVo.getChannelId());
                    if ("入口相机".equals(deviceVo.getCameraType())){
                        channelDao.updateInRecogCamId(channelVo);
                    }else if("出口相机".equals(deviceVo.getCameraType())){
                        channelDao.updateOutRecogCamId(channelVo);
                    }
                }

            }else if (ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){
                if (ProjConstants.LED_FLOORLED.equals(deviceVo.getLedType())){
                    //删除楼层余位屏，需要将楼层屏从已登录的相机的事件对象的ledList中移除。
                    //但是楼层屏没有保存与之关联的楼层信息，只能通过发布信息表根据楼层屏id查到所有的楼层信息。
                    //查到几条数据，说明就有几个楼层发布了，将LED屏从已发布的楼层下的所有相机的事件对象的ledList中移除。
                    //正常情况下，发布表中未添加某楼层的发布信息，已登录的相机的事件对象的ledList中是不会存在该楼层屏的
                    ParkingPublishVo publishVo = new ParkingPublishVo();
                    publishVo.setLedPartId(deviceVo.getId());
                    List<ParkingPublishVo> publishVoList = publishDao.queryByLedPartId(publishVo);//从发布信息表中根据ledPartId查到floorId，未发布则返回null
                    for (ParkingPublishVo pVo: publishVoList){
                        ParkingDeviceVo deviceVoParam = new ParkingDeviceVo();
                        deviceVoParam.setFloorId(pVo.getFloorId());
                        deviceVoParam.setDeviceType(ProjConstants.DEVICE_CAMERA);
                        List<ParkingDeviceVo> camVoList = dao.queryDevicesByBelongToFloorId(deviceVoParam);//根据floorId查询该楼层下所有相机
                        //移除所有相机事件对象的楼层余位屏
                        for (ParkingDeviceVo camVo : camVoList) {
                            Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                            if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                                fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                                List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                                List<fAnalyzerDataCallBackImpl.Led> newLeds = new ArrayList<>();
                                for (fAnalyzerDataCallBackImpl.Led led : leds) {
                                    if (!ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                                        newLeds.add(led);
                                    }
                                }
                                callBack.setLeds(newLeds);
                            }
                        }

                    }

                }else if (ProjConstants.LED_REGIONLED.equals(deviceVo.getLedType())){
                    ParkingDeviceVo deviceVoParam = new ParkingDeviceVo();
                    deviceVoParam.setRegionId(deviceVo.getRegionId());
                    deviceVoParam.setDeviceType(ProjConstants.DEVICE_CAMERA);
                    List<ParkingDeviceVo> camVoList = dao.queryDevicesByBelongToRegionId(deviceVoParam);//根据regionId查询该区域下所有相机
                    //移除所有相机事件对象的区域余位屏
                    for (ParkingDeviceVo camVo : camVoList) {
                        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                        if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                            fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                            List<fAnalyzerDataCallBackImpl.Led> newLeds = new ArrayList<>();
                            for (fAnalyzerDataCallBackImpl.Led led : leds) {
                                if (!ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                                    newLeds.add(led);
                                }
                            }
                            callBack.setLeds(newLeds);
                        }
                    }
                }/*else {
                    log.error("设备信息表中id为{}的数据led_type字段内容错误！-->{}", devicePo.getId(), deviceVo.getLedType());
                    return JsonResponse.fail("设备信息表中id为"+devicePo.getId()+"的数据led_type字段内容错误！-->"+deviceVo.getLedType());
                }*/
            }/*else{
                log.error("设备信息表中id为{}的数据device_type字段内容错误！-->{}", devicePo.getId(), deviceVo.getDeviceType());
                return JsonResponse.fail("设备信息表中id为"+devicePo.getId()+"的数据device_type字段内容错误！-->"+deviceVo.getDeviceType());
            }*/
            ArrayList<ParkingDevicePo> paramList = new ArrayList<>();
            paramList.add(devicePo);
            rows += dao.deleteByIdBatch(paramList);
        }
        return JsonResponse.success(rows);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionUpdateById(ParkingDeviceVo deviceVo, HttpServletRequest request) throws Exception{

        if (StringUtils.isEmpty(deviceVo.getId())){
            return JsonResponse.fail("001", "参数中id字段内容不能为空！");
        }
        if (!ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())&&!ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){
            return JsonResponse.fail("004", "deviceType字段内容错误，只能是相机和LED屏！");
        }

        ParkingDeviceVo dbVal = dao.queryById(new ParkingDevicePo(deviceVo.getId()));//原始数据
        if (!deviceVo.getDeviceType().equals(dbVal.getDeviceType())){
            return JsonResponse.fail("006", "不能将设备类型("+dbVal.getDeviceType()+")更改为("+deviceVo.getDeviceType()+")");
        }

        if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
            String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "deviceAcc", "devicePwd"};
            ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
            if (!validationResult.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
            }
            //不允许更改设备的归属信息
            String oldFloorId = StringUtils.isEmpty(dbVal.getFloorId())?"":dbVal.getFloorId();
            String oldRegionId = StringUtils.isEmpty(dbVal.getRegionId())?"":dbVal.getRegionId();
            String oldChannelId = StringUtils.isEmpty(dbVal.getId())?"":dbVal.getId();
            String newFloorId = StringUtils.isEmpty(deviceVo.getFloorId())?"":deviceVo.getFloorId();
            String newRegionId = StringUtils.isEmpty(deviceVo.getRegionId())?"":deviceVo.getRegionId();
            String newChannelId = StringUtils.isEmpty(deviceVo.getId())?"":deviceVo.getId();
            if (!newFloorId.equals(oldFloorId)||!newRegionId.equals(oldRegionId)||!newChannelId.equals(oldChannelId)){
                return JsonResponse.fail("007", "不支持直接更新相机的归属信息，请在通道管理模块更改！");
            }
            deviceVo.setLedType(null);
            deviceVo.setLedGroupId(null);
            deviceVo.setLedGroupName(null);
            deviceVo.setLedPartNum(null);
            deviceVo.setScreenBrightness(null);
            deviceVo.setLedRownum(null);
        }else {
            if (!deviceVo.getLedType().equals(dbVal.getLedType())){
                return JsonResponse.fail("008", "不能将屏幕类型("+dbVal.getLedType()+")更改为("+deviceVo.getLedType()+")");
            }

            if (ProjConstants.LED_FLOORLED.equals(deviceVo.getLedType())){//楼层余位屏regionId为空
                String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "ledPartNum", "ledType", "screenBrightness"};
                ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
                if (!validationResult.isSuccess()){
                    return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
                }
                deviceVo.setRegionId(null);

            }else if (ProjConstants.LED_REGIONLED.equals(deviceVo.getLedType())){//区域余位屏regionId、floorId都不能为空
                String[] validateFields = new String[]{"deviceName", "deviceType", /*"deviceBrand",*/ "deviceIp", "devicePort", "floorId", "regionId", "ledType", "screenBrightness"};
                ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, deviceVo);
                if (!validationResult.isSuccess()){
                    return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
                }
                //检查参数中floorId和regionId是否配套（检查数据库中regionId是否属于floorId）
                ParkingRegionVo regionVo = new ParkingRegionVo();
                regionVo.setId(deviceVo.getRegionId());
                regionVo.setFloorId(deviceVo.getFloorId());
                regionVo = regionDao.queryByIdAndOtherCols(regionVo);
                if (null == regionVo){
                    return JsonResponse.fail("002", "所选区域不属于所选楼层！");
                }

            }else {
                return JsonResponse.fail("003", "ledType参数值()错误！只能是楼层余位屏或区域余位屏！");
            }
            switch (deviceVo.getScreenBrightness()){
                case 1:
                    deviceVo.setScreenBrightness(3);
                    break;
                case 2:
                    deviceVo.setScreenBrightness(4);
                    break;
                case 3:
                    deviceVo.setScreenBrightness(9);
                    break;
                default:
                    deviceVo.setScreenBrightness(4);
                    break;
            }
            deviceVo.setDeviceAcc(null);
            deviceVo.setDevicePwd(null);
            deviceVo.setCameraType(null);
            deviceVo.setChannelId(null);


        }
        if (!deviceVo.getDeviceIp().equals(dbVal.getDeviceIp())) {
            int count = dao.queryByDeviceIp(deviceVo);
            if (count > 0) {
                return JsonResponse.fail("005", "ip为" + deviceVo.getDeviceIp() + "的设备已存在，无法更新！");
            }
        }




        int rows = 0;
        if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
            rows = dao.updateCameraById(deviceVo);//执行更新
        }else if (ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){
            rows = dao.updateLedById(deviceVo);
        }




        //执行更新后，更新相机事件相关句柄信息
        if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
            //如果相机已经是登陆的，更新相机相关句柄信息(因为相机的归属信息没变，无需更新对应的led信息)
            Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(deviceVo.getId());
            if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                fAnalyzerDataCallBackImpl.Camera camera = callBack.getCamera();
                camera.setAccount(deviceVo.getDeviceAcc());
                camera.setCameraIp(deviceVo.getDeviceIp());
                camera.setCameraType(deviceVo.getCameraType());
                camera.setPassword(deviceVo.getDevicePwd());
                camera.setPort(deviceVo.getDevicePort());
            }
        }else if (ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){

            if (ProjConstants.LED_FLOORLED.equals(deviceVo.getLedType())){
                ParkingPublishVo publishVo = new ParkingPublishVo();
                publishVo.setLedPartId(deviceVo.getId());
                List<ParkingPublishVo> publishVoList = publishDao.queryByLedPartId(publishVo);
                for (ParkingPublishVo pVo : publishVoList){
                    ParkingDeviceVo deviceVoParam = new ParkingDeviceVo();
                    deviceVoParam.setDeviceType(ProjConstants.DEVICE_CAMERA);
                    deviceVoParam.setFloorId(pVo.getFloorId());
                    List<ParkingDeviceVo> camVoList = dao.queryDevicesByBelongToFloorId(deviceVoParam);
                    for (ParkingDeviceVo camVo : camVoList) {
                        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                        if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                            fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                            for (fAnalyzerDataCallBackImpl.Led led : leds) {
                                if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                                    led.setLedIp(deviceVo.getDeviceIp());
                                    led.setPort(deviceVo.getDevicePort());
                                }
                            }
                        }
                    }
                }
            }else if (ProjConstants.LED_REGIONLED.equals(deviceVo.getLedType())){
                //如果更改了所属区域，则将区域屏从所属原来楼层区域下相机事件对象的ledList中移除，加入更新后楼层区域下相机事件对象的ledList中
                if (!deviceVo.getRegionId().equals(dbVal.getRegionId())){//不相等则更新了所属区域
                    ParkingDeviceVo deviceVoParam = new ParkingDeviceVo();
                    deviceVoParam.setRegionId(dbVal.getRegionId());//原regionId
                    deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
                    List<ParkingDeviceVo> camVoList = dao.queryDevicesByBelongToRegionId(deviceVoParam);//查询所属原区域下所有相机
                    for (ParkingDeviceVo camVo : camVoList) {
                        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                        if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                            fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                            for (fAnalyzerDataCallBackImpl.Led led : leds) {
                                if (deviceVo.getId().equals(led.getId())){
                                    leds.remove(led);//移除
                                    break;
                                }
                            }
                        }
                    }

                    deviceVoParam.setRegionId(deviceVo.getRegionId());//更新后regionId
                    deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
                    camVoList = dao.queryDevicesByBelongToRegionId(deviceVoParam);//查询更新后所属区域下所有相机
                    for (ParkingDeviceVo camVo : camVoList) {
                        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                        if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                            fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                            fAnalyzerDataCallBackImpl.Led led = new fAnalyzerDataCallBackImpl.Led();
                            //led.setLedType(deviceVo.getLedType());
                            led.setId(deviceVo.getId());
                            led.setLedType(deviceVo.getLedType());
                            led.setPort(deviceVo.getDevicePort());
                            led.setLedIp(deviceVo.getDeviceIp());
                            led.setRownum(1);
                            led.setPublishType(2);
                            //查询该屏所属区域的发布状态
                            ParkingPublishVo publishVo = new ParkingPublishVo();
                            publishVo.setRegionId(deviceVo.getRegionId());
                            publishVo.setPublishType(2);
                            publishVo = publishDao.queryByRegionId(publishVo);
                            if (publishVo!=null) {
                                led.setPublishStatus(publishVo.getStatus());
                            }else {
                                led.setPublishStatus(0);
                            }
                            leds.add(led);
                        }
                    }


                }else {//相等则没更新区域，只需更新屏幕基本信息

                    ParkingDeviceVo deviceVoParam = new ParkingDeviceVo();
                    deviceVoParam.setRegionId(deviceVo.getRegionId());
                    deviceVoParam.setDeviceType(ProjConstants.DEVICE_CAMERA);
                    List<ParkingDeviceVo> camVoList = dao.queryDevicesByBelongToRegionId(deviceVoParam);
                    for (ParkingDeviceVo camVo : camVoList) {
                        Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                        if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                            fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                            for (fAnalyzerDataCallBackImpl.Led led : leds) {
                                if (led.getId().equals(deviceVo.getId())){
                                    led.setLedIp(deviceVo.getDeviceIp());
                                    led.setPort(deviceVo.getDevicePort());
                                }
                            }
                        }
                    }
                }
            }

        }

        if (rows > 0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新失败！");
        }
    }

    @Override
    public JsonResponse queryById(ParkingDevicePo po, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        return JsonResponse.success(dao.queryById(po));
    }

    @Override
    public List<ParkingDeviceVo> queryUsableCamerasOfChannel(ParkingDeviceVo deviceVo) {
        return dao.queryUsableCamerasOfChannel(deviceVo);
    }

    /**
     * 更新相机登录状态
     * @param deviceVo
     * @return
     */
    @Override
    public JsonResponse changeCameraLoginState(ParkingDeviceVo deviceVo) throws Exception{
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "cameraLogin"}, deviceVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
        }
        ParkingDeviceVo camera = dao.queryById(new ParkingDevicePo(deviceVo.getId()));
        if (camera == null){
            return JsonResponse.fail("009", "id不存在！");
        }
        if (camera.getCameraType()==null){
            return JsonResponse.fail("010", "该相机尚未绑定到通道，无法更新登录状态！");
        }
        if (deviceVo.getCameraLogin()==1) {//登录
            fAnalyzerDataCallBackImpl callBack = ParkingChannelServiceImpl.buildCallBack(camera, dao, publishDao);
            int ret = CameraCommunicationUtil.loginAndSubscribe(callBack);
            if (ret > 0){
                return JsonResponse.success("登录成功！");
            }else {
                return JsonResponse.fail("012", "登录失败，请检查相机配置信息和网络状态！");
            }
        }else if (deviceVo.getCameraLogin() == 0){//取消登录
            CameraCommunicationUtil.unsubscribeAndLogout(deviceVo);
            return JsonResponse.success("操作成功！");
        }else {
            return JsonResponse.fail("011", "cameraLogin的值错误，只能是0或1");
        }

    }
}
