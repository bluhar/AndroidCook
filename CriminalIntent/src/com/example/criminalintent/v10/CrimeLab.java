package com.example.criminalintent.v10;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab {

    private static final String          TAG      = "CrimeLab";
    private static final String          FILENAME = "crimes.json";

    //为什么需要Context? 在Android中，有了Context，可以完成启动Activity、获取项目资源，查找应用的私有存储空间等任务
    private Context                      mAppContext;
    private CriminalIntentJSONSerializer mSerializer;
    private static CrimeLab              sCrimeLab;
    private ArrayList<Crime>             mCrimes;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        }
        catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes.", e);
        }
    }

    public static CrimeLab getInstance(Context c) {
        if (sCrimeLab == null) {
            //这里为什么没有直接使用context c呢？
            //因为这个c可以是activity，也可能是service，在APP的整个生命周期里，无法保证activity或service一直存在
            //所以为保证context一直可用，需要获取Application Context.
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
    
    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "Crimes saved to file.");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }
    
    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }

}
