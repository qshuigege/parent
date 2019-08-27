package com.drzk.parkingguide.vo;

import java.util.Date;

public class ParkingChannelVo extends BaseVo{
    private String id;

    private Date createDate;

    private Date updateDate;

    private String channelName;

    private String regionId;

    private String floorId;

    private String channelType;

    private String inRecogCameraId;

    private String outRecogCameraId;

    private String memo;

    private String regionName;

    private String floorName;

    private String cameraId;

    private String cameraType;

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId == null ? null : floorId.trim();
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType == null ? null : channelType.trim();
    }

    public String getInRecogCameraId() {
        return inRecogCameraId;
    }

    public void setInRecogCameraId(String inRecogCameraId) {
        this.inRecogCameraId = inRecogCameraId == null ? null : inRecogCameraId.trim();
    }

    public String getOutRecogCameraId() {
        return outRecogCameraId;
    }

    public void setOutRecogCameraId(String outRecogCameraId) {
        this.outRecogCameraId = outRecogCameraId == null ? null : outRecogCameraId.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }
}