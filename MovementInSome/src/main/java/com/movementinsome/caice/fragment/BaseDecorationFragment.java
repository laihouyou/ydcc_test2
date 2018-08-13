package com.movementinsome.caice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.movementinsome.R;
import com.movementinsome.caice.recycleView.DividerDecoration;

public abstract class BaseDecorationFragment extends Fragment {

    private LRecyclerView mList;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        mList = (LRecyclerView) view.findViewById(R.id.list);
        mList.setPullRefreshEnabled(false);

        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
                .setHeight(R.dimen.dp_10)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.default_header_color2)
                .build();

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mList.addItemDecoration(divider);

        setAdapterAndDecor(mList);

        return view;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
//                .setHeight(R.dimen.dp_10)
//                .setPadding(R.dimen.dp_4)
//                .setColorResource(R.color.default_header_color2)
//                .build();
//
//        mList.setHasFixedSize(true);
//        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        mList.addItemDecoration(divider);
//
//        setAdapterAndDecor(mList);
//    }

    protected abstract void setAdapterAndDecor(RecyclerView list);

}
