package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.dao.ParkingSnapInfoDao;
import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.service.ParkingSnapInfoService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingSnapInfoVo;
import com.drzk.parkingguide.vo.StatisticsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ParkingSnapInfoServiceImpl implements ParkingSnapInfoService {

    private Logger log = LoggerFactory.getLogger(ParkingSnapInfoServiceImpl.class);

    @Autowired
    private ParkingSnapInfoDao dao;

    @Override
    public JsonResponse permissionAddParkingSnapInfo(ParkingSnapInfoPo po, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        int i = dao.insert(po);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse queryParkingSnapInfoPage(ParkingSnapInfoVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingSnapInfoVo> lst = dao.queryAllWithPage(vo);
        PageInfo<ParkingSnapInfoVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    public JsonResponse permissionDeleteById(List<ParkingSnapInfoPo> list, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        int rows = dao.deleteByIdBatch(list);
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse permissionUpdateById(ParkingSnapInfoPo po, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        int rows = dao.updateById(po);
        /*if (rows > 0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail("更新失败！");
        }*/
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse queryById(ParkingSnapInfoPo po) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        return JsonResponse.success(dao.queryById(po));
    }

    @Override
    public List<StatisticsVo> regionVehicleFlowStatistics(StatisticsVo regionId) {
        regionId.setCameraType("入口相机");
        return dao.regionVehicleFlowStatistics(regionId);
    }
}
