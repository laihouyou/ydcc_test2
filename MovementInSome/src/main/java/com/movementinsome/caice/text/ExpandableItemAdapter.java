package com.movementinsome.caice.text;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;
import com.movementinsome.caice.activity.OfflineActivity2;


/**
 * 分组可点击展开的adapter
 * @author lizhixian
 * @time 2017/1/12 22:36
 */

public class ExpandableItemAdapter extends BaseMultiAdapter<MultiItemEntity> {
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();
//    public static final int TYPE_LEVEL_ZERO = 0;
    public static final int TYPE_LEVEL_ONE = 1;
    public static final int TYPE_ENTITY = 2;

    private OfflineActivity2 activity;

    public ExpandableItemAdapter(Context context) {
        super(context);
//        addItemType(TYPE_LEVEL_ZERO, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_ONE, R.layout.item_all_cities);
        addItemType(TYPE_ENTITY, R.layout.item_all_cities_chins);

        this.activity= (OfflineActivity2) context;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MultiItemEntity item = getDataList().get(position);
        switch (item.getItemType()) {
//            case TYPE_LEVEL_ZERO:
//                bindLevel0Item(holder,position, (Level0Item)item);
//                break;
            case TYPE_LEVEL_ONE:
                bindLevel1Item(holder,position, (ExGroupVo)item);
                break;
            case TYPE_ENTITY:
                bindEntityItem(holder,position, (ExChindVo) item);
                break;
            default:
                break;
        }

    }

//    private void bindLevel0Item(final SuperViewHolder holder, final int position, final Level0Item item) {
//        TextView title = holder.getView(R.id.title);
//        TextView subTitle = holder.getView(R.id.sub_title);
//        TextView expandState = holder.getView(R.id.expand_state);
//        title.setText(item.title);
//        subTitle.setText(item.subTitle);
//        expandState.setText(item.isExpanded() ? R.string.expanded : R.string.collapsed);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Level 0 item pos: " + position);
//                if (item.isExpanded()) {
//                    collapse(position);
//                } else {
//                    if (position % 3 == 0) {
//                        expandAll(position, false);
//                    } else {
//                        expand(position);
//                    }
//                }
//            }
//        });
//    }

    private void bindLevel1Item(final SuperViewHolder holder, final int position, final ExGroupVo item) {
        TextView tv_city = holder.getView(R.id.tv_city);
        tv_city.setText(item.getProvinceName());
//        final TextView tv_in_the_download = holder.getView(R.id.tv_in_the_download);
//        TextView download_size = holder.getView(R.id.download_size);
//        download_size.setText(OfflineActivity2.formatDataSize(item.getDataSize())+"");
//        TextView download_man_size = holder.getView(R.id.download_man_size);
//        ImageView download_icon = holder.getView(R.id.download_icon);
//        download_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int cityid = item.getProvinceId();
//                activity.mOffline.start(cityid);
//                activity.lm.setVisibility(View.VISIBLE);
//                activity.cl.setVisibility(View.GONE);
//                activity.tv_offline_have_downloaded.setBackgroundColor(activity.getResources()
//                        .getColor(R.color.red));
//                activity.tv_offline_have_downloaded.setTextColor(activity.getResources()
//                        .getColor(R.color.greenyellow));
//
//                activity.tv_offline_not_download.setBackgroundColor(activity.getResources()
//                        .getColor(R.color.gainsboro));
//                activity.tv_offline_not_download.setTextColor(activity.getResources().getColor(
//                        R.color.black));
//                Toast.makeText(activity,
//                        "开始下载" + item.getProvinceName() + "离线地图: ",
//                        Toast.LENGTH_SHORT).show();
//
//                activity.isDownload = true;
//
//                notifyItemChanged(position);
//
//                activity.updateView();
//            }
//        });

        ImageView arrows_im = holder.getView(R.id.arrows_im);
        if (item.isExpanded()) {
            arrows_im.setBackgroundResource(R.drawable.main_mine_down);
        } else {
            arrows_im.setBackgroundResource(R.drawable.main_mine_arrows);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Level 1 item pos: " + position);
                if (item.isExpanded()) {
                    collapse(position, false);
                } else {
                    expand(position, false);
                }
            }
        });
    }

    private void bindEntityItem(SuperViewHolder holder, final int position, final ExChindVo item) {
        TextView tv_city = holder.getView(R.id.tv_city);
        tv_city.setText(item.getCtilName());
        final TextView tv_in_the_download = holder.getView(R.id.tv_in_the_download);
        TextView download_size = holder.getView(R.id.download_size);
        download_size.setText(OfflineActivity2.formatDataSize(item.getDataSize())+"");
        final TextView download_man_size = holder.getView(R.id.download_man_size);

        ImageView download_icon = holder.getView(R.id.download_icon);
        if (item.getStatus()!=0){
            tv_in_the_download.setText(item.getStatusStr());
            tv_in_the_download.setVisibility(View.VISIBLE);

            download_man_size.setText(item.getRatio()+"%");
            download_man_size.setVisibility(View.VISIBLE);
        }
        if (item.getStatus()==4){
            download_icon.setVisibility(View.GONE);
        }else {
            download_icon.setVisibility(View.VISIBLE);
        }

        download_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cityid = item.getCtilId();
                activity.mOffline.start(cityid);

                tv_in_the_download.setText(item.getStatusStr());
                tv_in_the_download.setVisibility(View.VISIBLE);

                download_man_size.setText(item.getRatio()+"%");
                download_man_size.setVisibility(View.VISIBLE);

                activity.lm.setVisibility(View.VISIBLE);
                activity.cl.setVisibility(View.GONE);
                activity.tv_offline_have_downloaded.setBackgroundColor(activity.getResources()
                        .getColor(R.color.red));
                activity.tv_offline_have_downloaded.setTextColor(activity.getResources()
                        .getColor(R.color.greenyellow));

                activity.tv_offline_not_download.setBackgroundColor(activity.getResources()
                        .getColor(R.color.gainsboro));
                activity.tv_offline_not_download.setTextColor(activity.getResources().getColor(
                        R.color.black));
                Toast.makeText(activity,
                        "开始下载" + item.getCtilName() + "离线地图: ",
                        Toast.LENGTH_SHORT).show();

                activity.isDownload = true;

                notifyItemChanged(position);

                activity.updateExListview();

            }
        });
    }

}
