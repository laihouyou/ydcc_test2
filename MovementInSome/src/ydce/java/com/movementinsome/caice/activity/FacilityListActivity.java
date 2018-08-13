package com.movementinsome.caice.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.util.Arith;
import com.movementinsome.caice.adapter.SwipeMenuAdapter;
import com.movementinsome.caice.async.DeletePointLineAsync;
import com.movementinsome.caice.async.ShareProjectAsyn;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.util.SetValueUtil;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.view.RatioImageView;
import com.movementinsome.caice.vo.CityVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.util.ActivityUtil;
import com.movementinsome.kernel.util.FileUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**设施列表详情页面
 * Created by zzc on 2017/6/10.
 */

public class FacilityListActivity extends FullActivity implements View.OnClickListener{

    private TextView check_all;
    private TextView tv_cancel;
    private TextView submitTv;
    private TextView noDate_resub;
    private TextView title_fac;
    private LRecyclerView submit_recy;
    private SwipeMenuAdapter adapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private Button resubmit_back;

    private TextView projcet_lineLenght_tv;
    private TextView projcet_qican_tv;
    private TextView projcet_pointNumber_tv;
    private ImageView projcet_type_image;
    private AppCompatButton two_code_imageButton;

    private RelativeLayout submitFr;
//    private List<StatisticsChileListVo> statisticsChileListVoList;      //一个工程里所有的点线数据，没有按天分开

//    private StatisticsChileListVo statisticsChileListVo;
    private Dao<SavePointVo,Long> savePointDao;
//    private Dao<SaveLineVo,Long> saveLineVoDao;
    private Dao<DynamicFormVO, Long> dynamicFormDao = null;
    private List<DynamicFormVO> dynamicFormList;


    private String projectId;
    private Context context;

    // 用来控制CheckBox的选中状况
    private static Map<Integer, Boolean> isSelected;

    private CustomDialog customDialog;
    private CustomDialog.Builder builder;

    private TraceReceiver receiver;
    private List<SavePointVo> savePointList;
    private ProjectVo projectVo;

    private boolean isShowCheckBox=false;
    private View head_view;
    private RatioImageView full_screen_imageview;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 11;
//    final String BITMAP_PATH= android.os.Environment
//            .getExternalStorageDirectory().getAbsolutePath()+"/MoveEQCodeBitmap/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resubmit);
        context=this;
        try {
            Intent intent=getIntent();
            projectVo = (ProjectVo) intent.getSerializableExtra("projectVo");
            savePointDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
            savePointList=new ArrayList<>();
            initDate();
            initView();
            initHeadDate();
            initOath();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initHeadDate() throws SQLException {
        //添加头布局
        TextView projcet_name_tv= (TextView) head_view.findViewById(R.id.projcet_name_tv);
        TextView projcet_num_tv= (TextView) head_view.findViewById(R.id.projcet_num_tv);
        TextView projcet_type_tv= (TextView) head_view.findViewById(R.id.projcet_type_tv);
        TextView projcet_cDate_tv= (TextView) head_view.findViewById(R.id.projcet_cDate_tv);
        TextView projcet_uDate_tv= (TextView) head_view.findViewById(R.id.projcet_uDate_tv);
//        TextView projcet_shareCode_tv= (TextView) head_view.findViewById(R.id.projcet_shareCode_tv);
        projcet_lineLenght_tv= (TextView) head_view.findViewById(R.id.projcet_lineLenght_tv);
        projcet_pointNumber_tv= (TextView) head_view.findViewById(R.id.projcet_pointNumber_tv);
        projcet_qican_tv= (TextView) head_view.findViewById(R.id.projcet_qican_tv);
        projcet_type_image= (ImageView) head_view.findViewById(R.id.projcet_type_image);
        two_code_imageButton= (AppCompatButton) head_view.findViewById(R.id.two_code_imageButton);

        full_screen_imageview= (RatioImageView) head_view.findViewById(R.id.full_screen_imageview);

        SetValueUtil.setProjectBackgroup(projcet_type_image, projectVo.getProjectType());
        projcet_name_tv.setText(getString(R.string.project_name)+ projectVo.getProjectName());
        projcet_num_tv.setText(getString(R.string.project_num)+ projectVo.getProjectNum());
        projcet_type_tv.setText(getString(R.string.project_type)+ projectVo.getProjectType());
        projcet_cDate_tv.setText(getString(R.string.project_cDate)+"\n"+ projectVo.getProjectCreateDateStr());
        projcet_uDate_tv.setText(getString(R.string.project_uDate)+"\n"+ projectVo.getProjectUpdatedDateStr());
//        projcet_shareCode_tv.setText(getString(R.string.project_shareCode)+ projectVo.getProjectShareCode());


        updateData();


    }

    private void updateData() throws SQLException {
        final CityVo[] cityVo = {new CityVo()};
        final List<SavePointVo> savePointVos=new ArrayList<>();
        final float[] lineLenghts = {0};
        final Bitmap[] bitmap = new Bitmap[1];
        projectVo=AppContext.getInstance().getProjectVoDao().queryForSameId(projectVo);
        getObservable(cityVo,savePointVos,lineLenghts,bitmap).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(cityVo,savePointVos,lineLenghts,bitmap));
    }

