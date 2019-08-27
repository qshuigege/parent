package com.drzk.parkingguide.vo;

import java.util.Date;
import java.util.List;

public class ParkingDeviceVo extends BaseVo {

    private String id;

    private Date createDate;

    private Date updateDate;

    private String deviceName;

    private String deviceType;

    private String deviceBrand;

    private String deviceIp;

    private Integer devicePort;

    private String deviceAcc;

    private String devicePwd;

    private String floorId;

    private String regionId;

    private String channelId;

    private String cameraType;

    private Integer ledRownum;

    private String ledType;

    private String ledGroupId;

    private String ledGroupName;

    private Integer screenBrightness;

    private String status;

    private String memo;

    private Integer regionRemainPlaces;

    private Integer floorRemainPlaces;

    private Integer ledPartNum;

    private Integer cameraLogin;

    private String floorName;

    private String regionName;

    public ParkingDeviceVo(){}

    public ParkingDeviceVo(String id){
        this.id = id;
    }

    /*private List<ParkingDeviceLedVo> ledPartList;

    public List<ParkingDeviceLedVo> getLedPartList() {
        return ledPartList;
    }

    public void setLedPartList(List<ParkingDeviceLedVo> ledPartList) {
        this.ledPartList = ledPartList;
    }*/

    public Integer getLedPartNum() {
        return ledPartNum;
    }

    public void setLedPartNum(Integer ledPartNum) {
        this.ledPartNum = ledPartNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand == null ? null : deviceBrand.trim();
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp == null ? null : deviceIp.trim();
    }

    public Integer getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(Integer devicePort) {
        this.devicePort = devicePort;
    }

    public String getDeviceAcc() {
        return deviceAcc;
    }

    public void setDeviceAcc(String deviceAcc) {
        this.deviceAcc = deviceAcc == null ? null : deviceAcc.trim();
    }

    public String getDevicePwd() {
        return devicePwd;
    }

    public void setDevicePwd(String devicePwd) {
        this.devicePwd = devicePwd == null ? null : devicePwd.trim();
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId == null ? null : floorId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public Integer getLedRownum() {
        return ledRownum;
    }

    public void setLedRownum(Integer ledRownum) {
        this.ledRownum = ledRownum;
    }

    public String getLedType() {
        return ledType;
    }

    public void setLedType(String ledType) {
        this.ledType = ledType;
    }

    public String getLedGroupId() {
        return ledGroupId;
    }

    public void setLedGroupId(String ledGroupId) {
        this.ledGroupId = ledGroupId == null ? null : ledGroupId.trim();
    }

    public String getLedGroupName() {
        return ledGroupName;
    }

    public void setLedGroupName(String ledGroupName) {
        this.ledGroupName = ledGroupName == null ? null : ledGroupName.trim();
    }

    public Integer getScreenBrightness() {
        return screenBrightness;
    }

    public void setScreenBrightness(Integer screenBrightness) {
        this.screenBrightness = screenBrightness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Integer getRegionRemainPlaces() {
        return regionRemainPlaces;
    }

    public void setRegionRemainPlaces(Integer regionRemainPlaces) {
        this.regionRemainPlaces = regionRemainPlaces;
    }

    public Integer getFloorRemainPlaces() {
        return floorRemainPlaces;
    }

    public void setFloorRemainPlaces(Integer floorRemainPlaces) {
        this.floorRemainPlaces = floorRemainPlaces;
    }

    public Integer getCameraLogin() {
        return cameraLogin;
    }

    public void setCameraLogin(Integer cameraLogin) {
        this.cameraLogin = cameraLogin;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}