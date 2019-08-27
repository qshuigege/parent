package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingSettingsPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingSettingsVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingSettingsService {
    List<ParkingSettingsVo> queryParkingSettingsPage(ParkingSettingsVo vo);

    JsonResponse permissionAddParkingSettings(ParkingSettingsPo po, HttpServletRequest request) throws Exception;

    JsonResponse queryById(ParkingSettingsPo po);

    JsonResponse permissionDeleteById(List<ParkingSettingsPo> list, HttpServletRequest request);

    JsonResponse permissionUpdateById(ParkingSettingsPo po, HttpServletRequest request);

    JsonResponse permissionUpdateByOptionKey(ParkingSettingsVo vo, HttpServletRequest request) throws Exception;
}
