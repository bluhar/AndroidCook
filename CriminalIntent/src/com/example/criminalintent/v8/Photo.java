package com.example.criminalintent.v8;

import org.json.JSONException;
import org.json.JSONObject;


public class Photo {

    private static final String JSON_FILENAME = "filename";
    
    private String mFileName;
    
    public Photo(String fileName) {
        this.mFileName = fileName;
    }
    
    public Photo(JSONObject obj) throws JSONException {
        this.mFileName = obj.getString(JSON_FILENAME);
    }
    
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(JSON_FILENAME, mFileName);
        return obj;
    }
    
    public String getFileName() {
        return mFileName;
    }
    
    
    
}
