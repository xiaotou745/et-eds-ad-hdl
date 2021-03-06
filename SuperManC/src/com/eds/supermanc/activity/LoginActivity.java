package com.eds.supermanc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.eds.supermanc.Constants;
import com.eds.supermanc.beans.UserVo;
import com.eds.supermanc.utils.ButtonUtil;
import com.eds.supermanc.utils.EtsCLog;
import com.eds.supermanc.utils.MD5;
import com.eds.supermanc.utils.UserTools;
import com.eds.supermanc.utils.VolleyTool;
import com.eds.supermanc.utils.VolleyTool.HTTPListener;
import com.supermanc.R;

/**
 * 登录页面 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-2-28
 */
public class LoginActivity extends BaseActivity implements HTTPListener, OnClickListener {

    private EditText mPhoneNumber;
    private EditText mPassword;
    private TextView mForgetPassword;
    private Button login;
    String phoneNumber, password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        initView();
        initListener();
    }

    private void initView() {
        initTitle();
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mPassword = (EditText) findViewById(R.id.password);
        mForgetPassword = (TextView) findViewById(R.id.forgetPassword);
        login = (Button) findViewById(R.id.login);
    }

    private void initTitle() {
        LinearLayout backLayout = (LinearLayout) this.findViewById(R.id.titleLeftLayout);
        backLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        ((ImageView) this.findViewById(R.id.titleLogo)).setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.titleContent)).setText("登录");
    }

    private void initListener() {
        mForgetPassword.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void login() {
        if (verifyCode()) {
            ButtonUtil.setDisabled(login, this);
            String strPwd = MD5.md5(password);
            Map<String, String> params = new HashMap<String, String>();
            params.put("phoneNo", phoneNumber);
            params.put("passWord", strPwd);
            VolleyTool.post(Constants.LOGIN_URL, params, this, Constants.LOGIN, UserVo.class);
        }
    }

    private boolean verifyCode() {
        phoneNumber = mPhoneNumber.getText().toString();
        password = mPassword.getText().toString();
        if ("".equals(phoneNumber)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "密码长度不能低于6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.forgetPassword:
            Intent intent = new Intent();
            intent.setClass(this, ForgetPasswordActivity.class);
            startActivity(intent);
            break;
        case R.id.login:
            login();
            break;
        default:
            break;
        }
    }

    @Override
    public <T> void onResponse(T t, int requestCode) {
        ButtonUtil.setEnable(login);
        if (requestCode == Constants.LOGIN) {
            UserVo userVo = (UserVo) t;
            EtsCLog.d("login:result[" + userVo.toString() + "]");
            if (userVo.getStatus() == 0) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                userVo.getResult().setUserName(phoneNumber);
                userVo.getResult().setPassword(password);
                UserTools.saveUser(this, userVo);
                // 保存
                UserTools.saveUserBusinessInfo(this, userVo.getResult().getBusibussId());
                preferences.edit().putString(Constants.USER_CITY_NAME_TAG, userVo.getResult().getCity())
                        .putString(Constants.USER_CITY_ID_TAG, userVo.getResult().getCityId()).commit();
                this.setResult(Constants.SET_JPUSH_TAG_RESULT_CODE);
                this.finish();
            } else {
                Toast.makeText(this, userVo.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, int requestCode) {
        ButtonUtil.setEnable(login);
    }

}
