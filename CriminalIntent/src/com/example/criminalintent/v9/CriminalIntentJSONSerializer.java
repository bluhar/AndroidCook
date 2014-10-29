package com.example.criminalintent.v9;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class CriminalIntentJSONSerializer {

    private static final String TAG = "CriminalIntentJSONSerializer";

    private Context             mContext;
    private String              mFileName;

    public CriminalIntentJSONSerializer(Context c, String fileName) {
        this.mContext = c;
        this.mFileName = fileName;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Crime crime : crimes) {
            array.put(crime.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream output = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(output);
            writer.write(array.toString());
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> result = new ArrayList<Crime>();

        BufferedReader reader = null;
        try {
            InputStream input = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(input));

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(sb.toString()).nextValue();
            for (int i = 0 ; i < jsonArray.length() ; i++) {
                result.add(new Crime(jsonArray.getJSONObject(i)));
            }

        }
        catch (FileNotFoundException e) {
            Log.d(TAG, mFileName + " not found.");
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }

        return result;
    }

}
