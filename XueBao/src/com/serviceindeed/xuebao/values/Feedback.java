package com.serviceindeed.xuebao.values;

import java.util.Date;

public class Feedback {

    private int    mId;
    private String mContent;
    private Date   mCreateDate;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

}
