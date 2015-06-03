/*
 * File Name: UserBusinessDataHelper.java 
 * History:
 * Created by Administrator on 2015-4-21
 */
package com.eds.supermanc.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * C端数据表 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-21
 */
public class SuperManCDataHelper extends SQLiteOpenHelper {
    public static final String DATA_NAME = "e_daisong_c.db";
    public static final int DATA_VERSION = 1;
    public static final String USER_BUSINESS_TABLE_NAME = "user_business_lib";// 用户与商户关联表

    public SuperManCDataHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SuperManCDataHelper(Context context, String name, CursorFactory factory, int version,
            DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SuperManCDataHelper(Context context) {
        super(context, DATA_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(USER_BUSINESS_TABLE_NAME).append(" ( ")
                .append(UserBusinessColumn._ID).append(" integer primary key autoincrement, ")
                .append(UserBusinessColumn.BUSINESS_ID).append(" varchar(40) ").append(" ) ");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_BUSINESS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_BUSINESS_TABLE_NAME);
        onCreate(db);
    }

    // ==========================================================================
    // Constants
    // ==========================================================================

    // ==========================================================================
    // Fields
    // ==========================================================================

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================

    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
}
