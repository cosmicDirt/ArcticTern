package com.mirrordust.telecomlocate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.entity.Sample;
import com.mirrordust.telecomlocate.util.Converter;

import io.realm.RealmResults;

/**
 * Created by LiaoShanhe on 2017/07/11/011.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {

    private static final String ARG_SAMPLE_ID = "sample_id";
    private Context mContext;
    private RealmResults<Sample> mSampleList;

    public SampleAdapter(Context context, RealmResults<Sample> samples) {
        mContext = context;
        mSampleList = samples;
    }

    public void addSample(Sample sample) {
        //add new sample to the beginning
        mSampleList.add(0, sample);
        notifyItemInserted(0);
    }

    public void addSample() {
        notifyItemInserted(0);

    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_sample_recycler_view, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        int size = mSampleList.size();
        int sampleIdx = size - position - 1;
        int showIdx = sampleIdx + 1;
        Sample s = mSampleList.get(sampleIdx);
        holder.No.setText(Converter.index2String(showIdx));
        holder.BSNum.setText(Converter.baseStationNum2String(s.getBSList().size()));
        holder.sampleTime.setText(Converter.timestamp2LocalTime(s.getTime()));
        holder.sampleLocation.setText(Converter.Latlng2String(s.getLatLng()));
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SampleDetailActivity.class);
                intent.putExtra(ARG_SAMPLE_ID, "测试id_123456789");
                mContext.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mSampleList.size();
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        TextView No;
        TextView BSNum;
        TextView sampleTime;
        TextView sampleLocation;

        SampleViewHolder(View itemView) {
            super(itemView);
            No = (TextView) itemView.findViewById(R.id.tv_No);
            BSNum = (TextView) itemView.findViewById(R.id.tv_bs_num);
            sampleTime = (TextView) itemView.findViewById(R.id.tv_time);
            sampleLocation = (TextView) itemView.findViewById(R.id.tv_loc);
        }
    }
}
