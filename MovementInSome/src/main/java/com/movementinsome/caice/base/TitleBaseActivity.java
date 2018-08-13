package com.movementinsome.caice.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

/**
 * Created by zzc on 2017/8/10.
 */

public abstract class TitleBaseActivity extends FullActivity {

    private Toolbar toolbar;
    private TextView tvTitle;
    private String   menuStr;
    private int   menuIcon;
    private boolean menuIconIsVible=true;
    private boolean menuStrIsVible=true;

    private static Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbase);
        toolbar = (Toolbar) findViewById(R.id.toolsbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.ContentView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        LayoutInflater.from(this).inflate(getContentView(), contentView);
        setTopLeftButton(R.drawable.black_while, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setTopRightButton("    ", null);
        initViews();
        initTitle();
    }

    protected abstract void initTitle();

    protected abstract void initViews();

    protected abstract int getContentView();

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    private OnClickListener clickListenerTopLeft;
    private OnClickListener clickListenerTopRight;

    public interface OnClickListener {
        void onClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (clickListenerTopLeft != null) {
                clickListenerTopLeft.onClick();
            }
        } else if (item.getItemId() == R.id.menu_1) {
            clickListenerTopRight.onClick();
        }
        return true;
    }

    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener) {
        toolbar.setNavigationIcon(iconResId);
        this.clickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(String str, OnClickListener onClickListener) {
        this.menuStr = str;
        this.clickListenerTopRight = onClickListener;
    }
    protected void setTopRightButton(int iconResId, OnClickListener onClickListener) {
        this.menuIcon = iconResId;
        this.clickListenerTopRight = onClickListener;
    }
    protected void setTopRightButton(String str,int iconResId, OnClickListener onClickListener) {
        this.menuStr = str;
        this.menuIcon = iconResId;
        this.clickListenerTopRight = onClickListener;
    }

    protected void setTopRightVible(boolean isVible) {
        this.menuStrIsVible=isVible;
    }

    protected void setTopLeftVible(boolean isVible) {
        this.menuIconIsVible=isVible;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(menuStr)||menuIcon!=0) {
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_1).setTitle(menuStr).setVisible(menuIconIsVible);
        }
        if (menuIcon!=0) {
            menu.findItem(R.id.menu_1).setIcon(menuIcon).setVisible(menuStrIsVible);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void toast(String showContent){
        if (toast == null) {
            toast = Toast.makeText(AppContext.getInstance(), "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(showContent);
        toast.show();
    }
}
