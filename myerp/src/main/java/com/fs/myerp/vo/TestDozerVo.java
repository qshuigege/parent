package com.fs.myerp.vo;

import java.util.Date;

public class TestDozerVo {

    private String name;
    private Date createDate;
    private int pageNum;
    private int pageSize;
    private Date timeRangeBegin;
    private Date timeRangeEnd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

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
