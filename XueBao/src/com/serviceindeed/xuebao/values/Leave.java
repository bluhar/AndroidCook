package com.serviceindeed.xuebao.values;

import java.util.Date;

public class Leave {

    private int    mId;
    private int    mStatus; //审核中  已审核
    private Date   mStart;
    private Date   mEnd;
    private String mReason;
    private Date   mCreate;
    private String mType;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public Date getStart() {
        return mStart;
    }

    public void setStart(Date start) {
        mStart = start;
    }

    public Date getEnd() {
        return mEnd;
    }

    public void setEnd(Date end) {
        mEnd = end;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public Date getCreate() {
        return mCreate;
    }

    public void setCreate(Date create) {
        mCreate = create;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}
