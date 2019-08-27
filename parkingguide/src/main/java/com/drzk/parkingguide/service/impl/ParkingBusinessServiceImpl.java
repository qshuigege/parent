package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.bo.RemainPlaces;
import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import com.drzk.parkingguide.camera.LEDCommunicationUtil;
import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.dao.*;
import com.drzk.parkingguide.po.ParkingCameraLedRelationPo;
import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.po.ParkingRegionPo;
import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.service.ParkingBusinessService;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ProjConstants;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingFloorVo;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
public class ParkingBusinessServiceImpl implements ParkingBusinessService {

    @Autowired
    private ParkingRegionDao regionDao;

    @Autowired
    private ParkingFloorDao floorDao;

    @Autowired
    private ParkingCameraLedRelationDao relationDao;

    @Autowired
    private ParkingSnapInfoDao snapInfoDao;

    @Autowired
    private ParkingDeviceDao deviceDao;

    @Autowired
    private ParkingPublishDao publishDao;

    private Logger log = LoggerFactory.getLogger(ParkingBusinessServiceImpl.class);


    @Override
    public List<ParkingCameraLedRelationPo> queryPublishedDevices() {
        return relationDao.queryAllPublishedDevices();
    }


    /**
     * 更新剩余车位数，驶入识别剩余车位数减1，驶出识别剩余车位数加1
     * @param camera
     * @param plusOrSubtract 1代表驶入识别，2代表驶出识别
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RemainPlaces updateRemainPlaces(fAnalyzerDataCallBackImpl.Camera camera, int plusOrSubtract) throws Exception {
        ParkingRegionVo regionVo = regionDao.queryById(new ParkingRegionPo(camera.getRegionId()));
        if (null == regionVo){
            throw new Exception("根据当前相机（{"+camera.getId()+"}）的regionId（{"+camera.getRegionId()+"}）在区域信息表中找不到数据！");
        }
        ParkingFloorVo floorVo = floorDao.queryById(new ParkingFloorVo(camera.getFloorId()));
        if (null == floorVo){
            throw new Exception("根据当前相机（{"+camera.getId()+"}）的floorId（{"+camera.getFloorId()+"}）在楼层信息表中找不到数据！");
        }
        if(null == regionVo.getRemainPlaces()){
            throw new Exception("区域信息表中id为（"+regionVo.getId()+"）的记录remain_places字段的值为null");
        }
        if (null == floorVo.getRemainPlaces()){
            throw new Exception("楼层信息表中id为（"+floorVo.getId()+"）的记录remain_places字段的值为null");
        }
        if (plusOrSubtract == 1) {//驶入识别
            regionVo.setRemainPlaces(regionVo.getRemainPlaces() - 1);
            floorVo.setRemainPlaces(floorVo.getRemainPlaces() - 1);
        }else if (plusOrSubtract == 2){//驶出识别
            regionVo.setRemainPlaces(regionVo.getRemainPlaces() + 1);
            floorVo.setRemainPlaces(floorVo.getRemainPlaces() + 1);
        }else {
            throw new Exception("ParkingBusinessServiceImpl.updateRemainPlaces()方法传入的参数("+plusOrSubtract+")错误！只能是1或2。1为剩余车位加1，2为剩余车位减1。");
        }
        regionDao.updateRemainPlaces(regionVo);
        floorDao.updateRemainPlaces(floorVo);
        RemainPlaces remainPlaces = new RemainPlaces();
        remainPlaces.setRegionRemainPlaces(regionVo.getRemainPlaces());
        remainPlaces.setFloorRemainPlaces(floorVo.getRemainPlaces());
        return remainPlaces;
    }

    /**
     * 保存车辆流水信息
     * @param snapPo
     * @return
     */
    @Override
    public int saveVehicleSnapInfo(ParkingSnapInfoPo snapPo) {
        return snapInfoDao.insert(snapPo);
    }

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public void permissionPublish(List<ParkingCameraLedRelationPo> simpleRelations, HttpServletRequest request) {
        //每次发布先删除该相机与LED的对应关系
        for (ParkingCameraLedRelationPo po : simpleRelations) {
            relationDao.deleteByCamIdAndLedId(po);
        }
        //插入当前发布的对应关系
        relationDao.insertBatch(simpleRelations);
        simpleRelations = relationDao.queryByCameraIds(simpleRelations);
        //查询出该相机与对应的LED的详细信息
        Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations = injectDetailInfo(simpleRelations);
        //构建相机事件的回调类
        List<fAnalyzerDataCallBackImpl> callBacks = buildCameraCallBackObjects(detailRelations);
        //登录该相机并订阅事件
        CameraCommunicationUtil.loginAndSubscribe(callBacks);
        //更新LED屏显示内容
        sendRemainPlacesToLED(detailRelations);

        //更新摄像机状态为已发布
        List<ParkingDevicePo> cameraIdList = new ArrayList<>();
        for (ParkingCameraLedRelationPo po : simpleRelations) {
            ParkingDevicePo devicePo = new ParkingDevicePo();
            devicePo.setId(po.getCameraId());
            devicePo.setStatus("已发布");
            cameraIdList.add(devicePo);
        }
        deviceDao.updateCameraStatusByCameraIdBatch(cameraIdList);
    }*/

