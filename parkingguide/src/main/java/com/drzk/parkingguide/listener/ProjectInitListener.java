package com.drzk.parkingguide.listener;

import com.drzk.parkingguide.camera.*;
import com.drzk.parkingguide.config.StaticResourceConfiguration;
import com.drzk.parkingguide.service.ParkingBusinessService;
import com.drzk.parkingguide.service.ParkingSettingsService;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ProjConstants;
import com.drzk.parkingguide.util.SpringApplicationContextUtil;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import com.drzk.parkingguide.vo.ParkingSettingsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectInitListener implements ApplicationListener<ApplicationReadyEvent> {

    public static List<ParkingSettingsVo> SYSTEM_SETTINGS;

    private Logger log = LoggerFactory.getLogger(ProjectInitListener.class);

    @Autowired
    private ParkingBusinessService busiService;

    @Autowired
    private ParkingSettingsService settingsService;

    @Autowired
    private StaticResourceConfiguration staticRes;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        LEDCommunicationUtil.ledConnTimeout = Integer.parseInt(SpringApplicationContextUtil.getApplicationContext().getBean(StaticResourceConfiguration.class).getTimeout());
        //获取系统配置信息
        ParkingSettingsVo settingsVo = new ParkingSettingsVo();
        SYSTEM_SETTINGS= settingsService.queryParkingSettingsPage(settingsVo);

        //初始化相机SDK
        log.info("初始化相机SDK...");
        Map<String, Object> config = new HashMap<>();
        config.put("waitTime", Integer.parseInt(staticRes.getWaitTime()));
        config.put("tryTimes", Integer.parseInt(staticRes.getTryTimes()));
        config.put("nConnectTime", Integer.parseInt(staticRes.getnConnectTime()));
        config.put("nGetConnInfoTime", Integer.parseInt(staticRes.getnGetConnInfoTime()));
        boolean initFlag = CameraCommunicationUtil.initSDK(config, new fDisConnectImpl(), new fHaveReConnectImpl());
        if (!initFlag){
            log.error("相机SDK初始化失败，程序无法启动！");
            SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
        }
        log.info("初始化相机SDK结束。");


        //查询所有相机以及和相机关联的楼层余位屏和区域余位屏，整理好关联关系，用这些关联好的数据构建相机事件回调对象fAnalyzerDataCallBackImpl.
        //1.查询设备表的所有数据（包括所有相机和所有LED屏）
        List<ParkingDeviceVo> allDevices = busiService.queryAllDevices();
        //2.将相机和LED屏分类
        List<ParkingDeviceVo> allCameras = new ArrayList<>();//保存所有相机的集合
        List<ParkingDeviceVo> allLeds = new ArrayList<>();//保存所有LED的集合
        for (ParkingDeviceVo deviceVo : allDevices) {
            if (ProjConstants.DEVICE_CAMERA.equals(deviceVo.getDeviceType())){
                allCameras.add(deviceVo);
            }else if (ProjConstants.DEVICE_LED.equals(deviceVo.getDeviceType())){
                allLeds.add(deviceVo);
            }else {
                log.error("相机([{}][{}])的类型信息错误！deviceType-->{}", deviceVo.getId(), deviceVo.getDeviceIp(), deviceVo.getDeviceType());
            }
        }
        log.info("所有相机信息-->{}", JsonUtils.objectToJson(allCameras));
        //3.将楼层余位屏和区域余位屏分类
        List<ParkingDeviceVo> floorLeds = new ArrayList<>();//保存所有楼层余位屏的集合
        List<ParkingDeviceVo> regionLeds = new ArrayList<>();//保存所有区域余位屏的集合
        for (ParkingDeviceVo led : allLeds) {
            if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                floorLeds.add(led);
            }else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                regionLeds.add(led);
            }else {
                log.error("LED屏([{}][{}])的类型信息错误！ledType-->{}", led.getId(), led.getDeviceIp(), led.getLedType());
            }
        }

        log.info("所有楼层余位屏信息-->{}", JsonUtils.objectToJson(floorLeds));
        log.info("所有区域余位屏信息-->{}", JsonUtils.objectToJson(regionLeds));

        //4.将楼层余位屏转存到HashMap中，floorId作为key.(错误，楼层屏并没有floorId)
        Map<String, List<ParkingDeviceVo>> floorLedsMap = new HashMap<>();//保存所有楼层余位屏的Map
        /*for (ParkingDeviceVo floorLed : floorLeds) {
            if (floorLedsMap.containsKey(floorLed.getFloorId())){
                floorLedsMap.get(floorLed.getFloorId()).add(floorLed);
            }else {
                List<ParkingDeviceVo> temp = new ArrayList<>();
                temp.add(floorLed);
                floorLedsMap.put(floorLed.getFloorId(), temp);
            }
        }*/
        //4.将楼层余位屏转存到HashMap中，ledPartId作为key
        for (ParkingDeviceVo floorLed : floorLeds) {
            if (floorLedsMap.containsKey(floorLed.getId())){
                floorLedsMap.get(floorLed.getId()).add(floorLed);
            }else {
                List<ParkingDeviceVo> temp = new ArrayList<>();
                temp.add(floorLed);
                floorLedsMap.put(floorLed.getId(), temp);
            }
        }
        //5.将区域余位屏转存到HashMap中，regionId作为key
        Map<String, List<ParkingDeviceVo>> regionLedsMap = new HashMap<>();//保存所有区域余位屏的Map
        for (ParkingDeviceVo regionLed : regionLeds) {
            if (regionLedsMap.containsKey(regionLed.getRegionId())){
                regionLedsMap.get(regionLed.getRegionId()).add(regionLed);
            }else {
                List<ParkingDeviceVo> temp = new ArrayList<>();
                temp.add(regionLed);
                regionLedsMap.put(regionLed.getRegionId(), temp);
            }
        }
        //6.查询发布表的所有数据（包括所有楼层发布信息和所有区域发布信息）
        List<ParkingPublishVo> allPublishInfo = busiService.getAllPublishInfo();
        //7.将楼层发布信息和区域发布信息分类
        List<ParkingPublishVo> floorPublishInfo = new ArrayList<>();//保存所有楼层发布信息
        List<ParkingPublishVo> regionPublishInfo = new ArrayList<>();//保存所有区域发布信息
        for (ParkingPublishVo pbVo : allPublishInfo) {
            if (pbVo.getPublishType() == 1){
                floorPublishInfo.add(pbVo);
            }else if (pbVo.getPublishType() == 2){
                regionPublishInfo.add(pbVo);
            }else {
                log.error("id为{}的发布数据发布类型错误！publishType-->{}", pbVo.getId(), pbVo.getPublishType());
            }
        }

        //8.将楼层发布信息转存到HashMap中，floorId作为key
        Map<String, ParkingPublishVo> floorPublishInfoMap = new HashMap<>();
        for (ParkingPublishVo floor : floorPublishInfo) {
            floorPublishInfoMap.put(floor.getFloorId(), floor);
        }
        //9.将区域发布信息转存到HashMap中，regionId作为key
        Map<String, ParkingPublishVo> regionPublishInfoMap = new HashMap<>();
        for (ParkingPublishVo region : regionPublishInfo) {
            regionPublishInfoMap.put(region.getRegionId(), region);
        }


        //10.构建所有相机的事件回调对象fAnalyzerDataCallBackImpl
        List<fAnalyzerDataCallBackImpl> callBackList = new ArrayList<>();
        for (ParkingDeviceVo cam : allCameras) {
            if (StringUtils.isEmpty(cam.getFloorId())||StringUtils.isEmpty(cam.getRegionId())||StringUtils.isEmpty(cam.getChannelId())){
                log.info("忽略登录相机([{}][{}])", cam.getDeviceIp(), cam.getId());
                continue;//相机添加后没有绑定到通道，不登录
            }
            fAnalyzerDataCallBackImpl.Camera camera = new fAnalyzerDataCallBackImpl.Camera();
            camera.setId(cam.getId());
            camera.setFloorId(cam.getFloorId());
            camera.setRegionId(cam.getRegionId());
            camera.setChannelId(cam.getChannelId());
            camera.setCameraIp(cam.getDeviceIp());
            camera.setAccount(cam.getDeviceAcc());
            camera.setPassword(cam.getDevicePwd());
            camera.setPort(cam.getDevicePort());
            camera.setCameraType(cam.getCameraType());
            camera.setRegionRemainPlaces(cam.getRegionRemainPlaces());
            camera.setFloorRemainPlaces(cam.getFloorRemainPlaces());
            List<ParkingDeviceVo> floorLedsOfCurCam = floorLedsMap.get(floorPublishInfoMap.get(cam.getFloorId())==null?null:floorPublishInfoMap.get(cam.getFloorId()).getLedPartId());//当前相机对应的楼层屏
            List<ParkingDeviceVo> regionLedsOfCurCam = regionLedsMap.get(cam.getRegionId());//当前相机对应的区域屏
            List<ParkingDeviceVo> allLedsOfCurCam = new ArrayList<>();//当前相机对应的所有屏幕（包括楼层屏和区域屏）
            if (floorLedsOfCurCam!=null) {
                allLedsOfCurCam.addAll(floorLedsOfCurCam);
            }
            if (regionLedsOfCurCam!=null) {
                allLedsOfCurCam.addAll(regionLedsOfCurCam);
            }
            List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>();
            for (ParkingDeviceVo led: allLedsOfCurCam) {
                fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
                tm.setId(led.getId());
                tm.setLedIp(led.getDeviceIp());
                tm.setPort(led.getDevicePort());
                tm.setLedType(led.getLedType());
                if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                    ParkingPublishVo temp = floorPublishInfoMap.get(cam.getFloorId());
                    if (null == temp){
                        tm.setRownum(null);
                        tm.setPublishStatus(0);
                        tm.setPublishType(1);
                    }else {
                        tm.setRownum(temp.getLedRownum());
                        tm.setPublishStatus(temp.getStatus());
                        tm.setPublishType(temp.getPublishType());
                    }
                }else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                    ParkingPublishVo temp = regionPublishInfoMap.get(led.getRegionId());
                    tm.setRownum(1);
                    if (null == temp){
                        tm.setPublishStatus(0);
                        tm.setPublishType(2);
                    }else {
                        tm.setPublishStatus(temp.getStatus());
                        tm.setPublishType(temp.getPublishType());
                    }
                }
                ledList.add(tm);
            }
            callBackList.add(new fAnalyzerDataCallBackImpl(camera, ledList));
        }
        CameraCommunicationUtil.callBackList = callBackList;

        //11.登录相机和注册相机事件
        CameraCommunicationUtil.loginAndSubscribe(callBackList);

        //12.初始化LED屏幕内容
        log.info("初始化LED屏幕内容...");
        for (fAnalyzerDataCallBackImpl callBack : callBackList) {
            fAnalyzerDataCallBackImpl.Camera cam = callBack.getCamera();
            List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
            log.info("相机([{}][{}])对应的led-->{}", cam.getId(), cam.getCameraIp(), JsonUtils.objectToJson(leds));
            for (fAnalyzerDataCallBackImpl.Led led : leds) {
                if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                    if (led.getPublishStatus()==1) {
                        try {
                            led.setRownum(1);//区域余位屏行号默认为1
                            if (cam.getRegionRemainPlaces() == null){
                                log.error("区域信息表中id为（{}）的记录remain_places字段值为空！", cam.getRegionId());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            if (led.getLedIp() == null){
                                log.error("设备信息表中id为（{}）的记录device_ip字段值为空！", led.getId());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            if (led.getPort() == null){
                                log.error("设备信息表中ip为（{}）的LED屏数据device_port字段值为空！", led.getLedIp());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            LEDCommunicationUtil.setLedParkingPlaceNum(led.getRownum(), cam.getRegionRemainPlaces(), led.getLedIp(), led.getPort());
                        } catch (IOException e) {
                            log.error("程序初始化时往区域余位屏({})推送信息异常！-->{}",led.getLedIp(), e.getMessage());
                            //e.printStackTrace();
                        }
                    }
                }else if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                    if (led.getPublishStatus()==1) {
                        try {
                            if (led.getRownum() == null){
                                log.error("设备信息表中id为（{}）的记录led_rownum字段值为空！", led.getId());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            if (cam.getFloorRemainPlaces() == null){
                                log.error("楼层信息表中id为（{}）的记录remain_places字段值为空！", cam.getFloorId());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            if (led.getLedIp() == null){
                                log.error("设备信息表中id为（{}）的记录device_ip字段值为空！", led.getId());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            if (led.getPort() == null){
                                log.error("设备信息表中ip为（{}）的LED屏数据device_port字段值为空！", led.getLedIp());
                                SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
                            }
                            LEDCommunicationUtil.setLedParkingPlaceNum17Bytes(0, led.getRownum(), cam.getFloorRemainPlaces(), led.getLedIp(), led.getPort());
                        } catch (Exception e) {
                            log.error("程序初始化时往楼层余位屏({})推送信息异常！-->{}",led.getLedIp(), e.getMessage());
                            //e.printStackTrace();
                        }
                    }
                }else {
                    log.error("id为{}的LED屏类型(led_type)在数据库中配置错误！-->{}", led.getId(), led.getLedType());
                }
            }
        }
        log.info("初始化LED屏幕内容结束。");


    }


    /*{

        //初始化相机SDK
        log.info("初始化相机SDK...");
        Map<String, Object> config = new HashMap<>();
        config.put("waitTime", 5000);
        config.put("tryTimes", 1);
        config.put("nConnectTime", 10000);
        config.put("nGetConnInfoTime", 3000);
        boolean initFlag = CameraCommunicationUtil.initSDK(config, new fDisConnectImpl(), new fHaveReConnectImpl());
        if (!initFlag){
            log.error("相机SDK初始化失败，程序无法启动！");
            SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
        }
        log.info("初始化相机SDK结束。");

        //查询所有已发布的相机与LED屏的对应关系
        List<ParkingCameraLedRelationPo> simpleRelations = busiService.queryPublishedDevices();
        if (simpleRelations.size() == 0){
            log.info("数据库中没有已发布数据！");
            return;
        }
        *//*
        detailRelations:
        相机与对应LED屏的关系。每一个相机对应多个LED屏，将所有的这种对应关系保存到Map中，方便后面的业务操作。
        map的key为相机对象，value为对应的LED屏集合
         *//*
        Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations = busiService.injectDetailInfo(simpleRelations);

        //相机事件的业务回调类，每个相机需单独设置，因为相机有各自的对应led信息。
        List<fAnalyzerDataCallBackImpl> list = busiService.buildCameraCallBackObjects(detailRelations);

        log.info("登录相机并订阅相机事件...");
        //登录相机并订阅相机事件
        CameraCommunicationUtil.loginAndSubscribe(list);
        log.info("登录相机并订阅相机事件结束。");


        //初始化LED屏幕内容
        log.info("初始化LED屏幕内容...");
        detailRelations.forEach((cam, leds)-> leds.forEach(led->{
            if ("2".equals(led.getLedType())){
                try {
                    LEDCommunicationUtil.setLedParkingPlaceNum(led.getLedRownum(), cam.getRegionRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
                } catch (IOException e) {
                    log.error("程序初始化时往区域余位屏推送信息异常！-->{}", e.getMessage());
                }
            }else if ("1".equals(led.getLedType())){
                try {
                    LEDCommunicationUtil.setLedParkingPlaceNum(led.getLedRownum(), cam.getFloorRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
                } catch (IOException e) {
                    log.error("程序初始化时往楼层余位屏推送信息异常！-->{}", e.getMessage());
                }
            }else {
                log.error("id为{}的LED屏类型(led_type)在数据库中配置错误！-->{}", led.getId(), led.getLedType());
            }
        }));
        log.info("初始化LED屏幕内容结束。");

    }*/
}
