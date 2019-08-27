package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingRegionPo;
import com.drzk.parkingguide.service.ParkingRegionService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/parkingRegion")
public class ParkingRegionController {

    private Logger log = LoggerFactory.getLogger(ParkingRegionController.class);

    @Autowired
    private ParkingRegionService service;

    @RequestMapping(value = "/queryParkingRegionPage", method = RequestMethod.POST)
    public JsonResponse queryParkingRegionPage(@RequestBody ParkingRegionVo vo){
        return service.queryParkingRegionPage(vo);
    }

    @RequestMapping(value = "/addParkingRegion", method = RequestMethod.POST)
    public JsonResponse addParkingRegion(@RequestBody ParkingRegionPo po, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        try {
            return service.permissionAddParkingRegion(po, request);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    /*@RequestMapping(value = "/addParkingRegionBatch", method = RequestMethod.POST)
    public JsonResponse addParkingRegionBatch(@RequestBody List<ParkingRegionPo> list, HttpServletRequest request){
        return service.permissionBatchInsert(list, request);
    }*/

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public JsonResponse queryById(@RequestBody ParkingRegionPo po, HttpServletRequest request){
        return service.queryById(po, request);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public JsonResponse deleteById(@RequestBody List<ParkingRegionPo> list, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        return service.permissionDeleteById(list, request);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public JsonResponse updateById(@RequestBody ParkingRegionPo po, HttpServletRequest request){
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        try {
            return service.permissionUpdateById(po, request);
        } catch (Exception e) {
            log.error("系统异常，更新失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，更新失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/queryRegionsByBelongToFloorId", method = RequestMethod.POST)
    public JsonResponse queryRegionsByBelongToFloorId(@RequestBody ParkingRegionVo regionVo){
        return service.queryRegionsByBelongToFloorId(regionVo);
    }

    @RequestMapping(value = "/uploadRegionMap", method = RequestMethod.POST)
    public JsonResponse uploadRegionMap(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        try {
            return service.permissionUploadRegionMap(file, request);
            //return JsonResponse.success("上传区域地图成功！");
        } catch (IOException e) {
            log.error("上传区域地图异常！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "上传区域地图异常！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/updateRegionMapByRegionId", method = RequestMethod.POST)
    public JsonResponse updateRegionMapByRegionId(@RequestBody ParkingRegionVo regionVo, HttpServletRequest request){
        try {
            return service.permissionUpdateRegionMapByRegionId(regionVo, request);
        } catch (Exception e) {
            log.error("更新区域地图异常！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新区域地图异常！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/downloadRegionMap")
    public void downloadRegionMap(@RequestParam("fileId") String fileId, HttpServletRequest request, HttpServletResponse response){
        try {
            service.permissionDownloadRegionMap(fileId, request, response);
        }catch (Exception e){
            log.error("服务器异常，下载地图失败！-->{}", e.getMessage());
            //return JsonResponse.fail("服务器异常，下载地图失败！-->"+e.getMessage());
        }
    }



}
