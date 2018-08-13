package com.movementinsome.caice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.Login;
import com.movementinsome.R;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.movementinsome.caice.util.ActivityCollector;
import com.movementinsome.database.vo.UserBeanVO;


/**
 * Created by zzc on 2017/12/20.
 */

public class PersonalCenterActivity extends TitleBaseActivity implements View.OnClickListener{
    Button btnExitLogin;
    TextView mineName;

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.personal_center));
    }

    @Override
    protected void initViews() {
        btnExitLogin= (Button) findViewById(R.id.btn_exit_login);
        mineName= (TextView) findViewById(R.id.mine_name);

        btnExitLogin.setOnClickListener(this);
        mineName.setOnClickListener(this);

        mineName.setText(AppContext.getInstance().getCurUserName());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_center;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_name:
                break;
            case R.id.btn_exit_login:
                ActivityCollector.removeAllActivity();
                AppContext.getInstance().setCurUser(new UserBeanVO());
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
