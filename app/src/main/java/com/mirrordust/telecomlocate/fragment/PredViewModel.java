package com.mirrordust.telecomlocate.fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.mirrordust.telecomlocate.entity.Sample;
import com.mirrordust.telecomlocate.livedata.MRdata;

public class PredViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MRdata mRdata;

    public MRdata getMRdata()
    {
        if (mRdata == null)
        {
            mRdata = new MRdata();
        }
        return mRdata;
    }

    public void setMRdata(MRdata mRdata){
        this.mRdata=mRdata;
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        mRdata = null;
    }
}
