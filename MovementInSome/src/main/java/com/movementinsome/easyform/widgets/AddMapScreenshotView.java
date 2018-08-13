package com.movementinsome.easyform.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.movementinsome.AppContext;
import com.movementinsome.easyform.widgets.michooser.activity.ShowPictureActivity;
import com.movementinsome.easyform.widgets.michooser.adapter.GridImageAdapter;
import com.movementinsome.map.MapBizViewer;
import com.movementinsome.map.utils.LocScreenshotUtil;

public class AddMapScreenshotView {
	private GridView gridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private ArrayList<String> imgePathList;
	private GridImageAdapter gridImageAdapter;
	private Context context;
	//private String imageDir = "";
	private LinearLayout linearLayout;
	private String bizType;  //配合业务的需要，用于识别图片的业务类型
	private String storePath;
	private Integer qty;
	
	public AddMapScreenshotView(Context context,LinearLayout linearLayout,String bizType,Integer qty){
		this.context=context;
		this.linearLayout=linearLayout;
		this.bizType = bizType;
		this.qty = qty;
		storePath = AppContext.getInstance().getAppStoreMedioPath();
	}
	// @Param:imgePathList(原有相片)
	public GridView getCameraView(ArrayList<String> imgePathList){
		init(imgePathList);
		initListener();
		LinearLayout cLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(paramsLayout);
		gridView.setNumColumns(4);
		gridView.setVerticalSpacing(12);
		gridView.setHorizontalSpacing(12);
		return gridView;
	}
	// 获取相片路径数组
	public ArrayList<String> getImgePathList(){
		return getIntentArrayList(dataList);
	}
	
	private void init(ArrayList<String> imgePathList) {
		gridView = new GridView(context);
		dataList.addAll(imgePathList);
		dataList.add("camera_default");
		gridImageAdapter = new GridImageAdapter(context, dataList);
		gridView.setAdapter(gridImageAdapter);
	}
	private void initListener() {

		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!dataList.get(position).contains("default")) {
					File file = new File(dataList.get(position));

					//下方是是通过Intent调用系统的图片查看器的关键代码
					Intent intent = new Intent();//调用图片显示功能
					intent.setClass(context, ShowPictureActivity.class);
					intent.putStringArrayListExtra("listPath", getIntentArrayList(dataList));
					intent.putExtra("id", position);
					ShowPictureActivity.addMapScreenshotView=AddMapScreenshotView.this;
					context.startActivity(intent);
					
				}else{

					Intent intent = new Intent(context,
							MapBizViewer.class);
					LocScreenshotUtil.addMapScreenshotView=AddMapScreenshotView.this;
					intent.putExtra("type", 10007);
					intent.putExtra("storePath", storePath);
					intent.putExtra("bizType", bizType);
					intent.putStringArrayListExtra("dataList", getIntentArrayList(dataList));
					context.startActivity(intent);
				}
				//发出变化事件
				notifyDataSetChange();
			}

		});
	}
	public void updateUI(List<String> tDataList){
		if (tDataList != null) {
			if (tDataList.size() < qty) {
				tDataList.add("camera_default");
			}
			dataList.clear();
			dataList.addAll(tDataList);
			gridImageAdapter.notifyDataSetChanged();
			int h=gridView.getChildAt(0).getHeight();
			int x=(tDataList.size()-1)/4+1;
			h=(int) (h*1.2*x);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
					ViewGroup.LayoutParams.MATCH_PARENT, 
					h);
			linearLayout.setLayoutParams(params);
		}
	}
	private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

		ArrayList<String> tDataList = new ArrayList<String>();

		for (String s : dataList) {
			if (!s.contains("default")) {
				tDataList.add(s);
			}
		}

		return tDataList;

	}
	
	/**
	 * 监听
	 * @author gordon
	 *
	 */
	public interface DataSetSupervioer {
        public void onChange();
    }

    private DataSetSupervioer dataSetChangeListener;

    public void setDataSetChangeListener(DataSetSupervioer dataSetChangeListener) {
        this.dataSetChangeListener = dataSetChangeListener;
    }

    public void notifyDataSetChange() {
        if (null != dataSetChangeListener) {
            dataSetChangeListener.onChange();
        }
    }
}
