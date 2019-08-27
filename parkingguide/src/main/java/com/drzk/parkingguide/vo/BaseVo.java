package com.drzk.parkingguide.vo;

import java.util.Date;

public class BaseVo {

    private int pageNum;

    private int pageSize;

    private Date timeRangeBegin;

    private Date timeRangeEnd;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Date getTimeRangeBegin() {
        return timeRangeBegin;
    }

    public void setTimeRangeBegin(Date timeRangeBegin) {
        this.timeRangeBegin = timeRangeBegin;
    }

    public Date getTimeRangeEnd() {
        return timeRangeEnd;
    }

    public void setTimeRangeEnd(Date timeRangeEnd) {
        this.timeRangeEnd = timeRangeEnd;
    }
}
