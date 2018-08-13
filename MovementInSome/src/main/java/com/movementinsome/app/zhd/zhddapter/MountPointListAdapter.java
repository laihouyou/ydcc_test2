package com.movementinsome.app.zhd.zhddapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.zhd.communication.object.CorsNode;

import java.util.List;

/**
 * 源节点列表适配器
 */
public class MountPointListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater inflater;
    private List<CorsNode> listCorsNodes;

    private OnItemClickListener onItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public MountPointListAdapter(Context context, List<CorsNode> corsNodes) {
        ctx = context;
        listCorsNodes = corsNodes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listCorsNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return listCorsNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_mountpoint_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMountPoint = (TextView) convertView.findViewById(R.id.tv_mountpoint_name);
            viewHolder.tvIdentifier = (TextView) convertView.findViewById(R.id.tv_mountpoint_Identifier);
            viewHolder.tvRefType = (TextView) convertView.findViewById(R.id.tv_mountpoint_format);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.layout_mountpoint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CorsNode corsNode = listCorsNodes.get(position);
        viewHolder.tvMountPoint.setText(corsNode.MountPoint);
        viewHolder.tvIdentifier.setText(corsNode.Identifier);
        viewHolder.tvRefType.setText(corsNode.RefType);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, corsNode);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvMountPoint;
        TextView tvIdentifier;
        TextView tvRefType;
        LinearLayout linearLayout;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CorsNode corsNode);
    }
}