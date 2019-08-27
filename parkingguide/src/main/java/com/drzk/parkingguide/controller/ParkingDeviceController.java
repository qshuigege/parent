package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.service.ParkingDeviceService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
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
@RequestMapping("/parkingDevice")
public class ParkingDeviceController {

    private Logger log = LoggerFactory.getLogger(ParkingDeviceController.class);

    @Autowired
    private ParkingDeviceService service;

    @RequestMapping(value = "/queryParkingDevicePage", method = RequestMethod.POST)
    public JsonResponse queryParkingDevicePage(@RequestBody ParkingDeviceVo vo){
        return service.queryParkingDevicePage(vo);
    }

    @RequestMapping(value = "/addParkingDevice", method = RequestMethod.POST)
    public JsonResponse addParkingDevice(@RequestBody ParkingDeviceVo deviceVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(deviceVo));
        try {
            return service.permissionAddParkingDevice(deviceVo, request);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingDevicePo po, HttpServletRequest request){
        return service.queryById(po, request);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingDevicePo> list, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingDeviceVo deviceVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(deviceVo));
        try {
            return service.permissionUpdateById(deviceVo, request);
        } catch (Exception e) {
            log.error("系统异常，更新失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，更新失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/queryUsableCamerasOfChannel", method = RequestMethod.POST)
    public JsonResponse queryUsableCamerasOfChannel(@RequestBody ParkingDeviceVo deviceVo){
        log.info("请求参数-->{}", JsonUtils.objectToJson(deviceVo));
        return JsonResponse.success(service.queryUsableCamerasOfChannel(deviceVo));
    }


    @RequestMapping(value = "/changeCameraLoginState", method = RequestMethod.POST)
    public JsonResponse changeCameraLoginState(@RequestBody ParkingDeviceVo deviceVo){
        log.info("请求参数-->{}", JsonUtils.objectToJson(deviceVo));
        try {
            return service.changeCameraLoginState(deviceVo);
        }catch (Exception e){
            log.error("系统异常-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常-->"+e.getMessage());
        }
    }




}
