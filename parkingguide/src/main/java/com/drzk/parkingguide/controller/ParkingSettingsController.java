package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingSettingsPo;
import com.drzk.parkingguide.service.ParkingSettingsService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingSettingsVo;
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
@RequestMapping("/parkingSettings")
public class ParkingSettingsController {

    private Logger log = LoggerFactory.getLogger(ParkingSettingsController.class);

    @Autowired
    private ParkingSettingsService service;

    @RequestMapping(value = "/queryParkingSettingsPage", method = RequestMethod.POST)
    public JsonResponse queryParkingSettingsPage(@RequestBody ParkingSettingsVo vo){
        List<ParkingSettingsVo> lst = service.queryParkingSettingsPage(vo);
        PageInfo<ParkingSettingsVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @RequestMapping(value = "/addParkingSettings", method = RequestMethod.POST)
    public JsonResponse addParkingSettings(@RequestBody ParkingSettingsPo po, HttpServletRequest request){
        try {
            return service.permissionAddParkingSettings(po, request);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingSettingsPo po){
        return service.queryById(po);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingSettingsPo> list, HttpServletRequest request){
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingSettingsPo po, HttpServletRequest request){
        return service.permissionUpdateById(po, request);
    }

    @RequestMapping(value = "/updateByOptionKey", method = RequestMethod.POST)
    public JsonResponse updateByOptionKey(@RequestBody ParkingSettingsVo vo, HttpServletRequest request){
        try {
            return service.permissionUpdateByOptionKey(vo, request);
        }catch (Exception e){
            log.error("系统异常，更新失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，更新失败！-->"+e.getMessage());
        }
    }

}
