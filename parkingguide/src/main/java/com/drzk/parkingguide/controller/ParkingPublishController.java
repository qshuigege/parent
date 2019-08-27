package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.service.ParkingPublishService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("/parkingPublish")
public class ParkingPublishController {

    private Logger log = LoggerFactory.getLogger(ParkingPublishController.class);

    @Autowired
    private ParkingPublishService service;

    @RequestMapping(value = "/getAllPublishedInfo", method = RequestMethod.POST)
    public JsonResponse getAllPublishedInfo(@RequestBody ParkingPublishVo publishVo){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        List<ParkingPublishVo> list = service.getAllPublishedInfo(publishVo);
        return JsonResponse.success(new PageInfo<>(list));
    }

    @RequestMapping(value = "/addParkingPublish", method = RequestMethod.POST)
    public JsonResponse addParkingPublish(@RequestBody ParkingPublishVo publishVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        try {
            return service.permissionAddParkingPublish(publishVo, request);
        } catch (Exception e) {
            log.error("程序异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "程序异常，添加失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/delParkingPublish", method = RequestMethod.POST)
    public JsonResponse delParkingPublish(@RequestBody ParkingPublishVo publishVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        try {
            return service.permissionDelParkingPublish(publishVo, request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("程序异常，删除失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "程序异常，删除失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/doPublish", method = RequestMethod.POST)
    public JsonResponse doPublish(@RequestBody ParkingPublishVo publishVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        try {
            return service.permissionDoPublish(publishVo, request);
        } catch (Exception e) {
            log.error("程序异常，发布失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "程序异常，发布失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/updateParkingPublish", method = RequestMethod.POST)
    public JsonResponse updateParkingPublish(@RequestBody ParkingPublishVo publishVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        try {
            return service.permissionUpdateParkingPublish(publishVo, request);
        } catch (Exception e) {
            log.error("程序异常，更新失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "程序异常，更新失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/correctRemainPlaces", method = RequestMethod.POST)
    public JsonResponse correctRemainPlaces(@RequestBody ParkingPublishVo publishVo, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(publishVo));
        try {
            return service.permissionCorrectRemainPlaces(publishVo, request);
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，操作失败！-->"+e.getMessage());
        }
    }
}
