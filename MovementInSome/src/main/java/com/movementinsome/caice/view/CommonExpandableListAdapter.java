package com.movementinsome.caice.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 2017/9/27.
 */

public abstract class CommonExpandableListAdapter<T, K> extends BaseExpandableListAdapter {
    private int childResource;
    private int groupResource;
    private List<List<T>> childrenData = new ArrayList();
    private List<K> groupData = new ArrayList();
    private Context context;

    protected abstract void getChildView(CommonExpandableListAdapter.ViewHolder var1, int var2, int var3, boolean var4, T var5);

    protected abstract void getGroupView(CommonExpandableListAdapter.ViewHolder var1, int var2, boolean var3, K var4);

    protected abstract void adapter();

    public List<List<T>> getChildrenData() {
        return this.childrenData;
    }

    public List<K> getGroupData() {
        return this.groupData;
    }

    public CommonExpandableListAdapter(Context context, int childResource, int groupResource) {
        this.context = context;
        this.childResource = childResource;
        this.groupResource = groupResource;
        adapter();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ((List)this.childrenData.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long)childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CommonExpandableListAdapter.ViewHolder viewHolder = null;
        if(null == convertView) {
            convertView = LayoutInflater.from(this.context).inflate(this.childResource, parent, false);
            viewHolder = new CommonExpandableListAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonExpandableListAdapter.ViewHolder)convertView.getTag();
        }

        this.getChildView(viewHolder, groupPosition, childPosition, isLastChild, (T) ((List)this.childrenData.get(groupPosition)).get(childPosition));
        return convertView;
    }


    public int getChildrenCount(int groupPosition) {
        return this.childrenData.size() > 0?((List)this.childrenData.get(groupPosition)).size():0;
    }

    public Object getGroup(int groupPosition) {
        return this.groupData.get(groupPosition);
    }

    public int getGroupCount() {
        return this.groupData.size();
    }

    public long getGroupId(int groupPosition) {
        return (long)groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CommonExpandableListAdapter.ViewHolder viewHolder = null;
        if(null == convertView) {
            convertView = LayoutInflater.from(this.context).inflate(this.groupResource, parent, false);
            viewHolder = new CommonExpandableListAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonExpandableListAdapter.ViewHolder)convertView.getTag();
        }

        this.getGroupView(viewHolder, groupPosition, isExpanded, this.groupData.get(groupPosition));
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static final class ViewHolder {
        private SparseArray<View> views = new SparseArray();
        private View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public <T extends View> T getView(int viewId) {
            View view = (View)this.views.get(viewId);
            if(null == view) {
                view = this.convertView.findViewById(viewId);
                this.views.put(viewId, view);
            }

            return (T) view;
        }
    }
}
