package com.mirrordust.telecomlocate.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mirrordust.telecomlocate.entity.Sample;
import com.mirrordust.telecomlocate.model.SampleManager;
import com.mirrordust.telecomlocate.presenter.SamplePresenter;

import rx.functions.Action1;

public class SampleService extends Service {
    public static final String TAG = "SampleService";
    private final IBinder mBinder = new LocalBinder();
    private String mode;
    private SampleManager mSampleManager;
    private SamplePresenter mPresenter;
    private Handler mHandler = new Handler();
    private Runnable mDataCollection = new Runnable() {
        @Override
        public void run() {
            try {
                requestRecord();
            } finally {
                mHandler.postDelayed(mDataCollection, 5000);
/*                SignalStrength signalStrength = (SignalStrength) getApplication();
                mHandler.postDelayed(mDataCollection, signalStrength.getmTimeInterval());*/
            }
        }
    };

    public SampleService() {
    }

    private void requestRecord() {
        mSampleManager.fetchRecord().subscribe(new Action1<Sample>() {
            @Override
            public void call(Sample sample) {
                sample.setIndex(0);
                sample.setMode(mode);
                mPresenter.addOrUpdateSample(sample);
            }
        });
    }

    public void startCollecting() {
        mDataCollection.run();
    }

    public void stopCollecting() {
        mHandler.removeCallbacks(mDataCollection);
    }

    public void setRecordManager(Context context) {
        mSampleManager = new SampleManager(context);
    }

    public boolean isRecordManagerInitialized() {
        return mSampleManager != null;
    }

    public void setSamplePresenter(SamplePresenter presenter) {
        mPresenter = presenter;
    }

    public boolean isSamplePresenterInitialized() {
        return mPresenter != null;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public SampleService getService() {
            return SampleService.this;
        }
    }
}