    private void initOath() {
        builder = new CustomDialog.Builder(this);
        // 注册广播
        receiver = new TraceReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.DELETE_DATE);
        registerReceiver(receiver, filter1);

        EventBus.getDefault().register(this);
    }

    private void initDate() throws SQLException {
//        getObservable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObserver());

        dynamicFormDao = AppContext.getInstance()
                .getAppDbHelper().getDao(DynamicFormVO.class);
        if (projectVo !=null){
            projectId= projectVo.getProjectId();

            setPointLineData(projectId);
        }

        if (adapter!=null){
            adapter.setDataList(savePointList);
        }
    }

    private void setPointLineData(String projectId) throws SQLException {

        savePointList=savePointDao.queryForEq(OkHttpParam.PROJECT_ID,projectId);

    }

    private Observable<Integer> getObservable(final CityVo[] cityVo, final List<SavePointVo> savePointVos,
                                              final float[] lineLenghts, final Bitmap[] bitmap ){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //七参城市
                cityVo[0] =new Gson().fromJson(projectVo.getMoveBaseVo().getQicanStr(),CityVo.class);
                e.onNext(1);

                //管线长度与设施数量
                List<SavePointVo> savePointVoList=savePointDao.queryForEq(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                if (savePointVoList.size()>0){
                    for (SavePointVo savePointVo:savePointVoList){
                        if (savePointVo.getDataType().equals(MapMeterMoveScope.POINT)){
                            savePointVos.add(savePointVo);
                        }else if (savePointVo.getDataType().equals(MapMeterMoveScope.LINE)){
                            lineLenghts[0] += Arith.add(lineLenghts[0],savePointVo.getPipelineLinght());
                        }
                    }
                }
                e.onNext(2);

                //二维码图片生成
                if (projectVo.getProjectShareCode().equals("")){    //不共享
                    e.onNext(3);
                }else {   //共享
                    bitmap[0]=FileUtils.getEQCodeBitmap(projectVo);
                    e.onNext(4);
                }

                e.onComplete();
            }
        });
    }

    private Observer<Integer> getObserver(final CityVo[] cityVo, final List<SavePointVo> savePointVos,
                                          final float[] lineLenghts, final Bitmap[] bitmap){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer s) {
                switch (s){
                    case 1:
                        if (cityVo[0] !=null){
                            projcet_qican_tv.setText(getString(R.string.project_cityName)+ cityVo[0].getCityName());
                        }else {
                            projcet_qican_tv.setText(getString(R.string.project_cityName)+"");
                        }
                        break;
                    case 2:
                        projcet_lineLenght_tv.setText(getString(R.string.project_lineLenght)+ lineLenghts[0]);
                        projcet_pointNumber_tv.setText(getString(R.string.project_pointNumber)+savePointVos.size());
                        break;
                    case 3:
                        two_code_imageButton.setOnClickListener(FacilityListActivity.this);
                        two_code_imageButton.setText(getString(R.string.code_msg));
                        break;
                    case 4:
                        two_code_imageButton.setOnClickListener(FacilityListActivity.this);
                        two_code_imageButton.setBackgroundDrawable(new BitmapDrawable(bitmap[0]));
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        };
    }

    private void initView() throws SQLException {
        check_all= (TextView) findViewById(R.id.check_all);
        tv_cancel= (TextView) findViewById(R.id.tv_cancel);
        submitTv= (TextView) findViewById(R.id.submitTv);
        noDate_resub= (TextView) findViewById(R.id.noDate_resub);
        submit_recy= (LRecyclerView) findViewById(R.id.submit_recy);
        resubmit_back= (Button) findViewById(R.id.resubmit_back);
        submitFr= (RelativeLayout) findViewById(R.id.submitFr);
        title_fac= (TextView) findViewById(R.id.title_fac);
        head_view=getLayoutInflater().inflate(R.layout.item_fac_listview_head,null);

        check_all.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        resubmit_back.setOnClickListener(this);

        submitTv.setText("删除");
        title_fac.setText("设施列表");

        submit_recy.setEmptyView(noDate_resub);

        submit_recy.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_1)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.split)
                .build();

        submit_recy.setHasFixedSize(true);
        submit_recy.addItemDecoration(divider);


        isSelected = new HashMap<>();
        if (savePointList.size()>0){
            for (int i=1;i<=savePointList.size();i++){
                isSelected.put(i,false);
            }
        }
        Map<String,View> views=new HashMap<>();
        views.put("check_all",check_all);
        views.put("submitFr",submitFr);
        views.put("tv_cancel",tv_cancel);
        adapter=new SwipeMenuAdapter(this,isSelected,isShowCheckBox,views);
        adapter.setDataList(savePointList);
        adapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {

                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(FacilityListActivity.this))
                        .widthpx(ActivityUtil.getWindowsWidth(FacilityListActivity.this))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    List<SavePointVo> savePointVoList=new ArrayList<>();
                                    savePointVoList.add(savePointList.get(pos));
                                    DeletePointLineAsync asyn = new DeletePointLineAsync(
                                            context,
                                            savePointVoList,
                                            true,
                                            pos
                                    );
                                    asyn.execute("");

                                    customDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

            }

            @Override
            public void onTop(int pos) {

            }
        });
        lRecyclerViewAdapter=new LRecyclerViewAdapter(adapter);
        submit_recy.setAdapter(lRecyclerViewAdapter);

        lRecyclerViewAdapter.addHeaderView(head_view);

        //禁用下拉刷新与加载更多
        submit_recy.setPullRefreshEnabled(false);
        submit_recy.setLoadMoreEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_all:    //全选
                switch ((String) check_all.getTag()) {
                    case "check_all":   //全选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 1; i <= savePointList.size(); i++) {
                            isSelected.put(i, true);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("uncheck_all");
                        check_all.setText("全不选");
                        break;
                    case "uncheck_all":     //全不选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 1; i <= savePointList.size(); i++) {
                            isSelected.put(i, false);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("check_all");
                        check_all.setText("全选");
                        break;
                }
                break;

            case R.id.tv_cancel:    //取消
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 1; i <= savePointList.size(); i++) {
                    isSelected.put(i, false);
                }

                isShowCheckBox=false;
                adapter.setShowCheckBox(isShowCheckBox);
                // 刷新listview和TextView的显示
                adapter.notifyDataSetChanged();

                check_all.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                submitFr.setVisibility(View.GONE);


                break;

            case R.id.submitTv:     //删除

                if (isSelected!=null&&isSelected.size()>0){
                    if (!isSelected.containsValue(true)){
                        Toast.makeText(context,"请选择要删除的设施或管线",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(this))
                        .widthpx(ActivityUtil.getWindowsWidth(this))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isSelected != null && isSelected.size() > 0) {
                                    String ids = "";

                                    try {
                                        List<SavePointVo> savePointVos=new ArrayList<>();
                                        for (int i = 1; i <= isSelected.size(); i++) {
                                            if (isSelected.get(i)) {
                                                savePointVos.add(savePointList.get(i-1));
                                            }
                                        }
                                        DeletePointLineAsync asyn = new DeletePointLineAsync(
                                                context,
                                                savePointVos,
                                                false,
                                                -1
                                        );
                                        asyn.execute("");

                                        customDialog.dismiss();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

                break;
            case R.id.two_code_imageButton:    //二维码
                if (projectVo.getProjectShareCode().equals("")){    //不共享
                    Intent intent = new Intent(this, QRCodeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{   //共享
                    startPictureActivity(OkHttpParam.BITMAP_PATH+ projectVo.getProjectId()+".png"
                            , projectVo.getProjectName(),
                            full_screen_imageview);
                }
                break;
            case R.id.resubmit_back:    //返回
                this.finish();
//                try {
//                    String json="{\"sharedProjectId\":\"507468d4-504f-4ab4-8a65-008ddd1b3cc5\",\"shareCode\":\"8a14a417a6c0462ca35aee41ef702f35\"}";
//                    JSONObject jsonObject=new JSONObject(json);
//                    jsonObject.put(OkHttpParam.PROJECT_ID,projectId);
//                    ShareProjectAsyn shareProjectAsyn=new ShareProjectAsyn(FacilityListActivity.this,jsonObject);
//                    shareProjectAsyn.execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
        }

    }

    private void startPictureActivity(String filePath,String projectName, View transitView) {
        Intent intent = PictureActivity.newIntent(FacilityListActivity.this, filePath,projectName);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                FacilityListActivity.this, transitView, PictureActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity(FacilityListActivity.this, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void postString(String postString) throws SQLException {
        if (postString.equals(Constant.FAC_LIST_UPDATA)){       //返回true,更新列表
            updateData();
//            Toast.makeText(this, "开始更新UI啦2222222", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        jsonObject.put(OkHttpParam.PROJECT_ID,projectId);
                        ShareProjectAsyn shareProjectAsyn=new ShareProjectAsyn(FacilityListActivity.this,jsonObject);
                        shareProjectAsyn.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class TraceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(Constant.DELETE_DATE)){
                int pos=intent.getIntExtra("pos",-1);
                boolean isSpreads=intent.getBooleanExtra("isSpreads",false);

                //删除成功后刷新列表
                try {
                    setPointLineData(projectId);
                    if (savePointList.size()>0){
                        isSelected.clear();
                        for (int i=1;i<=savePointList.size();i++){
                            isSelected.put(i,false);
                        }
                    }else {
                        isSelected.clear();
                    }
                    adapter.setIsSelected(isSelected);

                    if (pos!=-1&&isSpreads){
                        //RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                        adapter.getDataList().remove(pos);
                        adapter.notifyItemRemoved(pos);//推荐用这个

                        if(pos != (adapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                            adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                        }
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    }else {
                        adapter.setDataList(savePointList);
                    }

                    if (!isSpreads){
                        if (savePointList!=null&&savePointList.size()==0){
                            submitFr.setVisibility(View.GONE);
                            check_all.setVisibility(View.GONE);
                        }else {
                            submitFr.setVisibility(View.VISIBLE);
                            check_all.setVisibility(View.VISIBLE);
                        }
                    }else {
                        submitFr.setVisibility(View.GONE);
                        check_all.setVisibility(View.GONE);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
