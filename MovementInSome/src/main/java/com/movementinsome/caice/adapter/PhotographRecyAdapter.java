package com.movementinsome.caice.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movementinsome.R;

import java.util.List;

/**
 * Created by zzc on 2017/3/30.
 */

public class PhotographRecyAdapter extends RecyclerView.Adapter<PhotographRecyAdapter.MyViewHolder> {
    private Context context;
    private List<String> datas;
    protected OnItemClickListener mOnItemClickListener;

    public PhotographRecyAdapter(Context context, List<String> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_photograph_recycleview, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.im.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(datas.get(position))));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,holder,layoutPos);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,holder,layoutPos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(int position,String addData) {
        datas.add(position, addData);
        notifyItemInserted(position);
    }

    public void addData(String addData) {
        datas.add(addData);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView im;
        public MyViewHolder(View view) {
            super(view);
            im = (ImageView) view.findViewById(R.id.imRecyView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas2) {
        datas = datas2;
    }

}
