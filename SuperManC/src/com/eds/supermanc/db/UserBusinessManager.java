/*
 * File Name: UserBusinessManager.java 
 * History:
 * Created by Administrator on 2015-4-21
 */
package com.eds.supermanc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * 操作用户与商户的操作 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-21
 */
public class UserBusinessManager {
    private SuperManCDataHelper dbHelper = null;
    private SQLiteDatabase UserBusinessDB = null;
    private Context context;
    private String tableName = SuperManCDataHelper.USER_BUSINESS_TABLE_NAME;

    public UserBusinessManager(Context con) {
        context = con;
    }

    private void open() {
        if (dbHelper == null) {
            dbHelper = new SuperManCDataHelper(context);
        }
        if (UserBusinessDB == null) {
            UserBusinessDB = dbHelper.getWritableDatabase();
        }
    }

    private void closed() {
        if (UserBusinessDB != null) {
            UserBusinessDB.close();
            UserBusinessDB = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

    /**
     * 添加数据
     * 
     * @param data
     * @return
     */
    public long addUserBusinessInfo(List<String> data) {
        long iResult = 0;
        try {
            if (data == null || data.size() == 0) {
                return iResult;
            } else {
                open();
                UserBusinessDB.beginTransaction();
                StringBuffer sql = new StringBuffer();
                sql.append("insert into ").append(tableName).append(" ( ").append(UserBusinessColumn.BUSINESS_ID)
                        .append(" ) ").append(" values ").append(" ( ").append("?").append(" ) ");
                SQLiteStatement stat = UserBusinessDB.compileStatement(sql.toString());
                for (String sId : data) {
                    stat.bindString(1, sId);
                    iResult = stat.executeInsert();
                    if (iResult <= 0) {
                        break;
                    }
                }
                UserBusinessDB.setTransactionSuccessful();
                UserBusinessDB.endTransaction();
            }
        } catch (Exception e) {

        } finally {
            closed();
        }
        return iResult;
    }

    public List<String> getUserBusinessInfo() {
        List<String> BIdData = new ArrayList<String>();
        Cursor cursor = null;
        this.open();
        try {
            cursor = UserBusinessDB.query(tableName, UserBusinessColumn.PROJECTION_QUEUE_DETAIL, null, null, null,
                    null, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    int icolumnIndex = cursor.getColumnIndex(UserBusinessColumn.BUSINESS_ID);
                    BIdData.add(cursor.getString(icolumnIndex));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
            this.closed();
        }
        return BIdData;
    }

    /*
     * 8 删除全部数据
     */
    public int delUserBusinessInfo() {
        int iResult = -1;
        this.open();
        try {
            iResult = UserBusinessDB.delete(tableName, null, null);
        } catch (Exception e) {

        } finally {
            this.closed();
        }
        return iResult;
    }
}
