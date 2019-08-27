package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import com.drzk.parkingguide.camera.NetSDKLib;
import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.dao.ParkingChannelDao;
import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.dao.ParkingPublishDao;
import com.drzk.parkingguide.dao.ParkingRegionDao;
import com.drzk.parkingguide.po.ParkingChannelPo;
import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.service.ParkingChannelService;
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
public class ParkingChannelServiceImpl implements ParkingChannelService {

    private Logger log = LoggerFactory.getLogger(ParkingChannelServiceImpl.class);

    @Autowired
    private ParkingChannelDao dao;

    @Autowired
    private ParkingDeviceDao deviceDao;

    @Autowired
    private ParkingRegionDao regionDao;

    @Autowired
    private ParkingPublishDao publishDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionAddParkingChannel(ParkingChannelVo vo, HttpServletRequest request) throws Exception{
        /*ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"channelType"}, vo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数channelType不能为空！");
        }*/
        String[] validateFields = new String[]{"channelName", "regionId", "floorId", "channelType"};
        ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, vo);
        if (!validationResult.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
        }
        if (ProjConstants.CHANNEL_IN.equals(vo.getChannelType())){
            if (null==vo.getInRecogCameraId()||"".equals(vo.getInRecogCameraId())){
                return JsonResponse.fail("002", "选择驶入通道时，参数中inRecogCameraId字段内容不能为空！");
            }
            //检查选择的相机是否已经被其它通道绑定
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setId(vo.getInRecogCameraId());
            int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
            if (ret > 0){
                return JsonResponse.fail("007", "选择的相机"+vo.getInRecogCameraId()+"已绑定到其它通道，无法重复绑定！");
            }
            vo.setOutRecogCameraId(null);
        }else if (ProjConstants.CHANNEL_OUT.equals(vo.getChannelType())){
            if (null==vo.getOutRecogCameraId()||"".equals(vo.getOutRecogCameraId())){
                return JsonResponse.fail("003", "选择驶出通道时，参数中outRecogCameraId字段内容不能为空！");
            }
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setId(vo.getOutRecogCameraId());
            int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
            if (ret > 0){
                return JsonResponse.fail("007", "选择的相机"+vo.getOutRecogCameraId()+"已绑定到其它通道，无法重复绑定！");
            }
            vo.setInRecogCameraId(null);
        }else if (ProjConstants.CHANNEL_INANDOUT.equals(vo.getChannelType())){
            if (null==vo.getInRecogCameraId()||"".equals(vo.getInRecogCameraId())||null==vo.getOutRecogCameraId()||"".equals(vo.getOutRecogCameraId())){
                return JsonResponse.fail("004", "选择双向通道时，参数中inRecogCameraId, outRecogCameraId字段内容不能为空！");
            }
            if (vo.getInRecogCameraId().equals(vo.getOutRecogCameraId())){
                return JsonResponse.fail("008", "驶入识别和驶出识别不能相同！");
            }
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setId(vo.getInRecogCameraId());
            int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
            if (ret > 0){
                return JsonResponse.fail("007", "选择的相机"+vo.getInRecogCameraId()+"已绑定到其它通道，无法重复绑定！");
            }
            deviceVo.setId(vo.getOutRecogCameraId());
            ret = deviceDao.queryByCameraTypeAndId(deviceVo);
            if (ret > 0){
                return JsonResponse.fail("007", "选择的相机"+vo.getOutRecogCameraId()+"已绑定到其它通道，无法重复绑定！");
            }
        }else {
            return JsonResponse.fail("005", "channelType字段内容错误，只能是驶入通道、驶出通道或双向通道！");
        }
        //检查参数中floorId和regionId是否配套（检查数据库中regionId是否属于floorId）
        ParkingRegionVo regionVo = new ParkingRegionVo();
        regionVo.setId(vo.getRegionId());
        regionVo.setFloorId(vo.getFloorId());
        regionVo = regionDao.queryByIdAndOtherCols(regionVo);
        if (null == regionVo){
            return JsonResponse.fail("006", "所选区域不属于所选楼层！");
        }

        vo.setId(UUID.randomUUID().toString().toLowerCase());
        int i = dao.addParkingChannel(vo);


