package com.example.runtracker;

import android.content.Context;

public class Runloader extends DataLoader<Run> {

    private long mRunId;

    public Runloader(Context context, long runId) {
        super(context);
        this.mRunId = runId;
    }

    @Override
    public Run loadInBackground() {

        return RunManager.getInstance(getContext()).getRun(mRunId);
    }

}