    //@Override
    //@Transactional(rollbackFor = Exception.class)
    public void permissionPublish_bak(List<ParkingCameraLedRelationPo> simpleRelations, HttpServletRequest request) {
        //每次发布先删除该相机与LED的对应关系
        for (ParkingCameraLedRelationPo po : simpleRelations) {
            relationDao.deleteByCamIdAndLedId(po);
        }
        //插入当前发布的对应关系
        relationDao.insertBatch(simpleRelations);
        simpleRelations = relationDao.queryByCameraIds(simpleRelations);
        //查询出该相机与对应的LED的详细信息
        Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations = injectDetailInfo(simpleRelations);
        //构建相机事件的回调类
        List<fAnalyzerDataCallBackImpl> callBacks = buildCameraCallBackObjects(detailRelations);
        //登录该相机并订阅事件
        CameraCommunicationUtil.loginAndSubscribe(callBacks);
        //更新LED屏显示内容
        sendRemainPlacesToLED(detailRelations);

        //更新摄像机状态为已发布
        List<ParkingDevicePo> cameraIdList = new ArrayList<>();
        for (ParkingCameraLedRelationPo po : simpleRelations) {
            ParkingDevicePo devicePo = new ParkingDevicePo();
            devicePo.setId(po.getCameraId());
            devicePo.setStatus("已发布");
            cameraIdList.add(devicePo);
        }
        deviceDao.updateCameraStatusByCameraIdBatch(cameraIdList);
    }

    //@Override
    public void permissionPublishRegion_bak(ParkingDeviceVo deviceVo, HttpServletRequest request) {
        deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
        List<ParkingDeviceVo> cameraList = deviceDao.queryDevicesByBelongToRegionId(deviceVo);
        List<ParkingCameraLedRelationPo> temp = new ArrayList<>();
        for (ParkingDeviceVo cam : cameraList) {
            ParkingCameraLedRelationPo po = new ParkingCameraLedRelationPo();
            po.setCameraId(cam.getId());
            temp.add(po);
        }
        List<ParkingCameraLedRelationPo> simpleRelations = relationDao.queryByCameraIds(temp);
        permissionPublish_bak(simpleRelations, request);
    }

    //@Override
    public void permissionPublishFloor_bak(ParkingDeviceVo deviceVo, HttpServletRequest request) {
        List<ParkingDeviceVo> devicePoList = deviceDao.queryDevicesByBelongToFloorId(deviceVo);
        List<ParkingCameraLedRelationPo> temp = new ArrayList<>();
        for (ParkingDeviceVo cam : devicePoList) {
            ParkingCameraLedRelationPo po = new ParkingCameraLedRelationPo();
            po.setCameraId(cam.getId());
            temp.add(po);
        }
        List<ParkingCameraLedRelationPo> simpleRelations = relationDao.queryByCameraIds(temp);
        permissionPublish_bak(simpleRelations, request);
    }