        ArrayList<ParkingDevicePo> ids = new ArrayList<>();
        //更新相机所属区域、所属楼层信息
        if (ProjConstants.CHANNEL_INANDOUT.equals(vo.getChannelType())){
            vo.setCameraId(vo.getInRecogCameraId());
            vo.setCameraType("入口相机");
            deviceDao.updateCameraLocationInfo(vo);
            vo.setCameraType("出口相机");
            vo.setCameraId(vo.getOutRecogCameraId());
            deviceDao.updateCameraLocationInfo(vo);

            ParkingDevicePo p1 = new ParkingDevicePo();
            ParkingDevicePo p2 = new ParkingDevicePo();
            p1.setId(vo.getInRecogCameraId());
            p2.setId(vo.getOutRecogCameraId());
            ids.add(p1);
            ids.add(p2);

        }else if (ProjConstants.CHANNEL_IN.equals(vo.getChannelType())){
            vo.setCameraType("入口相机");
            vo.setCameraId(vo.getInRecogCameraId());
            deviceDao.updateCameraLocationInfo(vo);


            ParkingDevicePo p1 = new ParkingDevicePo();
            p1.setId(vo.getInRecogCameraId());
            ids.add(p1);

        }else if (ProjConstants.CHANNEL_OUT.equals(vo.getChannelType())){
            vo.setCameraType("出口相机");
            vo.setCameraId(vo.getOutRecogCameraId());
            deviceDao.updateCameraLocationInfo(vo);


            ParkingDevicePo p2 = new ParkingDevicePo();
            p2.setId(vo.getOutRecogCameraId());
            ids.add(p2);

        }

