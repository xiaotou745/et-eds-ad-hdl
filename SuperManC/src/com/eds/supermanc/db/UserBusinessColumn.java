/*
 * File Name: UserBusinessColumn.java 
 * History:
 * Created by Administrator on 2015-4-21
 */
package com.eds.supermanc.db;

import android.provider.BaseColumns;

/**
 * 用户商家关联 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-21
 */
public class UserBusinessColumn implements BaseColumns {

    public static final String BUSINESS_ID = "business_id";// 用户id

    public static final String[] PROJECTION_QUEUE_DETAIL = { _ID, BUSINESS_ID };
}
