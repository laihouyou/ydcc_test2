package com.movementinsome.kernel.exchange;

import java.io.Serializable;
import java.util.List;

import android.widget.ImageView;

import com.movementinsome.map.view.MyMapView;

public class ExchangeContent implements Serializable {

	private static final long serialVersionUID = 8957360320706242157L;
	private String type;  					//业务类型
	private String url;   					//图层url
	private String actionName; 				//类名
	private ImageView image;        		//图标
	private MyMapView map;          		//地图容器
	private List<LinkAction> linkActions;   //关联动作

	public ExchangeContent(String type, MyMapView map, String url,
			String actionName, ImageView image, List<LinkAction> linkActions) {
		this.type = type;
		this.map = map;
		this.url = url;
		this.actionName = actionName;
		this.image = image;
		this.linkActions = linkActions;
	}

	/*
	 * public Context getContext() { return context; }
	 * 
	 * public void setContext(Context context) { this.context = context; }
	 */

	public MyMapView getMap() {
		return map;
	}

	public void setMap(MyMapView map) {
		this.map = map;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<LinkAction> getLinkActions() {
		return linkActions;
	}

	public void setLinkActions(List<LinkAction> linkActions) {
		this.linkActions = linkActions;
	}

}
