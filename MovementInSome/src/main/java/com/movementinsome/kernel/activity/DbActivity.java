package com.movementinsome.kernel.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.res.Configuration;
import android.view.Gravity;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.database.db.AppDataBaseHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class DbActivity extends OrmLiteBaseActivity<AppDataBaseHelper>  {
	
	
	private boolean isOuter; //是否外网
	
	private static Toast toast;	
	
	public static void pToastShow(String showContent){
		if (toast==null){
			toast = Toast.makeText(AppContext.getInstance(), "", 1);
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.setText(showContent);
		toast.show();
	}
	
	/**
	 * 取消Toast显示
	 */
	public void pToastConcel(){
		toast.cancel();
	}	
	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文
	 * @param str
	 * @return
	 */
	public boolean isValidTagAndAlias(String str) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public boolean isOuter() {
		return isOuter;
	}

	public void setOuter(boolean isOuter) {
		this.isOuter = isOuter;
	}
	

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}
}
