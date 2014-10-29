package com.example.criminalintent.v8;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {

    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_DATE="date";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_PHOTO="photo";
    
	private UUID mId;
	private String mTitle;
	
	private Date mDate;
	private boolean mSolved;
	
	private Photo mPhoto;
	
	

	public Crime() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Crime(JSONObject obj) throws JSONException {
	    mId = UUID.fromString(obj.getString(JSON_ID));
	    if (obj.has(JSON_TITLE)) {
	        mTitle = obj.getString(JSON_TITLE);
	    }
	    if (obj.has(JSON_PHOTO)) {
	        mPhoto = new Photo(obj.getJSONObject(JSON_PHOTO));
	    }
	    
	    mDate = new Date(obj.getLong(JSON_DATE));
	    mSolved = obj.getBoolean(JSON_SOLVED);
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return mId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return mDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		mDate = date;
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return mSolved;
	}

	/**
	 * @param solved the solved to set
	 */
	public void setSolved(boolean solved) {
		mSolved = solved;
	}
	
    /**
     * @return the photo
     */
    public Photo getPhoto() {
        return mPhoto;
    }

    
    /**
     * @param photo the photo to set
     */
    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    @Override
	public String toString() {
		return mTitle;
	}
	
	public JSONObject toJSON() throws JSONException {
	    JSONObject obj = new JSONObject();
	    obj.put(JSON_ID, mId);
	    obj.put(JSON_SOLVED, mSolved);
	    obj.put(JSON_TITLE, mTitle);
	    obj.put(JSON_DATE, mDate.getTime());
	    return obj;
	}
	

}
