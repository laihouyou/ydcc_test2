package com.movementinsome.caice.activity;

import com.movementinsome.caice.base.TitleBaseActivity;

/**
 * 找回密码界面
 * Created by zzc on 2017/10/30.
 */

public class ChangPasswordActivity extends TitleBaseActivity {
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
//    @Bind(R.id.account_chang)
//    ClearableEditText account;
//    @Bind(R.id.email)
//    ClearableEditText email;
//    @Bind(R.id.get_email_code)
//    TextView getEmailCode;
//    @Bind(R.id.password)
//    ClearableEditText password;
//    //    @Bind(R.id.password_again)
////    ClearableEditText passwordAgain;
//    @Bind(R.id.change_password)
//    Button changePassword;
//    @Bind(R.id.email_code)
//    ClearableEditText emailCode;
//
//    private MyCountDownTimer myCountDownTimer;
//    private Drawable drawable;
//    private Drawable drawable_yes;
//
//    private boolean is_standard_account = true;
//    private boolean is_standard_email = false;
//
//
//    @Override
//    protected void initTitle() {
//        setTitle(getString(R.string.change_password));
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
//        myCountDownTimer = new MyCountDownTimer(60000, 1000, getEmailCode);
//
//        drawable = ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.input_error);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//
//        drawable_yes = ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.input_correct);
//        drawable_yes.setBounds(0, 0, drawable_yes.getMinimumWidth(), drawable_yes.getMinimumHeight());
//
//        //账号    限制只能数字跟字母, 最大长度为30个字符
//        /**
//         * 监听EditText框中的变化
//         */
////        account.addTextChangedListener(new TextWatcher() {
////            private CharSequence temp;
////            private int editStart;
////            private int editEnd;
////
////            /**
////             * 文本变化之前
////             * @param s
////             * @param start
////             * @param count
////             * @param after
////             */
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                temp = s;
////            }
////
////            /**
////             * 文本变化中
////             * @param s
////             * @param start
////             * @param before
////             * @param count
////             */
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////
////            }
////
////            /**
////             * 文本变化之后
////             * @param s
////             */
////            @Override
////            public void afterTextChanged(Editable s) {
////                editStart = account.getSelectionStart();
////                editEnd = account.getSelectionEnd();
////                if (TextUtils.isEmpty(account.getText().toString())){
////                    if (temp.length() > 30) {   //限制长度
////                        Toast.makeText(AppContext.getInstance(),
////                                "输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
////                                .show();
////                        s.delete(editStart - 1, editEnd);
////                        int tempSelection = editStart;
////                        account.setText(s);
////                        account.setSelection(tempSelection);
////
////                        is_standard_account = false;
////                        account.setCompoundDrawables(null, null, drawable, null);
////                    } else {
////                        account.setCompoundDrawables(null, null, drawable_yes, null);
////                        is_standard_account = true;
////                    }
////                }else {
////                    is_standard_account = false;
////                    account.setCompoundDrawables(null, null, drawable, null);
////                }
////
////            }
////        });
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
//                if (!Lanlngutil.isEmail(s.toString())) {
//                    email.setCompoundDrawables(null, null, drawable, null);
//                    is_standard_email = false;
//                } else {
//                    email.setCompoundDrawables(null, null, drawable_yes, null);
//                    is_standard_email = true;
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected int getContentView() {
//        return R.layout.activity_chang_password;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
//
//    @OnClick({R.id.get_email_code, R.id.change_password})
//    public void onViewClicked(final View view) {
//        switch (view.getId()) {
//            case R.id.get_email_code:   //获取验证码
//                if (!TextUtils.isEmpty(account.getText().toString())
//                        && !TextUtils.isEmpty(email.getText().toString())) {
//                    String serverUrl= OkHttpURL.serverUrl;
//                    if (serverUrl==null){
//                        ToastUtils.show(getString(R.string.error_message2));
//                        return;
//                    }
//                    String url = serverUrl + OkHttpURL.sendPassswordResetCaptcha
//                            + OkHttpParam.usUsercode + "=" + account.getText().toString()
//                            + "&" + OkHttpParam.usEmail + "=" + email.getText().toString();
//                    OkHttpUtils.post()
//                            .url(url)
//                            .build()
//                            .execute(new StringDialogCallback(ChangPasswordActivity.this) {
//                                @Override
//                                public void onError(Call call, Exception e, int id) {
//                                    e.printStackTrace();
//                                }
//
//                                @Override
//                                public void onResponse(String response, int id) {
//                                    Log.i("tag", response);
//                                    ResponseVo responseVo= new Gson().fromJson(response,ResponseVo.class);
//                                    if (responseVo.getState()==1){
//                                        myCountDownTimer.start();
//                                    }
//                                    pToastShow( responseVo.getMessage());
//                                }
//                            });
//
//                } else {
//                    pToastShow( getString(R.string.account_email_no_null));
//                }
//                break;
//            case R.id.change_password:  //修改密码
//                if (is_standard_account) {
//                    if (is_standard_email) {
//                        if (!TextUtils.isEmpty(emailCode.getText().toString())) {
//                            //获取所填的json字符串
//                            String accountStr = account.getText().toString();
//                            String emailStr = email.getText().toString();
//                            String emailCodeStr = emailCode.getText().toString();
//                            String passwordStr = password.getText().toString();
//                            try {
//                                if (!TextUtils.isEmpty(accountStr)
//                                        && !TextUtils.isEmpty(passwordStr)
//                                        && !TextUtils.isEmpty(emailStr)
//                                        && !TextUtils.isEmpty(emailCodeStr)
//                                        ) {
//                                    Map<String, Object> map = new HashMap<>();
//                                    map.put(OkHttpParam.usUsercode, accountStr);
//                                    map.put(OkHttpParam.usPassword, passwordStr);
//                                    map.put(OkHttpParam.usEmail, emailStr);
//                                    map.put(OkHttpParam.captcha, emailCodeStr);
//                                    String json = new Gson().toJson(map);
//                                    String serverUrl=OkHttpURL.serverUrl;
//                                    if (serverUrl==null){
//                                        ToastUtils.show(getString(R.string.error_message2));
//                                        return;
//                                    }
//                                    OkHttpUtils.postString()
//                                            .url(serverUrl + OkHttpURL.passswordReset)
//                                            .content(json)
//                                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                                            .build()
//                                            .execute(new StringDialogCallback(ChangPasswordActivity.this) {
//                                                @Override
//                                                public void onError(Call call, Exception e, int id) {
//                                                    e.printStackTrace();
//                                                    final AlertDialog.Builder bd = new AlertDialog.Builder(ChangPasswordActivity.this);
//                                                    bd.setTitle(getString(R.string.app_toast));
//                                                    bd.setMessage(getString(R.string.error_message));
//                                                    bd.setNegativeButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//
//                                                        }
//                                                    });
//                                                    bd.show();
//                                                }
//
//                                                @Override
//                                                public void onResponse(String response, int id) {
//                                                    Log.i("tag", response);
//                                                    ResponseVo isUpdateVo = new Gson().fromJson(response, ResponseVo.class);
//
//                                                    if (isUpdateVo.getState() == 1) {
//                                                        final AlertDialog.Builder bd = new AlertDialog.Builder(ChangPasswordActivity.this);
//                                                        bd.setTitle("提示");
//                                                        bd.setCancelable(false);
//                                                        bd.setMessage(isUpdateVo.getMessage());
//                                                        bd.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                viewClean();
//                                                                myCountDownTimer.onStart();
//                                                            }
//                                                        });
//                                                        bd.show();
//                                                    } else {
//                                                        final AlertDialog.Builder bd = new AlertDialog.Builder(ChangPasswordActivity.this);
//                                                        bd.setTitle("提示");
//                                                        bd.setCancelable(false);
//                                                        bd.setMessage(isUpdateVo.getMessage());
//                                                        bd.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                            }
//                                                        });
//                                                        bd.show();
//                                                    }
//                                                }
//                                            });
//
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            pToastShow( getString(R.string.code_no_null));
//                        }
//                    } else {
//                        pToastShow( getString(R.string.email_of_account_format));
//                    }
//                } else {
//                    pToastShow( getString(R.string.error_of_account_format));
//                }
//
//                break;
//        }
//    }
//
//    private void viewClean() {
//        account.setText("");
//        password.setText("");
//        email.setText("");
//        emailCode.setText("");
//    }
}