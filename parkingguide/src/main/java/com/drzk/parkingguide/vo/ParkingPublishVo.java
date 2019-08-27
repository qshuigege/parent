package com.drzk.parkingguide.vo;

import java.util.Date;
import java.util.List;

public class ParkingPublishVo extends BaseVo {
    private String id;

    private Date createDate;

    private Date updateDate;

    private String floorId;

    private String regionId;

    private String ledPartId;

    private Integer remainPlaces;

    private Integer status;

    private Integer operationType;

    private Integer publishType;

    private Integer ledRownum;

    private String floorName;

    private String regionName;

    private String deviceName;

    private Integer floorRemainPlaces;

    private Integer regionRemainPlaces;

    public Integer getFloorRemainPlaces() {
        return floorRemainPlaces;
    }

    public void setFloorRemainPlaces(Integer floorRemainPlaces) {
        this.floorRemainPlaces = floorRemainPlaces;
    }

    public Integer getRegionRemainPlaces() {
        return regionRemainPlaces;
    }

    public void setRegionRemainPlaces(Integer regionRemainPlaces) {
        this.regionRemainPlaces = regionRemainPlaces;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getLedRownum() {
        return ledRownum;
    }

    public void setLedRownum(Integer ledRownum) {
        this.ledRownum = ledRownum;
    }

    public Integer getPublishType() {
        return publishType;
    }

    public void setPublishType(Integer publishType) {
        this.publishType = publishType;
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

    public String getLedPartId() {
        return ledPartId;
    }

    public void setLedPartId(String ledPartId) {
        this.ledPartId = ledPartId == null ? null : ledPartId.trim();
    }

    public Integer getRemainPlaces() {
        return remainPlaces;
    }

    public void setRemainPlaces(Integer remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}