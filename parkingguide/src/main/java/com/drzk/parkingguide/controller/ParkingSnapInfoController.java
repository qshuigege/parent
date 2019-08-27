package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.service.ParkingSnapInfoService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingSnapInfoVo;
import com.drzk.parkingguide.vo.StatisticsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/parkingSnapInfo")
public class ParkingSnapInfoController {

    @Autowired
    private ParkingSnapInfoService service;

    private Logger log = LoggerFactory.getLogger(ParkingSnapInfoController.class);

    @RequestMapping(value = "/queryParkingSnapInfoPage", method = RequestMethod.POST)
    public JsonResponse queryParkingSnapInfoPage(@RequestBody ParkingSnapInfoVo vo){
        return service.queryParkingSnapInfoPage(vo);
    }

    @RequestMapping(value = "/addParkingSnapInfo", method = RequestMethod.POST)
    public JsonResponse addParkingSnapInfo(@RequestBody ParkingSnapInfoPo po, HttpServletRequest request){
        return service.permissionAddParkingSnapInfo(po, request);
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingSnapInfoPo po){
        return service.queryById(po);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingSnapInfoPo> list, HttpServletRequest request){
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingSnapInfoPo po, HttpServletRequest request){
        return service.permissionUpdateById(po, request);
    }

    @RequestMapping(value = "/regionVehicleFlowStatistics", method = RequestMethod.POST)
    public JsonResponse regionVehicleFlowStatistics(@RequestBody StatisticsVo regionId){
        log.info("请求参数-->{}", JsonUtils.objectToJson(regionId));
        if (regionId.getRegionId()==null||"".equals(regionId.getRegionId())){
            return JsonResponse.fail(JsonResponse.SYS_ERR, "参数regionId不能为空！");
        }
        List<StatisticsVo> list = service.regionVehicleFlowStatistics(regionId);
        return JsonResponse.success(list);
    }

}
