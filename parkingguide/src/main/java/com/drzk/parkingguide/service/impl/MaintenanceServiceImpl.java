package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import com.drzk.parkingguide.util.JsonResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class MaintenanceServiceImpl {

    public JsonResponse permissionViewCameraLoginHandle(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("loginHandles", CameraCommunicationUtil.m_loginMap);
        map.put("subscribeHandles", CameraCommunicationUtil.m_attachMap);
        //map.put("callbacks", CameraCommunicationUtil.callBackList);
        return JsonResponse.success(map);
    }

}
