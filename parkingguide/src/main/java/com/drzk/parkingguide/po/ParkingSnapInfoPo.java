package com.drzk.parkingguide.po;

import java.util.Date;

public class ParkingSnapInfoPo {
    private String id;

    private Date createDate;

    private Date updateDate;

    private String cameraId;

    private String cameraIp;

    private String cameraType;

    private String plateNo;

    private String plateType;

    private String plateColor;

    private String vehicleColor;

    private String vehicleType;

    private String vehicleSize;

    private String vehicleBrand;

    private String channelId;

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

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId == null ? null : cameraId.trim();
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp == null ? null : cameraIp.trim();
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType == null ? null : cameraType.trim();
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo == null ? null : plateNo.trim();
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType == null ? null : plateType.trim();
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor == null ? null : plateColor.trim();
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor == null ? null : vehicleColor.trim();
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType == null ? null : vehicleType.trim();
    }

    public String getVehicleSize() {
        return vehicleSize;
    }

    public void setVehicleSize(String vehicleSize) {
        this.vehicleSize = vehicleSize == null ? null : vehicleSize.trim();
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand == null ? null : vehicleBrand.trim();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }
}