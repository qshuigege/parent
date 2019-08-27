package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingCameraLedRelationPo;
import com.drzk.parkingguide.service.ParkingBusinessService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingBusinessVo;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingFloorVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/parkingBusiness")
public class ParkingBusinessController {

    @Autowired
    private ParkingBusinessService service;

    @RequestMapping(value = "/publish.bak", method = RequestMethod.POST)
    public JsonResponse publish(HttpServletRequest request, @RequestBody List<ParkingCameraLedRelationPo> simpleRelations){
        try {
            //service.permissionPublish(simpleRelations, request);
            return JsonResponse.success("发布成功！");
        }catch (Exception e) {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "发布失败-->"+e.getMessage());
        }
    }

    /**
     * 根据区域id发布该区域下所有相机
     * @param request
     * @param regionId
     * @return
     */
    @RequestMapping(value = "/publishRegion", method = RequestMethod.POST)
    public JsonResponse publishRegion(HttpServletRequest request, @RequestBody ParkingDeviceVo regionId){
        try{
            String s = service.permissionPublishRegion(regionId, request);
            String[] split = s.split("\\|");
            if ("success".equals(split[0])){
                return JsonResponse.success(split[1]);
            }else {
                return JsonResponse.fail(JsonResponse.SYS_ERR, split[1]);
            }
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    /**
     * 根据楼层id发布该楼层下所有区域所有摄像机
     * @param request
     * @param floorId
     * @return
     */
    @RequestMapping(value = "/publishFloor", method = RequestMethod.POST)
    public JsonResponse publishFloor(HttpServletRequest request, @RequestBody ParkingRegionVo floorId){
        try {
            List<String> strings = service.permissionPublishFloor(floorId, request);
            if ("success".equals(strings.get(0))){
                strings.remove(0);
                return JsonResponse.success(strings);
            }else {
                strings.remove(0);
                return JsonResponse.fail(JsonResponse.SYS_ERR, strings);
            }
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    /**
     * 根据区域id停止发布该区域下所有摄像机
     * @param request
     * @param regionId
     * @return
     */
    @RequestMapping(value = "/stopPublishRegion", method = RequestMethod.POST)
    public JsonResponse stopPublishRegion(HttpServletRequest request, @RequestBody ParkingDeviceVo regionId){
        return JsonResponse.success(service.permissionStopPublishRegion(regionId, request));
    }

    /**
     * 根据楼层id停止发布该楼层下所有区域所有摄像机
     * @param request
     * @param floorId
     * @return
     */
    @RequestMapping(value = "/stopPublishFloor", method = RequestMethod.POST)
    public JsonResponse stopPublishFloor(HttpServletRequest request, @RequestBody ParkingRegionVo floorId){
        return JsonResponse.success(service.permissionStopPublishFloor(floorId, request));
    }

    /**
     * 查询所有已发布或未发布区域信息
     * 可附加条件字段：publishState
     * @param regionVo
     * @return
     */
    @RequestMapping(value = "/getRegions", method = RequestMethod.POST)
    public JsonResponse getRegions(@RequestBody ParkingRegionVo regionVo){
        List<ParkingRegionVo> regions = service.getRegions(regionVo);
        return JsonResponse.success(regions);
    }

    /**
     * 查询所有已发布或未发布楼层信息
     * 可附加条件字段：publishState
     * @param floorVo
     * @return
     */
    @RequestMapping(value = "/getFloors", method = RequestMethod.POST)
    public JsonResponse getFloors(@RequestBody ParkingFloorVo floorVo){
        List<ParkingFloorVo> floors = service.getFloors(floorVo);
        return JsonResponse.success(floors);
    }

    /**
     * 根据楼层id查询属于该楼层的所有区域
     * 可附加条件字段：publishState
     * @param regionVo
     * @return
     */
    @RequestMapping(value = "/getRegionsByBelongToFloorId", method = RequestMethod.POST)
    public JsonResponse getRegionsByBelongToFloorId(@RequestBody ParkingRegionVo regionVo){
        List<ParkingRegionVo> regions = service.getRegionsByBelongToFloorId(regionVo);
        return JsonResponse.success(regions);
    }

    @RequestMapping(value = "/correctRemainPlaces", method = RequestMethod.POST)
    public JsonResponse correctRemainPlaces(@RequestBody ParkingDeviceVo businessVo){
        service.correctRemainPlaces(businessVo);
        return null;
    }



    /*@RequestMapping(value = "/logoutFromCamera", method = RequestMethod.POST)
    public JsonResponse logoutFromCamera(HttpServletRequest request, @RequestBody List<ParkingDevicePo> camList){
        try {
            service.permissionLogoutFromCamera(camList, request);
            return JsonResponse.success("退出相机登录成功！");
        }catch (Exception e){
            return JsonResponse.fail("退出登录相机失败！-->"+e.getMessage());
        }
    }*/
}
