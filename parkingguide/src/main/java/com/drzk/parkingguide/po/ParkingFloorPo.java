package com.drzk.parkingguide.po;

import java.util.Date;

public class ParkingFloorPo {
    private String id;

    private Date createDate;

    private Date updateDate;

    private String parkName;

    private String floorName;

    private Integer totalPlaces;

    private Integer specificPlaces;

    private Integer tempPlaces;

    private Integer specificInUse;

    private Integer tempInUse;

    private Integer reservedPlaces;

    private Integer remainPlaces;

    private String manager;

    private String memo;

    private Integer publishState;

    public Integer getPublishState() {
        return publishState;
    }

    public void setPublishState(Integer publishState) {
        this.publishState = publishState;
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

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName == null ? null : parkName.trim();
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName == null ? null : floorName.trim();
    }

    public Integer getTotalPlaces() {
        return totalPlaces;
    }

    public void setTotalPlaces(Integer totalPlaces) {
        this.totalPlaces = totalPlaces;
    }

    public Integer getSpecificPlaces() {
        return specificPlaces;
    }

    public void setSpecificPlaces(Integer specificPlaces) {
        this.specificPlaces = specificPlaces;
    }

    public Integer getTempPlaces() {
        return tempPlaces;
    }

    public void setTempPlaces(Integer tempPlaces) {
        this.tempPlaces = tempPlaces;
    }

    public Integer getSpecificInUse() {
        return specificInUse;
    }

    public void setSpecificInUse(Integer specificInUse) {
        this.specificInUse = specificInUse;
    }

    public Integer getTempInUse() {
        return tempInUse;
    }

    public void setTempInUse(Integer tempInUse) {
        this.tempInUse = tempInUse;
    }

    public Integer getReservedPlaces() {
        return reservedPlaces;
    }

    public void setReservedPlaces(Integer reservedPlaces) {
        this.reservedPlaces = reservedPlaces;
    }

    public Integer getRemainPlaces() {
        return remainPlaces;
    }

    public void setRemainPlaces(Integer remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}