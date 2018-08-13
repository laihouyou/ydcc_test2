package com.movementinsome.map.nearby;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearByActivity extends ContainActivity implements OnClickListener,
		OnGestureListener {

	private ViewFlipper nearby_viewpager;// 按钮容器
	private LinearLayout pageGuide;// 页码容器
	private int pageCount;
	private int imageShow = 0;
	private GestureDetector detector;
	private List<String> bnt_data;// 按钮配置
	private ListView nearby_fac_list;// 附近设施类型列表
	// private List<List<String>>fac_type_data;//附近设施类型配置

	private List<List<FacTypeObj>> fac_type_data;// 附近设施类型配置
	// private MapParam mapParam;
	private List<Mapservice> mapServices;
	private List<Ftlayer> ftlayers;
	private List<Module> lstModule;
	private Button nearby_Back;// 返回

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_activity);

		nearby_viewpager = (ViewFlipper) findViewById(R.id.nearby_viewpager);// 按钮容器
		pageGuide = (LinearLayout) findViewById(R.id.pageGuide);// 页码容器
		nearby_fac_list = (ListView) findViewById(R.id.nearby_fac_list);// 附近设施类型列表

		mapServices = (List<Mapservice>) getIntent().getSerializableExtra(
				"mapService");
		ftlayers = (List<Ftlayer>) getIntent().getSerializableExtra("ftLayer");
		fac_type_data = new ArrayList<List<FacTypeObj>>();

		for (Mapservice mapService : mapServices) {

			List<FacTypeObj> fac_type_d1 = new ArrayList<FacTypeObj>();
			FacTypeObj facTypeObj = new FacTypeObj();
			facTypeObj.setLabel(mapService.getLabel());
			facTypeObj.setForeign(mapService.getForeign());
			facTypeObj.setLocal(mapService.getLocal());

			fac_type_d1.add(facTypeObj);
			for (Ftlayer ftLayer : ftlayers) {
				if (ftLayer.getMapservice()!= null && ftLayer.getMapservice().getId() == mapService.getId()) {
					facTypeObj = new FacTypeObj();
					facTypeObj.setLabel(ftLayer.getName());
					facTypeObj.setForeign(mapService.getForeign() + "/"
							+ ftLayer.getLayerId());
					facTypeObj.setLocal(mapService.getLocal() + "/"
							+ ftLayer.getLayerId());

					fac_type_d1.add(facTypeObj);
				}
			}
			if (fac_type_d1.size() > 1)
				fac_type_data.add(fac_type_d1);
		}
		nearby_fac_list.setAdapter(new NearByFacTypeListAdater(fac_type_data, this));

		lstModule = AppContext.getInstance().getModuleData(this.getMenu().getId());
		detector = new GestureDetector(this);
		ArrayList<HashMap<String, Object>> lst = new ArrayList<HashMap<String, Object>>();
		for (Module module : lstModule) {
			if (module.isCanwrite()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (module.getIcon()!=null&&!module.getIcon().equals("")){
					int resID = this.getResources().getIdentifier(module.getIcon(), "drawable",
							this.getPackageName());
					map.put("itemImage", resID);//this.getResources().getDrawable(resID)
				}else{
					map.put("itemImage", R.drawable.function);
				}
				map.put("itemText", module.getName());
				map.put("intentId", module.getId());
				lst.add(map);

			}
		}
		setView(lst);
	}

	private void setView(ArrayList<HashMap<String, Object>> data) {
		if (data != null) {
			int n = 0;
			List<ArrayList<HashMap<String, Object>>> ds = new ArrayList<ArrayList<HashMap<String, Object>>>();
			ArrayList<HashMap<String, Object>> d = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < data.size(); ++i) {
				if (n < 8) {
					d.add(data.get(i));
					++n;
				} else {
					ds.add(d);
					d = new ArrayList<HashMap<String, Object>>();
					n = 0;
					d.add(data.get(i));
					++n;
				}
			}
			if (n != 0) {
				ds.add(d);
			}
			for (int j = 0; j < ds.size(); ++j) {
				GridView gridView = (GridView) View.inflate(this,
						R.layout.nearbygridview, null);
				gridView.setAdapter(new GridAdapter(this, ds.get(j),lstModule));
				nearby_viewpager.addView(gridView);
				ImageView img = new ImageView(this);
				if (j == 0) {
					img.setBackgroundResource(R.drawable.yema1_1);
				} else {
					img.setBackgroundResource(R.drawable.yema1_2);
				}
				pageGuide.addView(img);
			}
			pageCount = pageGuide.getChildCount();
			if (pageCount <= 1) {
				pageGuide.setVisibility(View.GONE);
			} else {
				pageGuide.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 120) {// 下一页
			this.nearby_viewpager.setInAnimation(this, R.anim.push_left_in);
			this.nearby_viewpager.setOutAnimation(this, R.anim.push_left_out);
			nearby_viewpager.showNext();
			if (pageCount > 1) {
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_2);
				++imageShow;
				if (imageShow == pageCount) {
					imageShow = 0;
				}
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_1);
			}
		} else if (e1.getX() - e2.getX() < -120) {// 上一页
			this.nearby_viewpager.setInAnimation(this, R.anim.push_right_in);
			this.nearby_viewpager.setOutAnimation(this, R.anim.push_right_out);
			nearby_viewpager.showPrevious();
			if (pageCount > 1) {
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_2);
				--imageShow;
				if (imageShow < 0) {
					imageShow = pageCount - 1;
				}
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_1);
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nearby_Back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

}
