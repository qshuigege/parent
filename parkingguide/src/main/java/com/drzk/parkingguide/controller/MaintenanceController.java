package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.dao.ParkingPublishDao;
import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.service.impl.MaintenanceServiceImpl;
import com.drzk.parkingguide.service.impl.ParkingChannelServiceImpl;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private ParkingDeviceDao deviceDao;

    @Autowired
    private ParkingPublishDao publishDao;

    @Autowired
    private MaintenanceServiceImpl service;

    @RequestMapping("/viewCameraLoginHandle")
    public JsonResponse viewCameraLoginHandle(HttpServletRequest request){
        return service.permissionViewCameraLoginHandle(request);
    }

    @RequestMapping("/getRelevantLedsOfCamera")
    public JsonResponse getRelevantLedsOfCamera(@RequestBody ParkingDeviceVo camera, HttpServletRequest request){
        camera = deviceDao.queryById(new ParkingDevicePo(camera.getId()));
        if (camera == null){
            return JsonResponse.fail("", "id不存在");
        }
        if (StringUtils.isEmpty(camera.getCameraType())){
            return JsonResponse.fail("", "相机未绑定到通道");
        }
        fAnalyzerDataCallBackImpl callBack = ParkingChannelServiceImpl.buildCallBack(camera, deviceDao, publishDao);
        return JsonResponse.success(callBack);
    }

}
