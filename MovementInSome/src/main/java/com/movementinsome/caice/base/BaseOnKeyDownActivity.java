package com.movementinsome.caice.base;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by zzc on 2017/10/25.
 */

public class BaseOnKeyDownActivity extends AppCompatActivity {
    /**
     * 再次点击退出应用
     */
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
/*			if (side_drawer.isMenuShowing()
                    || side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {*/
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                //(new UserLogoutAsyncTask()).execute(null,null,null);
                finish();
                // System.exit(0);
            }
            //}
            return true;
        }
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
