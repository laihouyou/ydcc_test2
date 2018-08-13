package com.movementinsome.caice.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.async.ResubmitSubAsync;
import com.movementinsome.caice.util.ConstantDate;
import com.movementinsome.caice.vo.HistoryCommitVO;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.kernel.activity.FullActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 断网重提界面
 * Created by zzc on 2017/5/31.
 */

public class ResubmitActivity extends FullActivity implements View.OnClickListener{

    private TextView check_all;
    private TextView submitTv;
    private TextView noDate_resub;
    private ListView submit_recy;
    private Button resubmit_back;
    private RelativeLayout submitFr;

    private CommonListViewAdapter adapter;
    private List<HistoryCommitVO> historyCommitVOList;
    private  Dao<HistoryCommitVO,Long> historyCommitVOLongDao;
    private Context context;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    private  TraceReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resubmit2);
        context=this;
        initOath();
        initView();
    }

    private void initOath() {
        receiver = new TraceReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ConstantDate.UPDATALIST);
        registerReceiver(receiver, filter1);

        try {
            historyCommitVOLongDao= AppContext
                    .getInstance().getAppDbHelper().getDao(HistoryCommitVO.class);
            historyCommitVOList=new ArrayList<>();
            historyCommitVOList=historyCommitVOLongDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        check_all= (TextView) findViewById(R.id.check_all);
        submitTv= (TextView) findViewById(R.id.submitTv);
        noDate_resub= (TextView) findViewById(R.id.noDate_resub);
        submit_recy= (ListView) findViewById(R.id.submit_recy);
        resubmit_back= (Button) findViewById(R.id.resubmit_back);
        submitFr= (RelativeLayout) findViewById(R.id.submitFr);

        check_all.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        resubmit_back.setOnClickListener(this);

        submit_recy.setEmptyView(noDate_resub);

        if (historyCommitVOList!=null&&historyCommitVOList.size()==0){
            submitFr.setVisibility(View.GONE);
            check_all.setVisibility(View.GONE);
        }else {
            submitFr.setVisibility(View.VISIBLE);
            check_all.setVisibility(View.VISIBLE);
        }

        isSelected = new HashMap<>();
        if (historyCommitVOList.size()>0){
            for (int i=0;i<historyCommitVOList.size();i++){
                isSelected.put(i,false);
            }
        }
        adapter=new CommonListViewAdapter<HistoryCommitVO>(context,R.layout.item_resubmit_recycleview,historyCommitVOList) {

            @Override
            protected void convert(ViewHolder viewHolder, HistoryCommitVO item, final int position) {
                viewHolder.setText(R.id.resubmit_date_tv,item.getUploadTime());
                viewHolder.setText(R.id.projcet_fac_tv,"编号："+item.getTableNum());
                viewHolder.setText(R.id.point_type_tv,"类型："+item.getDataType());
                viewHolder.setText(R.id.adder_tv,"地址："+item.getAddr());
                viewHolder.setText(R.id.projcet_type_tv,"工程类型："+item.getProjectType());



                //防止checkBox错乱
                viewHolder.setOnCheckedChangeListener(R.id.resubmit_checkBox, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       isSelected.put(position,isChecked);
                    }
                });
                viewHolder.setChecked(R.id.resubmit_checkBox,isSelected.get(position));

            }
        };

        submit_recy.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_all:    //全选
                switch ((String)check_all.getTag()){
                    case "check_all":   //全选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < historyCommitVOList.size(); i++) {
                            isSelected.put(i, true);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("uncheck_all");
                        check_all.setText("全不选");
                        break;
                    case "uncheck_all":     //全不选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < historyCommitVOList.size(); i++) {
                            isSelected.put(i, false);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("check_all");
                        check_all.setText("全选");
                        break;
                }
                break;

            case R.id.submitTv:     //提交
                ResubmitSubAsync asyn=new ResubmitSubAsync(context,isSelected,historyCommitVOList);
                asyn.execute("");
                break;
            case R.id.resubmit_back:    //返回
                this.finish();
                break;
        }
    }

    private class TraceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ConstantDate.UPDATALIST.equals(action)) {    //获取坐标
                try {
                    historyCommitVOList=historyCommitVOLongDao.queryForAll();
                    adapter.setmDatas(historyCommitVOList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
