package com.drzk.parkingguide.po;

import java.util.Date;

public class ParkingCameraLedRelationPo {
    private String id;

    private Date createDate;

    private String cameraId;

    private String ledPartId;

    private int publishState;

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

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId == null ? null : cameraId.trim();
    }

    public String getLedPartId() {
        return ledPartId;
    }

    public void setLedPartId(String ledPartId) {
        this.ledPartId = ledPartId == null ? null : ledPartId.trim();
    }

    public int getPublishState() {
        return publishState;
    }

    public void setPublishState(int publishState) {
        this.publishState = publishState;
    }
}