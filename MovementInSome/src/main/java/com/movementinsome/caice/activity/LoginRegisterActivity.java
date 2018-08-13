package com.movementinsome.caice.activity;

import com.movementinsome.caice.base.TitleBaseActivity;

/**
 * Created by zzc on 2017/10/31.
 */

public class LoginRegisterActivity extends TitleBaseActivity {
    @Override
    protected void initTitle() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int getContentView() {
        return 0;
    }
//    @Bind(R.id.user_name)
//    ClearableEditText userName;
//    @Bind(R.id.account)
//    ClearableEditText account;
//    @Bind(R.id.password)
//    ClearableEditText password;
//    @Bind(R.id.phone)
//    ClearableEditText phone;
//    @Bind(R.id.company_name)
//    ClearableEditText companyName;
//    @Bind(R.id.email)
//    ClearableEditText email;
//    @Bind(R.id.email_code)
//    ClearableEditText emailCode;
//    @Bind(R.id.get_email_code)
//    TextView getEmailCode;
//    @Bind(R.id.regist)
//    Button regist;
//
//
//    private MyCountDownTimer myCountDownTimer;
//    private Drawable drawable;
//    private Drawable drawable_yes;
//
//    private boolean is_standard_account = false;
//    private boolean is_standard_email = false;
//
//    @Override
//    protected void initTitle() {
//        setTitle(getString(R.string.app_regist));
//        setTopLeftButton(R.drawable.black_while, new OnClickListener() {
//            @Override
//            public void onClick() {
//                finish();
//            }
//        });
//        setTopRightButton("    ", null);
//    }
//
//    @Override
//    protected void initViews() {
//        ButterKnife.bind(this);
//
//        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
//        myCountDownTimer = new MyCountDownTimer(60000, 1000,getEmailCode);
//
//        drawable = ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.input_error);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//
//        drawable_yes = ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.input_correct);
//        drawable_yes.setBounds(0, 0, drawable_yes.getMinimumWidth(), drawable_yes.getMinimumHeight());
//
//        //用户名
//        /**
//         * 监听EditText框中的变化
//         */
//        userName.addTextChangedListener(new TextWatcher() {
//            private CharSequence temp;
//            private int editStart;
//            private int editEnd;
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                temp = s;
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s)){
//                    userName.setCompoundDrawables(null, null, drawable, null);
//                }else {
//                    userName.setCompoundDrawables(null, null, drawable_yes, null);
//                }
//            }
//        });
//
//        //账号    限制只能数字跟字母, 最大长度为30个字符
//        /**
//         * 监听EditText框中的变化
//         */
//        account.addTextChangedListener(new TextWatcher() {
//            private CharSequence temp;
//            private int editStart;
//            private int editEnd;
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                temp = s;
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                editStart = account.getSelectionStart();
//                editEnd = account.getSelectionEnd();
//                if (TextUtils.isEmpty(account.getText().toString())){
//                    is_standard_account = false;
//                    account.setCompoundDrawables(null, null, drawable, null);
//                }else {
//                    if (temp.length() > 30) {//限制长度
//                        Toast.makeText(AppContext.getInstance(),
//                                "输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
//                                .show();
//                        s.delete(editStart - 1, editEnd);
//                        int tempSelection = editStart;
//                        account.setText(s);
//                        account.setSelection(tempSelection);
//
//                        is_standard_account = false;
//                        account.setCompoundDrawables(null, null, drawable, null);
//                    } else {
//                        account.setCompoundDrawables(null, null, drawable_yes, null);
//                        is_standard_account = true;
//                    }
//                }
//
//            }
//        });
//
//        //mima
//        /**
//         * 监听EditText框中的变化
//         */
//        password.addTextChangedListener(new TextWatcher() {
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s)){
//                    password.setCompoundDrawables(null, null, drawable, null);
//                }else {
//                    password.setCompoundDrawables(null, null, drawable_yes, null);
//                }
//            }
//        });
//        //手机号码
//        /**
//         * 监听EditText框中的变化
//         */
//        phone.addTextChangedListener(new TextWatcher() {
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (Lanlngutil.isMobileNO(s.toString())){
//                    phone.setCompoundDrawables(null, null, drawable_yes, null);
//                }else {
//                    phone.setCompoundDrawables(null, null, drawable, null);
//                }
//            }
//        });
//        //公司名称
//        /**
//         * 监听EditText框中的变化
//         */
//        companyName.addTextChangedListener(new TextWatcher() {
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s)){
//                    companyName.setCompoundDrawables(null, null, drawable, null);
//                }else {
//                    companyName.setCompoundDrawables(null, null, drawable_yes, null);
//                }
//            }
//        });
//
//        //邮箱    限制格式
//        email.addTextChangedListener(new TextWatcher() {
//
//            /**
//             * 文本变化之前
//             * @param s
//             * @param start
//             * @param count
//             * @param after
//             */
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            /**
//             * 文本变化中
//             * @param s
//             * @param start
//             * @param before
//             * @param count
//             */
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            /**
//             * 文本变化之后
//             * @param s
//             */
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (Lanlngutil.isEmail(s.toString())) {
//                    email.setCompoundDrawables(null, null, drawable_yes, null);
//                    is_standard_email = true;
//                } else {
//                    email.setCompoundDrawables(null, null, drawable, null);
//                    is_standard_email = false;
//                }
//            }
//        });
//    }
//
//    @Override
//    protected int getContentView() {
//        return R.layout.activity_register;
//    }
//
//
//    @OnClick({R.id.get_email_code, R.id.regist})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.get_email_code:
//                if (!TextUtils.isEmpty(account.getText().toString())
//                        &&!TextUtils.isEmpty(email.getText().toString())){
//                    String serverUrl=OkHttpURL.serverUrl;
//                    if (serverUrl==null){
//                        ToastUtils.show(getString(R.string.error_message2));
//                        return;
//                    }
//                    String url=serverUrl+ OkHttpURL.sendRegisterCaptcha
//                            +OkHttpParam.usUsercode+"=" + account.getText().toString()
//                            + "&"+OkHttpParam.usEmail+"=" + email.getText().toString();
//                    OkHttpUtils.post()
//                            .url(url)
//                            .build()
//                            .execute(new StringDialogCallback(LoginRegisterActivity.this) {
//                                @Override
//                                public void onError(Call call, Exception e, int id) {
//                                    e.printStackTrace();
//                                }
//
//                                @Override
//                                public void onResponse(String response, int id) {
//                                    Log.i("tag",response);
//                                    ResponseVo responseVo= new Gson().fromJson(response,ResponseVo.class);
//                                    if (responseVo.getState()==1){
//                                        myCountDownTimer.start();
//                                    }
//                                    pToastShow(responseVo.getMessage());
//                                }
//                            });
//
//                }else {
//                    pToastShow(getString(R.string.account_email_no_null));
//                }
//                break;
//
//            case R.id.regist:
//                try {
//                    if (!TextUtils.isEmpty(account.getText().toString())
//                            &&!TextUtils.isEmpty(password.getText().toString())
//                            &&!TextUtils.isEmpty(email.getText().toString())
//                            &&!TextUtils.isEmpty(emailCode.getText().toString())
//                            ){
//                        LoginRegVo loginRegVo=new LoginRegVo();
//                        loginRegVo.setUsUsercode(account.getText().toString());
//                        loginRegVo.setUsNameZh(userName.getText().toString());
//                        loginRegVo.setUsPassword(MD5.getMD5(password.getText().toString()));
//                        loginRegVo.setUsPhone(phone.getText().toString());
//                        loginRegVo.setUsEmail(email.getText().toString());
//                        loginRegVo.setMdn(AppContext.getInstance().getPhoneIMEI());
//                        loginRegVo.setCaptcha(emailCode.getText().toString());
//                        loginRegVo.setAppType(getString(R.string.APP_TYPE));        //项目类型
//                        String json= new Gson().toJson(loginRegVo);
//                        String serverUrl=OkHttpURL.serverUrl;
//                        if (serverUrl==null){
//                            ToastUtils.show(getString(R.string.error_message2));
//                            return;
//                        }
//                        OkHttpUtils.postString()
//                                .url(serverUrl+ OkHttpURL.register)
//                                .content(json)
//                                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                                .build()
//                                .execute(new StringDialogCallback(LoginRegisterActivity.this) {
//                                    @Override
//                                    public void onError(Call call, Exception e, int id) {
//                                        e.printStackTrace();
//                                        final AlertDialog.Builder bd = new AlertDialog.Builder(LoginRegisterActivity.this);
//                                        bd.setTitle(getString(R.string.app_toast));
//                                        bd.setMessage(getString(R.string.error_message));
//                                        bd.setNegativeButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        });
//                                        bd.show();
//                                    }
//
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        Log.i("tag",response);
//                                        ResponseVo responseVo= new Gson().fromJson(response,ResponseVo.class);
//                                        if (responseVo.getState()==1){
//                                            final AlertDialog.Builder bd = new AlertDialog.Builder(LoginRegisterActivity.this);
//                                            bd.setTitle("提示");
//                                            bd.setCancelable(false);
//                                            bd.setMessage(responseVo.getMessage());
//                                            bd.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    viewClean();
//                                                    myCountDownTimer.onStart();
//                                                }
//                                            });
//                                            bd.show();
//                                        }else {
//                                            final AlertDialog.Builder bd = new AlertDialog.Builder(LoginRegisterActivity.this);
//                                            bd.setTitle("提示");
//                                            bd.setCancelable(false);
//                                            bd.setMessage(responseVo.getMessage());
//                                            bd.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                }
//                                            });
//                                            bd.show();
//                                        }
//                                    }
//                                });
//
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    private void viewClean(){
//        userName.setText("");
//        account.setText("");
//        password.setText("");
//        phone.setText("");
//        companyName.setText("");
//        email.setText("");
//        emailCode.setText("");
//    }


}
