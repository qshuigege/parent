package com.drzk.parkingguide.vo;

import java.util.Date;

public class ParkingRegionVo extends BaseVo{
    private String id;

    private Date createDate;

    private Date updateDate;

    private String regionName;

    private String floorId;

    private String floorName;

    private String placeNoBegin;

    private String placeNoEnd;

    private Integer totalPlaces;

    private Integer specificPlaces;

    private Integer tempPlaces;

    private Integer specificInUse;

    private Integer tempInUse;

    private Integer availablePlaces;

    private Integer remainPlaces;

    private Integer warningNum;

    private String regionMapId;

    public ParkingRegionVo(){}

    public ParkingRegionVo(String id){
        this.id = id;
    }

    public String getRegionMapId() {
        return regionMapId;
    }

    public void setRegionMapId(String regionMapId) {
        this.regionMapId = regionMapId;
    }

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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId == null ? null : floorId.trim();
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName == null ? null : floorName.trim();
    }

    public String getPlaceNoBegin() {
        return placeNoBegin;
    }

    public void setPlaceNoBegin(String placeNoBegin) {
        this.placeNoBegin = placeNoBegin == null ? null : placeNoBegin.trim();
    }

    public String getPlaceNoEnd() {
        return placeNoEnd;
    }

    public void setPlaceNoEnd(String placeNoEnd) {
        this.placeNoEnd = placeNoEnd == null ? null : placeNoEnd.trim();
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

    public Integer getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(Integer availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    public Integer getRemainPlaces() {
        return remainPlaces;
    }

    public void setRemainPlaces(Integer remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

    public Integer getWarningNum() {
        return warningNum;
    }

    public void setWarningNum(Integer warningNum) {
        this.warningNum = warningNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

}
