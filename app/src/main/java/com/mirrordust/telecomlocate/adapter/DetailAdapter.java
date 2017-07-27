package com.mirrordust.telecomlocate.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.pojo.DetailItem;

import java.util.List;

/**
 * Created by LiaoShanhe on 2017/07/12/012.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private Context mContext;
    private List<DetailItem> mDetailItemList;

    public DetailAdapter(Context context, List<DetailItem> detailItemList) {
        this.mContext = context;
        this.mDetailItemList = detailItemList;
    }

    @Override
    public DetailAdapter.DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_recycler_view, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.DetailViewHolder holder, int position) {
        DetailItem detailItem = mDetailItemList.get(position);
        holder.detailItemTitle.setText(detailItem.getTitle());
        holder.detailItemValue.setText(detailItem.getValue());
        // set background color
        if (detailItem.getValue().equals("")) {
            // a new section of details
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.detailListDivideColor));
        } else if (position % 2 == 0) {
            // even view
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.detailListEvenColor));
        } else {
            //odd view
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.detailListOddColor));
        }
    }

    @Override
    public int getItemCount() {
        return mDetailItemList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {

        TextView detailItemTitle;
        TextView detailItemValue;

        DetailViewHolder(View itemView) {
            super(itemView);
            detailItemTitle = (TextView) itemView.findViewById(R.id.tv_title);
            detailItemValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
