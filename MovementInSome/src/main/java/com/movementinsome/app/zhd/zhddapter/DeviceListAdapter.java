package com.movementinsome.app.zhd.zhddapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.List;

/**
 * 蓝牙设备列表适配器
 */
public class DeviceListAdapter extends BaseAdapter {

    private Context ctx;
    private List<BluetoothDevice> deviceList;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public DeviceListAdapter(Context context, List<BluetoothDevice> list) {
        ctx = context;
        deviceList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_bluetooth_device_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout_device_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BluetoothDevice device = deviceList.get(position);
        viewHolder.tvText.setText(device.getName());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, device);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvText;
        LinearLayout layout;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BluetoothDevice device);
    }
}
