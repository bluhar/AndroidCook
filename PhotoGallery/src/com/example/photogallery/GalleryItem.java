package com.example.photogallery;


public class GalleryItem {
    
    private String mCaption;
    private String mId;
    private String mUrl;
    
    /**
     * @return the caption
     */
    public String getCaption() {
        return mCaption;
    }
    
    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        mCaption = caption;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return mId;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        mId = id;
    }
    
    /**
     * @return the url
     */
    public String getUrl() {
        return mUrl;
    }
    
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        mUrl = url;
    }
    
    
    @Override
    public String toString() {
        return mCaption;
    }
    

}
