package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.dao.ParkingFloorDao;
import com.drzk.parkingguide.dao.ParkingRegionDao;
import com.drzk.parkingguide.po.ParkingFloorPo;
import com.drzk.parkingguide.service.ParkingFloorService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.vo.ParkingFloorVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Service
public class ParkingFloorServiceImpl implements ParkingFloorService {

    private Logger log = LoggerFactory.getLogger(ParkingFloorServiceImpl.class);

    @Autowired
    private ParkingFloorDao parkingFloorDao;

    @Autowired
    private ParkingRegionDao regionDao;

    @Override
    public JsonResponse permissionInsert(ParkingFloorPo po, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"parkName", "floorName", "totalPlaces", "specificPlaces", "tempPlaces", "manager"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, po);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        if (po.getTotalPlaces() <= 0){
            return JsonResponse.fail("002", "总车位数必须大于0");
        }
        if (po.getTotalPlaces() < po.getSpecificPlaces() + po.getTempPlaces()){
            return JsonResponse.fail("003", "固定车位数与临时车位数之和不能大于总车位数");
        }
        po.setRemainPlaces(po.getTotalPlaces());
        int i = parkingFloorDao.insert(po);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse permissionUpdateById(ParkingFloorPo po, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"parkName", "floorName", "totalPlaces", "specificPlaces", "tempPlaces", "manager"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, po);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        if (po.getTotalPlaces() <= 0){
            return JsonResponse.fail("002", "总车位数必须大于0");
        }
        if (po.getTotalPlaces() < po.getSpecificPlaces() + po.getTempPlaces()){
            return JsonResponse.fail("003", "固定车位数与临时车位数之和不能大于总车位数");
        }
        int rows = parkingFloorDao.updateById(po);
        if (rows>0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新失败！");
        }
    }

    @Override
    public JsonResponse queryById(ParkingFloorVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        return JsonResponse.success(parkingFloorDao.queryById(vo));
    }

    @Override
    public JsonResponse permissionBatchInsert(List<ParkingFloorPo> list, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        int i = parkingFloorDao.batchInsert(list);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse queryParkingFloorPage(ParkingFloorVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingFloorVo> lst = parkingFloorDao.queryParkingFloorPage(vo);
        PageInfo<ParkingFloorVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    public JsonResponse queryByParkName(ParkingFloorVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingFloorVo> lst = parkingFloorDao.queryByParkName(vo);
        PageInfo<ParkingFloorVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    public JsonResponse queryByFloorName(ParkingFloorVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingFloorVo> lst = parkingFloorDao.queryByFloorName(vo);
        PageInfo<ParkingFloorVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    public JsonResponse permissionDeleteById(List<ParkingFloorPo> list, HttpServletRequest request) {
        for (ParkingFloorPo floorPo : list) {
            ParkingRegionVo regionVo = new ParkingRegionVo();
            regionVo.setFloorId(floorPo.getId());
            int count = regionDao.getCountByFloorId(regionVo);
            if (count > 0){
                return JsonResponse.fail("004", "该楼层下存在区域信息，请先删除区域信息！");
            }
        }
        int rows = parkingFloorDao.deleteByIdBatch(list);
        return JsonResponse.success(rows);
        /*if (rows > 0){
            return JsonResponse.success("成功删除"+rows+"条数据！");
        }else {
            return JsonResponse.fail("删除失败！");
        }*/
    }
}
