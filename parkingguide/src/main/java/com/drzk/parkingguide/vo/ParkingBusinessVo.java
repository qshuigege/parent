package com.drzk.parkingguide.vo;

public class ParkingBusinessVo extends BaseVo {

    private Integer remainPlaces;

    private String ledPartId;

    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Integer getRemainPlaces() {
        return remainPlaces;
    }

    public void setRemainPlaces(Integer remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

    public String getLedPartId() {
        return ledPartId;
    }

    public void setLedPartId(String ledPartId) {
        this.ledPartId = ledPartId;
    }
}
