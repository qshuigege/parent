package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import com.drzk.parkingguide.camera.LEDCommunicationUtil;
import com.drzk.parkingguide.camera.NetSDKLib;
import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.dao.ParkingFloorDao;
import com.drzk.parkingguide.dao.ParkingPublishDao;
import com.drzk.parkingguide.dao.ParkingRegionDao;
import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.service.ParkingPublishService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.util.ProjConstants;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingFloorVo;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ParkingPublishServiceImpl implements ParkingPublishService {

    private Logger log = LoggerFactory.getLogger(ParkingPublishServiceImpl.class);

    @Autowired
    private ParkingPublishDao dao;

    @Autowired
    private ParkingRegionDao regionDao;

    @Autowired
    private ParkingDeviceDao deviceDao;

    @Autowired
    private ParkingFloorDao floorDao;

    @Autowired
    private ParkingPublishDao publishDao;

    @Override
    public List<ParkingPublishVo> getAllPublishedInfo(ParkingPublishVo publishVo) {
        PageHelper.startPage(publishVo.getPageNum(), publishVo.getPageSize());
        return dao.queryAllWithPage(publishVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse permissionAddParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception{

        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"publishType"}, publishVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
        }
        int insert;
        if (publishVo.getPublishType() ==1){
            /*if (null==publishVo.getFloorId()||"".equals(publishVo.getFloorId())){
                return JsonResponse.fail("添加楼层发布时，参数中floorId的值不能为空！");
            }*/
            ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(new String[]{"ledPartId", "floorId", "ledRownum"}, publishVo);
            if (!validationResult.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
            }
            int rows = dao.isFloorIdExist(publishVo);
            if (rows>0){
                return JsonResponse.fail("002", "该楼层的发布信息已经存在，无法重复添加！");
            }
            //判断用户选择的LED屏的类型是否正确
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setId(publishVo.getLedPartId());
            deviceVo.setLedType(ProjConstants.LED_FLOORLED);
            deviceVo = deviceDao.queryByIdAndOtherCols(deviceVo);
            if (deviceVo == null){
                return JsonResponse.fail("003", "添加楼层发布只能选择楼层余位屏！");
            }

            //添加楼层发布需要将选择的LED屏（和行号）添加到该楼层下所有相机的事件对象的ledList中
            deviceVo = new ParkingDeviceVo();
            deviceVo.setFloorId(publishVo.getFloorId());
            deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
            List<ParkingDeviceVo> cameraVoList = deviceDao.queryDevicesByBelongToFloorId(deviceVo);
            for (ParkingDeviceVo camVo : cameraVoList) {
                Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的
                    fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                    List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                    boolean f = true;//如果相机的事件对象的LedList中不存在该LED屏，才添加
                    for (fAnalyzerDataCallBackImpl.Led led : leds) {
                        if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){
                            if (publishVo.getLedPartId().equals(led.getId())){//已存在。（存在则更新，不存在则添加）
                                f = false;
                                ParkingDevicePo devicePo = new ParkingDevicePo(publishVo.getLedPartId());
                                ParkingDeviceVo floorLed = deviceDao.queryById(devicePo);
                                led.setId(publishVo.getLedPartId());
                                led.setPublishType(1);
                                led.setPublishStatus(1);
                                led.setRownum(publishVo.getLedRownum());
                                led.setLedIp(floorLed.getDeviceIp());
                                led.setPort(floorLed.getDevicePort());
                                led.setLedType(floorLed.getLedType());
                                break;
                            }
                        }
                    }
                    if (f){
                        //根据ledPartId查询到LED屏信息
                        ParkingDevicePo devicePo = new ParkingDevicePo(publishVo.getLedPartId());
                        ParkingDeviceVo floorLed = deviceDao.queryById(devicePo);
                        fAnalyzerDataCallBackImpl.Led led = new fAnalyzerDataCallBackImpl.Led();
                        led.setId(publishVo.getLedPartId());
                        led.setPublishType(1);
                        led.setPublishStatus(1);
                        led.setRownum(publishVo.getLedRownum());
                        led.setLedIp(floorLed.getDeviceIp());
                        led.setPort(floorLed.getDevicePort());
                        led.setLedType(floorLed.getLedType());

                        leds.add(led);
                    }
                }
            }

            publishVo.setRegionId(null);

        }else if (publishVo.getPublishType() ==2){
            /*if (null==publishVo.getRegionId()||"".equals(publishVo.getRegionId())){
                return JsonResponse.fail("添加区域发布时，参数中regionId的值不能为空！");
            }*/
            ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(new String[]{"regionId"/*, "ledPartId"*/}, publishVo);
            if (!validationResult.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
            }
            int rows = dao.isRegionIdExist(publishVo);
            if (rows>0){
                return JsonResponse.fail("004", "该区域的发布信息已经存在，无法重复添加！");
            }

        }else {
            return JsonResponse.fail("006", "参数中publishType字段值("+publishVo.getPublishType()+")错误，只能是1或2！");
        }

        publishVo.setId(UUID.randomUUID().toString().toLowerCase());
        insert = dao.insert(publishVo);//添加发布信息

        //添加后同时发布
        publishVo.setOperationType(1);
        permissionDoPublish(publishVo, request);
        if (insert>0){
            return JsonResponse.success("成功添加"+insert+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "添加数据失败！");
        }
    }

    /**
     * 删除发布信息
     * @param publishVo
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public JsonResponse permissionDelParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception{
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"publishType", "id"}, publishVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
        }

        if (publishVo.getPublishType() == 1){//如果是删除楼层发布，删除发布数据，更改相机事件ledList的发布状态即可
            if (StringUtils.isEmpty(publishVo.getFloorId())){
                return JsonResponse.fail("001", "floorId参数为空！");
            }


        }else if (publishVo.getPublishType() == 2){//如果是删除区域发布
            if (StringUtils.isEmpty(publishVo.getRegionId())){
                return JsonResponse.fail("001", "regionId参数为空！");
            }




        }else {
            return JsonResponse.fail("006", "参数中publishType字段值("+publishVo.getPublishType()+")错误，只能是1或2！");
        }
        publishVo.setOperationType(0);
        JsonResponse jsonResponse = permissionDoPublish(publishVo, request);//删除之前取消发布
        if (jsonResponse.getResult() == 0) {
            return jsonResponse;
        }

        ArrayList<ParkingPublishVo> list = new ArrayList<>();
        list.add(publishVo);
        int rows = dao.deleteByIdBatch(list);
        if (rows > 0) {
            return JsonResponse.success("成功删除"+rows+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "删除失败！");
        }
    }

    /**
     * 发布和取消发布
     * 参数中operationType值为1则发布，为0则取消发布
     * @param publishVo
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public JsonResponse permissionDoPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception{
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "publishType", "operationType"}, publishVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
        }

        if (publishVo.getPublishType()==1){//更新楼层发布状态
            if (null==publishVo.getFloorId()||"".equals(publishVo.getFloorId())){
                return JsonResponse.fail("001", "floorId参数为空！");
            }
            //1.更改相机的推送
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setFloorId(publishVo.getFloorId());
            deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
            List<ParkingDeviceVo> cameraVoList = deviceDao.queryDevicesByBelongToFloorId(deviceVo);
            for (ParkingDeviceVo camVo : cameraVoList) {
                Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                if (map!=null){//如果map为空，说明该相机是用户新增的或者登陆失败的，需要登录该相机
                    fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                    List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                    for (fAnalyzerDataCallBackImpl.Led led : leds) {
                        if (ProjConstants.LED_FLOORLED.equals(led.getLedType())){//因为是取消楼层屏的发布，只停止相机往楼层屏推送余位数据
                            if (publishVo.getOperationType()==1) {//发布
                                led.setPublishStatus(1);
                            }else if (publishVo.getOperationType()==0){//取消发布
                                led.setPublishStatus(0);
                            }else {
                                return JsonResponse.fail("007", "参数operationType值("+publishVo.getOperationType()+")错误！只能是0或1。");
                            }
                        }
                    }
                }
            }

            //2.更改发布状态
            publishVo.setStatus(publishVo.getOperationType());
            dao.updatePublishStatus(publishVo);

        }else if (publishVo.getPublishType()==2){//更新区域发布状态
            if (null==publishVo.getRegionId()||"".equals(publishVo.getRegionId())){
                return JsonResponse.fail("001", "regionId参数为空！");
            }
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setRegionId(publishVo.getRegionId());
            deviceVo.setDeviceType(ProjConstants.DEVICE_CAMERA);
            List<ParkingDeviceVo> cameraVoList = deviceDao.queryDevicesByBelongToRegionId(deviceVo);
            for (ParkingDeviceVo camVo : cameraVoList) {
                Map<NetSDKLib.LLong, fAnalyzerDataCallBackImpl> map = CameraCommunicationUtil.m_loginMap.get(camVo.getId());
                if (map!=null){//如果map为空，说明该相机是用户新增的或是登陆失败的，需要登录该相机
                    fAnalyzerDataCallBackImpl callBack = map.values().iterator().next();
                    List<fAnalyzerDataCallBackImpl.Led> leds = callBack.getLeds();
                    for (fAnalyzerDataCallBackImpl.Led led : leds) {
                        if (ProjConstants.LED_REGIONLED.equals(led.getLedType())){//因为是取消区域屏的发布，只停止相机往区域屏推送余位数据
                            if (publishVo.getOperationType()==1) {//发布
                                led.setPublishStatus(1);
                            }else if (publishVo.getOperationType()==0){//取消发布
                                led.setPublishStatus(0);
                            }else {
                                return JsonResponse.fail("007", "参数operationType值("+publishVo.getOperationType()+")错误！只能是0或1。");
                            }
                        }
                    }
                }
            }
            publishVo.setStatus(publishVo.getOperationType());
            dao.updatePublishStatus(publishVo);
        }else {
            return JsonResponse.fail("006", "参数中publishType字段值("+publishVo.getPublishType()+")错误，只能是1或2！");
        }
        return JsonResponse.success("操作已执行！");
    }

    @Override
    public JsonResponse permissionUpdateParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception {
        int rows;
        if (publishVo.getPublishType() == 1) {//更新楼层发布信息
            ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "floorId", "ledPartId", "ledRownum"}, publishVo);
            if (!validate.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
            }
            publishVo.setRegionId(null);
            rows = dao.updateFloorPublishInfo(publishVo);
        }else if (publishVo.getPublishType() == 2){//更新区域发布信息
            ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "floorId", "regionId", "ledPartId"}, publishVo);
            if (!validate.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
            }
            publishVo.setLedRownum(null);
            rows = dao.updateRegionPublishInfo(publishVo);
        }else {
            return JsonResponse.fail("006", "参数中publishType字段值("+publishVo.getPublishType()+")错误，只能是1或2！");
        }
        if (rows>0){
            return JsonResponse.success("已更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新失败！");
        }
    }

    /**
     * 用户手动更新剩余车位数和更新LED屏幕显示
     * @param publishVo
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public JsonResponse permissionCorrectRemainPlaces(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception{
        String retVal = "success";
        ParameterValidationUtils.ValidationResult validationResult = ParameterValidationUtils.validate(new String[]{"publishType"}, publishVo);
        if (!validationResult.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResult)+"字段值不能为空！");
        }
        if (publishVo.getPublishType()==1){
            ParameterValidationUtils.ValidationResult validationResultFloor = ParameterValidationUtils.validate(new String[]{"floorId", "ledRownum", "floorRemainPlaces"}, publishVo);
            if (!validationResultFloor.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResultFloor)+"字段值不能为空！");
            }
            ParkingFloorVo floorVo = new ParkingFloorVo();
            floorVo.setId(publishVo.getFloorId());
            floorVo.setRemainPlaces(publishVo.getFloorRemainPlaces());
            floorDao.updateRemainPlaces(floorVo);//更新楼层表剩余车位数
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setFloorId(publishVo.getFloorId());
            deviceVo.setDeviceType(ProjConstants.DEVICE_LED);
            deviceVo.setLedType(ProjConstants.LED_FLOORLED);
            //问题：允许用户更新楼层余位，同时也必须让用户更新区域余位？否则余位数乱套
            //解决：不允许用户更新楼层余位，只让用户更新区域余位，楼层余位随区域余位自动更新
            //List<ParkingDeviceVo> ledVoList = deviceDao.queryDevicesByBelongToFloorId(deviceVo);//错误，设备表中楼层余位屏并没有floorId，需要先从发布表中查到楼层屏的id
            ParkingPublishVo aaa = publishDao.queryByFloorIdAndPublishType(publishVo);
            ParkingDeviceVo led = deviceDao.queryById(new ParkingDevicePo(aaa.getLedPartId()));
            try {
                LEDCommunicationUtil.setLedParkingPlaceNum17Bytes(0, publishVo.getLedRownum(), publishVo.getFloorRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
            }catch (Exception e){
                log.error("往楼层屏({})推送余位数失败！-->{}",led.getDeviceIp(), e.getMessage());
                retVal = "往楼层屏("+led.getDeviceIp()+")推送余位数失败！-->"+e.getMessage();
            }
            /*for (ParkingDeviceVo led : ledVoList) {
                LEDCommunicationUtil.setLedParkingPlaceNum17Bytes(0, led.getLedRownum(), publishVo.getFloorRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
            }*/
        }else if (publishVo.getPublishType()==2){
            ParameterValidationUtils.ValidationResult validationResultRegion = ParameterValidationUtils.validate(new String[]{"regionId", "regionRemainPlaces"}, publishVo);
            if (!validationResultRegion.isSuccess()){
                return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validationResultRegion)+"字段值不能为空！");
            }
            ParkingRegionVo regionVo = new ParkingRegionVo();
            regionVo.setId(publishVo.getRegionId());
            regionVo.setRemainPlaces(publishVo.getRegionRemainPlaces());
            regionDao.updateRemainPlaces(regionVo);//更新区域表剩余车位数
            ParkingDeviceVo deviceVo = new ParkingDeviceVo();
            deviceVo.setRegionId(publishVo.getRegionId());
            deviceVo.setDeviceType(ProjConstants.DEVICE_LED);
            deviceVo.setLedType(ProjConstants.LED_REGIONLED);
            List<ParkingDeviceVo> ledVoList = deviceDao.queryDevicesByBelongToRegionId(deviceVo);
            for (ParkingDeviceVo led : ledVoList) {
                try {
                    LEDCommunicationUtil.setLedParkingPlaceNum(1, publishVo.getRegionRemainPlaces(), led.getDeviceIp(), led.getDevicePort());
                }catch (Exception e){
                    log.error("往区域屏({})推送余位数失败！-->{}",led.getDeviceIp(), e.getMessage());
                    retVal = "往区域屏("+led.getDeviceIp()+")推送余位数失败！-->"+e.getMessage();
                }
            }
        }else {
            return JsonResponse.fail("006", "参数中publishType字段值("+publishVo.getOperationType()+")错误，只能是1或2！");
        }
        return JsonResponse.success(retVal);
    }
}