    /**
     * 根据regionId查询区域下所有摄像机和led屏幕，并发布
     * @param vo vo的regionId属性值由前端传过来
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String permissionPublishRegion(ParkingDeviceVo vo, HttpServletRequest request){
        return publishRegionInternal(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publishRegionInternal(ParkingDeviceVo vo){
        vo.setDeviceType(ProjConstants.DEVICE_CAMERA);
        List<ParkingDeviceVo> cameraList = deviceDao.queryParkingDevicePage(vo);
        if (cameraList.size()<1){
            log.error("根据区域id：{}找不到任何属于该区域的相机信息，请检查id是否正确或数据库数据是否存在！", vo.getRegionId());
            return "fail|根据区域id："+vo.getRegionId()+"找不到任何属于该区域的相机信息，请检查id是否正确或数据库数据是否存在！";
        }
        for (ParkingDeviceVo camera : cameraList) {
            if (null==camera.getFloorId()||"".equals(camera.getFloorId())||null==camera.getRegionId()||"".equals(camera.getRegionId())||null==camera.getChannelId()||"".equals(camera.getChannelId())){
                log.error("相机({},{})数据库信息不完整！floorId：{}，regionId：{}，channelId：{}", camera.getId(), camera.getDeviceIp(), camera.getFloorId(), camera.getRegionId(), camera.getChannelId());
                return "fail|相机("+camera.getId()+","+camera.getDeviceIp()+")数据库信息不完整！floorId："+camera.getFloorId()+"，regionId："+camera.getRegionId()+"，channelId："+camera.getChannelId();
            }
        }
        vo.setDeviceType(ProjConstants.DEVICE_LED);
        List<ParkingDeviceVo> ledList = deviceDao.queryParkingDevicePage(vo);
        Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations = new HashMap<>();
        for (ParkingDeviceVo cam : cameraList) {
            detailRelations.put(cam, ledList);
        }
        //构建相机事件的回调类
        List<fAnalyzerDataCallBackImpl> callBacks = buildCameraCallBackObjects(detailRelations);
        //登录该相机并订阅事件
        CameraCommunicationUtil.loginAndSubscribe(callBacks);
        //更新LED屏显示内容
        sendRemainPlacesToLED(detailRelations);


        List<ParkingDevicePo> cameraIdList = new ArrayList<>();
        for (fAnalyzerDataCallBackImpl cb : callBacks) {
            ParkingDevicePo devicePo = new ParkingDevicePo();
            devicePo.setId(cb.getCamera().getId());
            devicePo.setStatus("已发布");
            cameraIdList.add(devicePo);
        }
        deviceDao.updateCameraStatusByCameraIdBatch(cameraIdList);
        ParkingRegionVo regionVo = new ParkingRegionVo();
        regionVo.setId(vo.getRegionId());
        regionVo.setPublishState(1);
        //更新区域状态为已发布
        regionDao.updatePublishState(regionVo);
        //更新所属楼层状态为已发布
        ParkingFloorVo floorVo = new ParkingFloorVo();
        floorVo.setId(cameraIdList.get(0).getFloorId());
        floorVo.setPublishState(1);
        floorDao.updatePublishState(floorVo);
        return "success|区域"+vo.getRegionId()+"发布成功！";
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> permissionPublishFloor(ParkingRegionVo regionVo, HttpServletRequest request) {
        List<String> returnInfo = new ArrayList<>();
        returnInfo.add("success");
        List<ParkingRegionVo> regionVoList = regionDao.queryRegionsByBelongToFloorId(regionVo);
        if (regionVoList.size()<1){
            returnInfo.set(0, "fail");
            returnInfo.add("根据楼层id："+regionVo.getFloorId()+"查不到任何区域信息，请检查id是否正确或数据库数据是否存在！");
            log.error("根据楼层id：{}查不到任何区域信息，请检查id是否正确或数据库数据是否存在！", regionVo.getFloorId());
            return returnInfo;
        }
        for (ParkingRegionVo vo : regionVoList) {
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setRegionId(vo.getId());
            String retStr = permissionPublishRegion(deviceVo, request);
            String[] split = retStr.split("\\|");
            if ("fail".equals(split[0])){
                returnInfo.set(0, "fail");
            }
            returnInfo.add(split[1]);
        }
        ParkingFloorVo floorVo = new ParkingFloorVo();
        floorVo.setId(regionVo.getFloorId());
        floorDao.updatePublishState(floorVo);
        return returnInfo;
    }

    @Override
    public void sendRemainPlacesToLED(Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations) {
        detailRelations.forEach((cam, leds)-> leds.forEach(led->{
            if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                try {
                    LEDCommunicationUtil.setLedParkingPlaceNum(led.getLedRownum(), cam.getRegionRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
                } catch (IOException e) {
                    log.error("往区域余位屏推送信息异常！-->{}", e.getMessage());
                }
            }else if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                try {
                    LEDCommunicationUtil.setLedParkingPlaceNum(led.getLedRownum(), cam.getFloorRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
                } catch (IOException e) {
                    log.error("往楼层余位屏推送信息异常！-->{}", e.getMessage());
                }
            }else {
                log.error("id为{}的LED屏类型(led_type)在数据库中配置错误！-->{}", led.getId(), led.getLedType());
            }
        }));
    }

    @Override
    public Map<ParkingDeviceVo, List<ParkingDeviceVo>> injectDetailInfo(List<ParkingCameraLedRelationPo> simpleRelations){
        Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations = new HashMap<>();

        Map<String, List<ParkingCameraLedRelationPo>> temp = new HashMap<>();
        for (ParkingCameraLedRelationPo po : simpleRelations) {
            if (temp.containsKey(po.getCameraId())){
                temp.get(po.getCameraId()).add(po);
            }else {
                List<ParkingCameraLedRelationPo> lst = new ArrayList<>();
                lst.add(po);
                temp.put(po.getCameraId(), lst);
            }
        }

        Iterator<Map.Entry<String, List<ParkingCameraLedRelationPo>>> iter = temp.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String, List<ParkingCameraLedRelationPo>> entry = iter.next();
            ParkingDevicePo po = new ParkingDevicePo();
            po.setId(entry.getKey());
            //根据相机id查询相机设备信息
            ParkingDeviceVo cam = deviceDao.queryById(po);
            log.info("相机信息-->{}", JsonUtils.objectToJson(cam));
            if (cam == null) {
                log.error("数据库中不存在id为{}的相机信息", po.getId());
                continue;
            }

            List<ParkingDevicePo> lst = new ArrayList<>();
            for (ParkingCameraLedRelationPo p : entry.getValue()) {
                lst.add(new ParkingDevicePo(p.getLedPartId()));
            }
            //根据led id查询led设备信息
            List<ParkingDeviceVo> leds = deviceDao.queryByIds(lst);
            log.info("LED屏信息-->{}", JsonUtils.objectToJson(leds));

            //保存相机与该相机对应的led信息
            detailRelations.put(cam, leds);
        }
        return detailRelations;
    }

    @Override
    public List<fAnalyzerDataCallBackImpl> buildCameraCallBackObjects(Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations){
        List<fAnalyzerDataCallBackImpl> list = new ArrayList<>();
        detailRelations.forEach((cam, leds) -> {
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
            List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>() ;
            for (ParkingDeviceVo led: leds) {
                fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
                tm.setId(led.getId());
                tm.setLedIp(led.getDeviceIp());
                tm.setPort(led.getDevicePort());
                tm.setRownum(led.getLedRownum());
                tm.setLedType(led.getLedType());
                ledList.add(tm);
            }
            list.add(new fAnalyzerDataCallBackImpl(camera, ledList));
        });
        return list;
    }

    @Override
    public void permissionLogoutFromCamera(List<ParkingDeviceVo> camList, HttpServletRequest request) {
        List<String> cameraIdList = new ArrayList<>();
        camList.forEach(cam -> cameraIdList.add(cam.getId()));
        CameraCommunicationUtil.unsubscribeAndLogout(cameraIdList);
    }

    @Override
    public List<ParkingRegionVo> queryAllPublishedRegions() {
        ParkingRegionVo vo = new ParkingRegionVo();
        vo.setPublishState(1);
        return regionDao.queryRegionsByPublishState(vo);
    }

    @Override
    public List<ParkingFloorVo> getFloors(ParkingFloorVo floorVo){
        return floorDao.queryParkingFloorPage(floorVo);
    }

    @Override
    public List<ParkingRegionVo> getRegionsByBelongToFloorId(ParkingRegionVo regionVo){
        return regionDao.queryRegionsByBelongToFloorId(regionVo);
    }

    @Override
    public List<ParkingRegionVo> getRegions(ParkingRegionVo regionVo){
        return regionDao.queryParkingRegionPage(regionVo);
    }

    @Override
    public String permissionStopPublishRegion(ParkingDeviceVo regionId, HttpServletRequest request){
        List<ParkingDeviceVo> devicePoList = deviceDao.queryDevicesByBelongToRegionId(regionId);
        permissionLogoutFromCamera(devicePoList, request);
        return "操作已执行！";
    }

    @Override
    public String permissionStopPublishFloor(ParkingRegionVo floorId, HttpServletRequest request){
        List<ParkingRegionVo> regionVoList = regionDao.queryRegionsByBelongToFloorId(floorId);
        for (ParkingRegionVo regionVo : regionVoList) {
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setRegionId(regionVo.getId());
            permissionStopPublishRegion(deviceVo, request);
        }
        return "操作已执行！";
    }

    @Override
    public void correctRemainPlaces(ParkingDeviceVo deviceVo){
        //查出该区域下所有的区域余位屏，更新显示为新的余位数，同时更新楼层余位屏的余位数
        List<ParkingDeviceVo> regionLedList = deviceDao.queryDevicesByBelongToRegionId(deviceVo);
        List<ParkingDeviceVo> floorLedList = deviceDao.queryDevicesByBelongToFloorId(deviceVo);
        for (ParkingDeviceVo regionLed : regionLedList) {

        }
        //更新数据库区域余位数
        //更新数据库楼层余位数
    }

    @Override
    public List<ParkingDeviceVo> queryAllDevices() {
        return deviceDao.queryParkingDevicePage(new ParkingDeviceVo());
    }

    @Override
    public List<ParkingPublishVo> getAllPublishInfo() {
        return publishDao.queryAllWithPage(new ParkingPublishVo());
    }
}
