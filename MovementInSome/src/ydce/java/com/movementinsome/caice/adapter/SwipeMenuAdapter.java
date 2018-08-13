package com.movementinsome.caice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.caice.activity.NewMap;
import com.movementinsome.caice.base.ListBaseAdapter;
import com.movementinsome.caice.base.SuperViewHolder;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.SwipeMenuView;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.utils.MapMeterScope;

import java.util.Map;

/**
 * 设施列表数据适配器
 */
public class SwipeMenuAdapter extends ListBaseAdapter<SavePointVo> {
    private Context mContext;
    private Map<Integer, Boolean> isSelected;
    private boolean isShowCheckBox;
    private TextView check_all;
    private RelativeLayout submitFr;
    private TextView tv_cancel;
    private Map<String,View> views;

    public SwipeMenuAdapter(Context context,Map<Integer, Boolean> isSelected,boolean isShowCheckBox,
                            Map<String,View> views
    ) {
        super(context);
        this.mContext=context;
        this.isSelected=isSelected;
        this.isShowCheckBox=isShowCheckBox;
        this.views=views;
        if (views!=null){
            check_all= (TextView) views.get("check_all");
            submitFr= (RelativeLayout) views.get("submitFr");
            tv_cancel= (TextView) views.get("tv_cancel");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fac_listview;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final SavePointVo item=getDataList().get(position);
        View contentView = holder.getView(R.id.swipe_content);
        Button btnDelete = holder.getView(R.id.btnDelete);
        Button btnTop = holder.getView(R.id.btnTop);

        TextView projcet_fac_tv=holder.getView(R.id.projcet_fac_tv);
        TextView gather_type_fac=holder.getView(R.id.gather_type_fac);
        TextView resubmit_date_tv_fac=holder.getView(R.id.resubmit_date_tv_fac);
        TextView adder_tv_fac=holder.getView(R.id.adder_tv_fac);
        TextView location_text_fac=holder.getView(R.id.location_text_fac);
        CheckBox resubmit_checkBox_fac=holder.getView(R.id.resubmit_checkBox_fac);
        Button newest_item_fac = holder.getView(R.id.newest_item_fac);      //最新
        ImageView projcet_type_fac = holder.getView(R.id.projcet_type_fac);      //图标

        gather_type_fac.setText("采集方式:"+item.getGatherType());
        resubmit_date_tv_fac.setText("时间:"+item.getFacPipBaseVo().getUploadTime()==null
                ?"":item.getFacPipBaseVo().getUploadTime());
        adder_tv_fac.setText("地址:"+item.getHappenAddr());

        //设置图标
        if (item.getDataType().equals(MapMeterMoveScope.POINT)){
            projcet_fac_tv.setText("编号:"+item.getFacName());
            projcet_type_fac.setBackgroundResource(R.drawable.point_left);
        }else if (item.getDataType().equals(MapMeterMoveScope.LINE)){
            projcet_fac_tv.setText("编号:"+item.getPipName());
            projcet_type_fac.setBackgroundResource(R.drawable.line_left);
        }

        if (item.getFacSubmitStatus()!=null){
            if (item.getFacSubmitStatus().equals("0")){
                newest_item_fac.setText("未同步");
                newest_item_fac.setVisibility(View.VISIBLE);
                newest_item_fac.setTextColor(mContext.getResources().getColor(R.color.red));
            }else if (item.getFacSubmitStatus().equals("1")){
                newest_item_fac.setText("已同步");
                newest_item_fac.setVisibility(View.VISIBLE);
                newest_item_fac.setTextColor(mContext.getResources().getColor(R.color.lightgreen));
            }
        }else {
            newest_item_fac.setText("未同步");
            newest_item_fac.setVisibility(View.VISIBLE);
            newest_item_fac.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        resubmit_checkBox_fac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSelected.put(position+1, b);
                if (!isSelected.containsValue(false)) {
                    check_all.setTag("uncheck_all");
                    check_all.setText("全不选");
                } else {
                    check_all.setTag("check_all");
                    check_all.setText("全选");
                }
            }
        });

        if (isSelected!=null&&isSelected.size()>0){
            resubmit_checkBox_fac.setChecked(isSelected.get(position+1));
        }

        if (isShowCheckBox) {
            resubmit_checkBox_fac.setVisibility(View.VISIBLE);
        } else {
            resubmit_checkBox_fac.setVisibility(View.GONE);
        }

        location_text_fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewMap.class);
                intent.putExtra("facName", item.getDataType().equals(MapMeterScope.POINT)?item.getFacName():item.getPipName());
                intent.putExtra("dateType", item.getDataType());
                intent.putExtra("longitude", item.getLongitude());
                intent.putExtra("latitude", item.getLatitude());
                intent.putExtra("pointList", item.getPointList());
                mContext.startActivity(intent);
            }
        });

        contentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //显示全选，删除等按钮
                submitFr.setVisibility(View.VISIBLE);
                check_all.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);

                isSelected.put(position+1,true);

                isShowCheckBox=true;
                notifyDataSetChanged();
                return false;
            }
        });

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe(true);



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppToast.makeShortToast(mContext, getDataList().get(position).title);
//                Log.d("TAG", "onClick() called with: v = [" + v + "]");
//            }
//        });
        //置顶：
//        btnTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null!=mOnSwipeListener){
//                    mOnSwipeListener.onTop(position);
//                }
//
//            }
//        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }

    public Map<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Map<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }
}

