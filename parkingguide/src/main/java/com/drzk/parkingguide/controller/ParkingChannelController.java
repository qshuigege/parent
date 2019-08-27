package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingChannelPo;
import com.drzk.parkingguide.service.ParkingChannelService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingChannelVo;
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
@RequestMapping("/parkingChannel")
public class ParkingChannelController {

    private Logger log = LoggerFactory.getLogger(ParkingChannelController.class);

    @Autowired
    private ParkingChannelService service;

    @RequestMapping(value = "/queryParkingChannelPage", method = RequestMethod.POST)
    public JsonResponse queryParkingChannelPage(@RequestBody ParkingChannelVo vo){
        return service.queryParkingChannelPage(vo);
    }

    @RequestMapping(value = "/addParkingChannel", method = RequestMethod.POST)
    public JsonResponse addParkingChannel(@RequestBody ParkingChannelVo vo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(vo));
        try {
            return service.permissionAddParkingChannel(vo, request);
        } catch (Exception e) {
            log.error("添加通道信息失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "添加通道信息失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingChannelVo vo, HttpServletRequest request){
        return service.queryById(vo, request);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingChannelVo> list, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingChannelVo vo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(vo));
        try {
            return service.permissionUpdateById(vo, request);
        } catch (Exception e) {
            log.error("更新通道信息失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新通道信息失败！-->"+e.getMessage());
        }
    }

}
