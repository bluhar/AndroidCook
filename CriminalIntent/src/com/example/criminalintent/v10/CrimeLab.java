package com.example.criminalintent.v10;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab {

    private static final String          TAG      = "CrimeLab";
    private static final String          FILENAME = "crimes.json";

    //Ϊʲô��ҪContext? ��Android�У�����Context�������������Activity����ȡ��Ŀ��Դ������Ӧ�õ�˽�д洢�ռ������
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
            //����Ϊʲôû��ֱ��ʹ��context c�أ�
            //��Ϊ���c������activity��Ҳ������service����APP����������������޷���֤activity��serviceһֱ����
            //����Ϊ��֤contextһֱ���ã���Ҫ��ȡApplication Context.
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
