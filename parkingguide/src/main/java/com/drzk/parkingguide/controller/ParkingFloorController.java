package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingFloorPo;
import com.drzk.parkingguide.service.ParkingFloorService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingFloorVo;
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
@RequestMapping("/parkingFloor")
public class ParkingFloorController {

    private Logger log = LoggerFactory.getLogger(ParkingFloorController.class);

    @Autowired
    private ParkingFloorService service;

    @RequestMapping(value = "/queryParkingFloorPage", method = RequestMethod.POST)
    public JsonResponse queryParkingFloorPage(@RequestBody ParkingFloorVo vo){
        if (vo.getFloorName()!=null&&!"".equals(vo.getFloorName())){
            return service.queryByFloorName(vo);
        }else if (vo.getParkName()!=null&&!"".equals(vo.getParkName())){
            return service.queryByParkName(vo);
        }else {
            return service.queryParkingFloorPage(vo);
        }
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingFloorVo vo){
        return service.queryById(vo);
    }

    @RequestMapping(value = "/queryByParkName", method = RequestMethod.POST)
    public JsonResponse queryByParkName(@RequestBody ParkingFloorVo vo){
        return service.queryByParkName(vo);
    }

    @RequestMapping(value = "/queryByFloorName", method = RequestMethod.POST)
    public JsonResponse queryByFloorName(@RequestBody ParkingFloorVo vo){
        return service.queryByFloorName(vo);
    }

    @RequestMapping(value = "/addParkingFloor", method = RequestMethod.POST)
    public JsonResponse addParkingFloor(@RequestBody ParkingFloorPo po, HttpServletRequest request){
        try {
            return service.permissionInsert(po, request);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/addParkingFloorBatch", method = RequestMethod.POST)
    public JsonResponse addParkingFloorBatch(@RequestBody List<ParkingFloorPo> list, HttpServletRequest request){
        return service.permissionBatchInsert(list, request);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingFloorPo> list, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingFloorPo po, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        try {
            return service.permissionUpdateById(po, request);
        }catch (Exception e){
            log.error("系统异常，更新失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，更新失败！-->"+e.getMessage());
        }
    }

}
