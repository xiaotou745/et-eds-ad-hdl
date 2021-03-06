package com.eds.supermanc.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.eds.supermanc.beans.UserVo;
import com.eds.supermanc.beans.UserVo.User;
import com.eds.supermanc.db.UserBusinessManager;

public class UserTools {

    private static User user = null;

    private static final String USER_SHAREPERFERENCE_NAME = "user_shareperference";

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String STATUS = "status";
    private static final String AMOUNT = "amount";
    private static final String USER_CITY = "user_city";
    private static final String USER_CITY_ID = "user_city_id";

    private static UserVo.User findUser(Context context) {
        if (user == null || "".equals(user.getUserId())) {
            SharedPreferences mShareUserData = context.getSharedPreferences(USER_SHAREPERFERENCE_NAME,
                    Context.MODE_PRIVATE);
            UserVo userVo = new UserVo();
            user = userVo.new User();
            user.setUserId(mShareUserData.getString(USER_ID, ""));
            user.setUserName(mShareUserData.getString(USER_NAME, ""));
            user.setPassword(mShareUserData.getString(PASSWORD, ""));
            user.setStatus(mShareUserData.getInt(STATUS, 0));
            user.setAmount(mShareUserData.getString(AMOUNT, "0"));
            user.setCity(mShareUserData.getString(USER_CITY, ""));
            user.setCityId(mShareUserData.getString(USER_CITY_ID, ""));
        }
        return user;
    }

    public static void saveUser(Context context, UserVo muser) {
        SharedPreferences mShareUserData = context
                .getSharedPreferences(USER_SHAREPERFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = mShareUserData.edit();
        editor.putString(USER_ID, muser.getResult().getUserId());
        editor.putString(USER_NAME, muser.getResult().getUserName());
        editor.putString(PASSWORD, muser.getResult().getPassword());
        editor.putInt(STATUS, muser.getResult().getStatus());
        editor.putString(AMOUNT, muser.getResult().getAmount());
        editor.putString(USER_CITY, muser.getResult().getCity());
        editor.putString(USER_CITY_ID, muser.getResult().getCityId());
        editor.commit();
    }

    public static UserVo.User getUser(Context context) {
        User user = UserTools.findUser(context);
        if (user == null || "".equals(user.getUserId())) {
            return null;
        } else {
            return user;
        }
    }

    public static void clear(Context context) {
        SharedPreferences mShareUserData = context
                .getSharedPreferences(USER_SHAREPERFERENCE_NAME, Context.MODE_PRIVATE);
        if (user != null) {
            user = null;
        }
        Editor editor = mShareUserData.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 加密用户对应的商户信息
     * 
     * @param data
     * @return
     */
    private static List<String> encryptUserBusinessInfo(List<String> data) {
        List<String> eData = new ArrayList<String>();
        for (String strId : data) {
            String strInfo = MD5.md5(strId);
            eData.add(strInfo);
        }
        return eData;
    }

    /**
     * 保存
     * 
     * @param context
     * @param data
     */
    public static void saveUserBusinessInfo(Context context, List<String> data) {
        if (data != null && data.size() > 0) {
            UserBusinessManager mUBManager = new UserBusinessManager(context);
            // 清除数据
            mUBManager.delUserBusinessInfo();
            List<String> eData = encryptUserBusinessInfo(data);
            mUBManager.addUserBusinessInfo(eData);
        }
    }

    /**
     * 获取所有的商户id
     * 
     * @param context
     * @return
     */
    public static Set<String> getUserBusinessInfo(Context context) {
        Set<String> dataSet = new HashSet<String>();
        UserBusinessManager mUBManager = new UserBusinessManager(context);
        dataSet.addAll(mUBManager.getUserBusinessInfo());
        return dataSet;
    }

}
