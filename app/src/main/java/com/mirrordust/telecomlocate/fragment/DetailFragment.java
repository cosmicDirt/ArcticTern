package com.mirrordust.telecomlocate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.activity.SampleDetailActivity;
import com.mirrordust.telecomlocate.adapter.DetailAdapter;
import com.mirrordust.telecomlocate.pojo.DetailItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    private static final String ARG_SAMPLE_ID = "sample_id";
    private RecyclerView mRecyclerView;

    public DetailFragment() {
        // Required empty public constructor
    }

//    public static DetailFragment newInstance(String id) {
//        DetailFragment fragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_SAMPLE_ID, id);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_SAMPLE_ID);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_detail, container, false);
        mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.detail_rv);
        return frameLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycleView();
    }

    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        SampleDetailActivity parentActivity = (SampleDetailActivity) getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.setAdapter(new DetailAdapter(parentActivity, getDetailItemList(parentActivity)));
    }

    private List<DetailItem> getDetailItemList(SampleDetailActivity parentActivity) {
        String id = parentActivity.getSampleId();
        List<DetailItem> detailItemList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        set.add(0);
        set.add(4);
        set.add(7);
        set.add(9);
        set.add(15);
        set.add(25);
        set.add(35);
        set.add(45);
        set.add(46);
        set.add(47);
        set.add(59);
        for (int i = 0; i < 60; i++) {
            if (set.contains(i)) {
                detailItemList.add(new DetailItem("标题", ""));
            } else {
                detailItemList.add(new DetailItem(id, i+".="));
            }
        }
        return detailItemList;
    }
}
