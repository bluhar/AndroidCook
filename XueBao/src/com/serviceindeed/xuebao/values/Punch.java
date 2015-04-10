package com.serviceindeed.xuebao.values;

import java.util.Date;

public class Punch {

    private int    mId;
    private String mType;  //上班 下班
    private String mRemark; //备注
    private String mLocation; //备注
    private Date   mCreate; //创建时间
    

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    
    public String getLocation() {
        return mLocation;
    }

    
    public void setLocation(String location) {
        mLocation = location;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public Date getCreate() {
        return mCreate;
    }

    public void setCreate(Date create) {
        mCreate = create;
    }

}