        //登录相机并订阅事件
        //1. 查询上面添加通道绑定的相机信息
        List<ParkingDeviceVo> camList = deviceDao.queryByIds(ids);
        List<fAnalyzerDataCallBackImpl> callBackList = new ArrayList<>();
        //2. 查询相机对应的所有LED屏
        for (ParkingDeviceVo cam : camList) {
            ParkingDeviceVo p = new ParkingDeviceVo();
            p.setRegionId(cam.getRegionId());
            p.setDeviceType(ProjConstants.DEVICE_LED);
            p.setLedType(ProjConstants.LED_REGIONLED);
            List<ParkingDeviceVo> ledListOfCurCam = deviceDao.queryDevicesByBelongToRegionId(p);//查询该相机对应的所有区域余位屏，如果LED屏数据还没添加，该list长度为0
            ParkingPublishVo regionPublishVo = new ParkingPublishVo();
            regionPublishVo.setRegionId(cam.getRegionId());
            regionPublishVo.setPublishType(2);
            regionPublishVo = publishDao.queryByRegionIdAndPublishType(regionPublishVo);//获得当前相机所属区域的发布状态，在status字段中,如果发布数据不存在则返回null

            ParkingPublishVo floorPublishVo = new ParkingPublishVo();
            floorPublishVo.setFloorId(cam.getFloorId());
            floorPublishVo.setPublishType(1);
            floorPublishVo = publishDao.queryByFloorIdAndPublishType(floorPublishVo);//获得当前相机所属楼层的发布状态，在status字段中，如果发布数据不存在则返回null
            if (floorPublishVo!=null) {
                ParkingDeviceVo floorLed = deviceDao.queryById(new ParkingDevicePo(floorPublishVo.getLedPartId()));//查询该相机对应的楼层余位屏
                if (null != floorLed) {
                    ledListOfCurCam.add(floorLed);//区域余位屏和楼层余位屏保存到一起
                }
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
            List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>();
            for (ParkingDeviceVo led: ledListOfCurCam) {//当相机先于区域余位屏添加且相机所属楼层未发布，该list长度为0
                fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
                tm.setId(led.getId());
                tm.setLedIp(led.getDeviceIp());
                tm.setPort(led.getDevicePort());
                tm.setLedType(led.getLedType());
                if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                    if (null == floorPublishVo){
                        tm.setRownum(null);
                        tm.setPublishStatus(0);
                        tm.setPublishType(1);
                    }else {
                        tm.setRownum(floorPublishVo.getLedRownum());
                        tm.setPublishStatus(floorPublishVo.getStatus());
                        tm.setPublishType(floorPublishVo.getPublishType());
                    }
                }else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                    tm.setRownum(1);
                    if (null == regionPublishVo){
                        tm.setPublishStatus(0);
                        tm.setPublishType(2);
                    }else {
                        tm.setPublishStatus(regionPublishVo.getStatus());
                        tm.setPublishType(regionPublishVo.getPublishType());
                    }
                }
                ledList.add(tm);
            }
            callBackList.add(new fAnalyzerDataCallBackImpl(camera, ledList));
        }
        //登录相机和注册相机事件
        CameraCommunicationUtil.loginAndSubscribe(callBackList);

        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse queryParkingChannelPage(ParkingChannelVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingChannelVo> lst = dao.queryParkingChannelPage(vo);
        PageInfo<ParkingChannelVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionDeleteById(List<ParkingChannelVo> list, HttpServletRequest request) {
        //删除之前取消当前通道关联的相机的事件订阅并退出登录，再将相机的归属信息置空
        for (ParkingChannelVo vo : list) {
            ParkingChannelVo channelVo = dao.queryById(vo);
            if (!StringUtils.isEmpty(channelVo.getInRecogCameraId())){
                ArrayList<String> pL = new ArrayList<>();
                pL.add(channelVo.getInRecogCameraId());
                CameraCommunicationUtil.unsubscribeAndLogout(pL);
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(channelVo.getInRecogCameraId());
                deviceDao.updateBelongToIdsAndCameraTypeToNullById(deviceVo);
            }
            if (!StringUtils.isEmpty(channelVo.getOutRecogCameraId())){
                ArrayList<String> pL = new ArrayList<>();
                pL.add(channelVo.getOutRecogCameraId());
                CameraCommunicationUtil.unsubscribeAndLogout(pL);
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(channelVo.getOutRecogCameraId());
                deviceDao.updateBelongToIdsAndCameraTypeToNullById(deviceVo);
            }
        }
        int rows = dao.deleteByIdBatch(list);
        return JsonResponse.success(rows);
    }


    /**
     * 更新
     * @param vo
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public JsonResponse permissionUpdateById(ParkingChannelVo vo, HttpServletRequest request) throws Exception{

        String[] validateFields = new String[]{"id", "channelName", "regionId", "floorId", "channelType"};
        ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(validateFields, vo);
        if (!validationResult.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
        }

        ParkingChannelVo dbVal = dao.queryById(vo);//原始数据

        //不允许更改通道的归属信息
        if (!vo.getFloorId().equals(dbVal.getFloorId())||!vo.getRegionId().equals(dbVal.getRegionId())){
            return JsonResponse.fail("006", "不支持更新通道的归属信息，可以先删除再添加！");
        }

        //int rows = 0;
        List<ParkingDeviceVo> toBeUpdatedOldCams = new ArrayList<>();
        List<ParkingDeviceVo> toBeUpdatedNowCams = new ArrayList<>();
        if(ProjConstants.CHANNEL_IN.equals(vo.getChannelType())){
            if (StringUtils.isEmpty(vo.getInRecogCameraId())){
                return JsonResponse.fail("002", "更新为驶入通道时，参数中inRecogCameraId字段内容不能为空！");
            }

            //检查选择的相机是否已经被其它通道绑定
            if (!vo.getInRecogCameraId().equals(dbVal.getInRecogCameraId())) {
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(vo.getInRecogCameraId());
                int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
                if (ret > 0) {
                    return JsonResponse.fail("007", "选择的相机" + vo.getInRecogCameraId() + "已绑定到其它通道，无法重复绑定！");
                }
            }

            vo.setOutRecogCameraId(null);

            if (vo.getChannelType().equals(dbVal.getChannelType())){//如果原通道也是驶入通道
                if (!vo.getInRecogCameraId().equals(dbVal.getInRecogCameraId())){//如果相机更新了

                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getInRecogCameraId());
                    oldCam.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getInRecogCameraId());
                    nowCam.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCam);

                }//否则，说明本次更新只更新了通道的基本信息，只需执行通道的基本信息更新，不需要做其他操作

            }else {//原通道不是驶入通道
                if (ProjConstants.CHANNEL_OUT.equals(dbVal.getChannelType())){//原通道是驶出通道
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getOutRecogCameraId());
                    oldCam.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getInRecogCameraId());
                    nowCam.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCam);

                }else{//原通道是双向通道
                    ParkingDeviceVo oldCamIn = new ParkingDeviceVo();
                    oldCamIn.setId(dbVal.getInRecogCameraId());
                    oldCamIn.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCamIn);
                    ParkingDeviceVo oldCamOut = new ParkingDeviceVo();
                    oldCamOut.setId(dbVal.getOutRecogCameraId());
                    oldCamOut.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCamOut);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getInRecogCameraId());
                    nowCam.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCam);
                }
            }
        }else if (ProjConstants.CHANNEL_OUT.equals(vo.getChannelType())){
            if (StringUtils.isEmpty(vo.getOutRecogCameraId())){
                return JsonResponse.fail("003", "更新为驶出通道时，参数中outRecogCameraId字段内容不能为空！");
            }

            //检查选择的相机是否已经被其它通道绑定
            if (!vo.getOutRecogCameraId().equals(dbVal.getOutRecogCameraId())) {
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(vo.getOutRecogCameraId());
                int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
                if (ret > 0) {
                    return JsonResponse.fail("007", "选择的相机" + vo.getOutRecogCameraId() + "已绑定到其它通道，无法重复绑定！");
                }
            }

            vo.setInRecogCameraId(null);

            if (vo.getChannelType().equals(dbVal.getChannelType())){//如果原通道也是驶出通道
                if (!vo.getOutRecogCameraId().equals(dbVal.getOutRecogCameraId())){//如果相机更新了
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getOutRecogCameraId());
                    oldCam.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getOutRecogCameraId());
                    nowCam.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCam);
                }
            }else {//原通道不是驶出通道，可能是驶入通道或双向通道
                if (ProjConstants.CHANNEL_IN.equals(dbVal.getChannelType())){//原通道是驶入通道
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getInRecogCameraId());
                    oldCam.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getOutRecogCameraId());
                    nowCam.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCam);

                }else {//原通道是双向通道
                    ParkingDeviceVo oldCamOut = new ParkingDeviceVo();
                    oldCamOut.setId(dbVal.getOutRecogCameraId());
                    oldCamOut.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCamOut);
                    ParkingDeviceVo oldCamIn = new ParkingDeviceVo();
                    oldCamIn.setId(dbVal.getInRecogCameraId());
                    oldCamIn.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCamIn);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getOutRecogCameraId());
                    nowCam.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCam);

                }
            }
        }else if (ProjConstants.CHANNEL_INANDOUT.equals(vo.getChannelType())){
            if (StringUtils.isEmpty(vo.getInRecogCameraId())||StringUtils.isEmpty(vo.getOutRecogCameraId())){
                return JsonResponse.fail("004", "选择双向通道时，参数中inRecogCameraId, outRecogCameraId字段内容不能为空！");
            }
            if (vo.getInRecogCameraId().equals(vo.getOutRecogCameraId())){
                return JsonResponse.fail("008", "驶入识别和驶出识别不能相同！");
            }
            //检查选择的相机是否已经被其它通道绑定
            if (!vo.getInRecogCameraId().equals(dbVal.getInRecogCameraId())) {
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(vo.getInRecogCameraId());
                int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
                if (ret > 0) {
                    return JsonResponse.fail("007", "选择的相机" + vo.getInRecogCameraId() + "已绑定到其它通道，无法重复绑定！");
                }
            }
            //检查选择的相机是否已经被其它通道绑定
            if (!vo.getOutRecogCameraId().equals(dbVal.getOutRecogCameraId())) {
                ParkingDeviceVo deviceVo = new ParkingDeviceVo();
                deviceVo.setId(vo.getOutRecogCameraId());
                int ret = deviceDao.queryByCameraTypeAndId(deviceVo);
                if (ret > 0) {
                    return JsonResponse.fail("007", "选择的相机" + vo.getOutRecogCameraId() + "已绑定到其它通道，无法重复绑定！");
                }
            }

            if (vo.getChannelType().equals(dbVal.getChannelType())){//原通道也是双向通道
                if (!vo.getInRecogCameraId().equals(dbVal.getInRecogCameraId())){
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getInRecogCameraId());
                    oldCam.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getInRecogCameraId());
                    nowCam.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCam);
                }
                if (!vo.getOutRecogCameraId().equals(dbVal.getOutRecogCameraId())){
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getOutRecogCameraId());
                    oldCam.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCam = new ParkingDeviceVo();
                    nowCam.setId(vo.getOutRecogCameraId());
                    nowCam.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCam);
                }
            }else {//原通道不是双向通道，可能是驶入通道或驶出通道
                if (ProjConstants.CHANNEL_IN.equals(dbVal.getChannelType())){//原通道是驶入通道
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getInRecogCameraId());
                    oldCam.setCameraType("入口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCamIn = new ParkingDeviceVo();
                    nowCamIn.setId(vo.getInRecogCameraId());
                    nowCamIn.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCamIn);
                    ParkingDeviceVo nowCamOut = new ParkingDeviceVo();
                    nowCamOut.setId(vo.getOutRecogCameraId());
                    nowCamOut.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCamOut);

                }else {//原通道是驶出通道
                    ParkingDeviceVo oldCam = new ParkingDeviceVo();
                    oldCam.setId(dbVal.getOutRecogCameraId());
                    oldCam.setCameraType("出口相机");
                    toBeUpdatedOldCams.add(oldCam);

                    ParkingDeviceVo nowCamIn = new ParkingDeviceVo();
                    nowCamIn.setId(vo.getInRecogCameraId());
                    nowCamIn.setCameraType("入口相机");
                    toBeUpdatedNowCams.add(nowCamIn);
                    ParkingDeviceVo nowCamOut = new ParkingDeviceVo();
                    nowCamOut.setId(vo.getOutRecogCameraId());
                    nowCamOut.setCameraType("出口相机");
                    toBeUpdatedNowCams.add(nowCamOut);


                }
            }
        }else {
            return JsonResponse.fail("005", "channelType字段内容错误，只能是驶入通道、驶出通道或双向通道！");
        }

        //执行更新
        int rows = dao.updateById(vo);



        //执行更新后，如果更改了相机信息，取消订阅原相机事件并退出登录，将原相机的channelId，regionId，floorId，cameraType置空；
        // 如果新相机是已登录的，更新新相机的channelId，regionId，floorId，cameraType更新新相机事件对象的ledList
        //如果新相机没有登录，更新新相机的channelId，regionId，floorId，cameraType，登录新相机和订阅相机事件
        //更新相机所属区域、所属楼层信息

        //取消订阅原相机事件并退出登录，将原相机的channelId，regionId，floorId，cameraType置空
        for (ParkingDeviceVo oldCam : toBeUpdatedOldCams) {
            CameraCommunicationUtil.unsubscribeAndLogout(oldCam);
            deviceDao.updateBelongToIdsAndCameraTypeToNullById(oldCam);
        }



        for (ParkingDeviceVo cam : toBeUpdatedNowCams) {

            //更新新相机相关信息
            vo.setCameraId(cam.getId());
            vo.setCameraType(cam.getCameraType());
            deviceDao.updateCameraLocationInfo(vo);

            //更新相机事件对象相关信息

            cam = deviceDao.queryById(new ParkingDevicePo(cam.getId()));
            Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(cam.getId());
            if (map == null) {//新相机没有登录
                fAnalyzerDataCallBackImpl callBack = buildCallBack(cam, deviceDao, publishDao);
                int i = CameraCommunicationUtil.loginAndSubscribe(callBack);
                if (i < 1){
                    log.warn("更新通道相机时，相机登录失败！");
                }

            } else {//新相机是处于登录状态的
                ParkingDeviceVo p = new ParkingDeviceVo();
                p.setRegionId(cam.getRegionId());
                p.setDeviceType(ProjConstants.DEVICE_LED);
                p.setLedType(ProjConstants.LED_REGIONLED);
                List<ParkingDeviceVo> ledListOfCurCam = deviceDao.queryDevicesByBelongToRegionId(p);//查询该相机对应的所有区域余位屏，如果LED屏数据还没添加，该list长度为0
                ParkingPublishVo regionPublishVo = new ParkingPublishVo();
                regionPublishVo.setRegionId(cam.getRegionId());
                regionPublishVo.setPublishType(2);
                regionPublishVo = publishDao.queryByRegionIdAndPublishType(regionPublishVo);//获得当前相机所属区域的发布状态，在status字段中,如果发布数据不存在则返回null

                ParkingPublishVo floorPublishVo = new ParkingPublishVo();
                floorPublishVo.setFloorId(cam.getFloorId());
                floorPublishVo.setPublishType(1);
                floorPublishVo = publishDao.queryByFloorIdAndPublishType(floorPublishVo);//获得当前相机所属楼层的发布状态，在status字段中，如果发布数据不存在则返回null
                if (floorPublishVo != null) {
                    ParkingDeviceVo floorLed = deviceDao.queryById(new ParkingDevicePo(floorPublishVo.getLedPartId()));//查询该相机对应的楼层余位屏
                    if (null != floorLed) {
                        ledListOfCurCam.add(floorLed);//区域余位屏和楼层余位屏保存到一起
                    }
                }
                List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>();
                for (ParkingDeviceVo led : ledListOfCurCam) {
                    fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
                    tm.setId(led.getId());
                    tm.setLedIp(led.getDeviceIp());
                    tm.setPort(led.getDevicePort());
                    tm.setLedType(led.getLedType());
                    if (ProjConstants.LED_FLOORLED.equals(led.getLedType())) {
                        if (null == floorPublishVo) {
                            tm.setRownum(null);
                            tm.setPublishStatus(0);
                            tm.setPublishType(1);
                        } else {
                            tm.setRownum(floorPublishVo.getLedRownum());
                            tm.setPublishStatus(floorPublishVo.getStatus());
                            tm.setPublishType(floorPublishVo.getPublishType());
                        }
                    } else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())) {
                        tm.setRownum(1);
                        if (null == regionPublishVo) {
                            tm.setPublishStatus(0);
                            tm.setPublishType(2);
                        } else {
                            tm.setPublishStatus(regionPublishVo.getStatus());
                            tm.setPublishType(regionPublishVo.getPublishType());
                        }
                    }
                    ledList.add(tm);
                }
                fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                fAnalyzerDataCallBackImpl.Camera camera = callBack.getCamera();
                camera.setCameraType(cam.getCameraType());//如果用户更改了相机类型，相机事件对象的相机类型也要同时更改。(防止用户更新入口相机却选择了出口相机来赋值)
                callBack.setLeds(ledList);

            }
        }


        if (rows > 0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新失败！");
        }
    }

    @Override
    public JsonResponse queryById(ParkingChannelVo vo, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(vo));
        return JsonResponse.success(dao.queryById(vo));
    }


    public List<fAnalyzerDataCallBackImpl> buildCallBackList(List<ParkingDeviceVo> cameraList){
        List<fAnalyzerDataCallBackImpl> callBackList = new ArrayList<>();
        //2. 查询相机对应的所有LED屏
        for (ParkingDeviceVo cam : cameraList) {

            ParkingDeviceVo p = new ParkingDeviceVo();
            p.setRegionId(cam.getRegionId());
            p.setDeviceType(ProjConstants.DEVICE_LED);
            p.setLedType(ProjConstants.LED_REGIONLED);
            List<ParkingDeviceVo> ledListOfCurCam = deviceDao.queryDevicesByBelongToRegionId(p);//查询该相机对应的所有区域余位屏，如果LED屏数据还没添加，该list长度为0
            ParkingPublishVo regionPublishVo = new ParkingPublishVo();
            regionPublishVo.setRegionId(cam.getRegionId());
            regionPublishVo.setPublishType(2);
            regionPublishVo = publishDao.queryByRegionIdAndPublishType(regionPublishVo);//获得当前相机所属区域的发布状态，在status字段中,如果发布数据不存在则返回null

            ParkingPublishVo floorPublishVo = new ParkingPublishVo();
            floorPublishVo.setFloorId(cam.getFloorId());
            floorPublishVo.setPublishType(1);
            floorPublishVo = publishDao.queryByFloorIdAndPublishType(floorPublishVo);//获得当前相机所属楼层的发布状态，在status字段中，如果发布数据不存在则返回null
            if (floorPublishVo!=null) {
                ParkingDeviceVo floorLed = deviceDao.queryById(new ParkingDevicePo(floorPublishVo.getLedPartId()));//查询该相机对应的楼层余位屏
                if (null != floorLed) {
                    ledListOfCurCam.add(floorLed);//区域余位屏和楼层余位屏保存到一起
                }
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
            List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>();
            for (ParkingDeviceVo led: ledListOfCurCam) {//当相机先于区域余位屏添加且相机所属楼层未发布，该list长度为0
                fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
                tm.setId(led.getId());
                tm.setLedIp(led.getDeviceIp());
                tm.setPort(led.getDevicePort());
                tm.setLedType(led.getLedType());
                if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                    if (null == floorPublishVo){
                        tm.setRownum(null);
                        tm.setPublishStatus(0);
                        tm.setPublishType(1);
                    }else {
                        tm.setRownum(floorPublishVo.getLedRownum());
                        tm.setPublishStatus(floorPublishVo.getStatus());
                        tm.setPublishType(floorPublishVo.getPublishType());
                    }
                }else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                    tm.setRownum(1);
                    if (null == regionPublishVo){
                        tm.setPublishStatus(0);
                        tm.setPublishType(2);
                    }else {
                        tm.setPublishStatus(regionPublishVo.getStatus());
                        tm.setPublishType(regionPublishVo.getPublishType());
                    }
                }
                ledList.add(tm);
            }
            callBackList.add(new fAnalyzerDataCallBackImpl(camera, ledList));
        }
        return callBackList;
    }

    public static fAnalyzerDataCallBackImpl buildCallBack(ParkingDeviceVo cam, ParkingDeviceDao deviceDao, ParkingPublishDao publishDao){

        ParkingDeviceVo p = new ParkingDeviceVo();
        p.setRegionId(cam.getRegionId());
        p.setDeviceType(ProjConstants.DEVICE_LED);
        p.setLedType(ProjConstants.LED_REGIONLED);
        List<ParkingDeviceVo> ledListOfCurCam = deviceDao.queryDevicesByBelongToRegionId(p);//查询该相机对应的所有区域余位屏，如果LED屏数据还没添加，该list长度为0
        ParkingPublishVo regionPublishVo = new ParkingPublishVo();
        regionPublishVo.setRegionId((cam.getRegionId()));
        regionPublishVo.setPublishType(2);
        regionPublishVo = publishDao.queryByRegionIdAndPublishType(regionPublishVo);//获得当前相机所属区域的发布状态，在status字段中,如果发布数据不存在则返回null

        ParkingPublishVo floorPublishVo = new ParkingPublishVo();
        floorPublishVo.setFloorId(cam.getFloorId());
        floorPublishVo.setPublishType(1);
        floorPublishVo = publishDao.queryByFloorIdAndPublishType(floorPublishVo);//获得当前相机所属楼层的发布状态，在status字段中，如果发布数据不存在则返回null
        if (floorPublishVo!=null) {
            ParkingDeviceVo floorLed = deviceDao.queryById(new ParkingDevicePo(floorPublishVo.getLedPartId()));//查询该相机对应的楼层余位屏
            if (null != floorLed) {
                ledListOfCurCam.add(floorLed);//区域余位屏和楼层余位屏保存到一起
            }
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
        List<fAnalyzerDataCallBackImpl.Led> ledList = new ArrayList<>();
        for (ParkingDeviceVo led: ledListOfCurCam) {//当相机先于区域余位屏添加且相机所属楼层未发布，该list长度为0
            fAnalyzerDataCallBackImpl.Led tm = new fAnalyzerDataCallBackImpl.Led();
            tm.setId(led.getId());
            tm.setLedIp(led.getDeviceIp());
            tm.setPort(led.getDevicePort());
            tm.setLedType(led.getLedType());
            if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                if (null == floorPublishVo){
                    tm.setRownum(null);
                    tm.setPublishStatus(0);
                    tm.setPublishType(1);
                }else {
                    tm.setRownum(floorPublishVo.getLedRownum());
                    tm.setPublishStatus(floorPublishVo.getStatus());
                    tm.setPublishType(floorPublishVo.getPublishType());
                }
            }else if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){
                tm.setRownum(1);
                if (null == regionPublishVo){
                    tm.setPublishStatus(0);
                    tm.setPublishType(2);
                }else {
                    tm.setPublishStatus(regionPublishVo.getStatus());
                    tm.setPublishType(regionPublishVo.getPublishType());
                }
            }
            ledList.add(tm);
        }
        return new fAnalyzerDataCallBackImpl(camera, ledList);
    }


}
