package cn.lee.android.sinaweibo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.lee.android.sinaweibo.model.UserInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 上午9:56
 */
public class UserDBHelper {
    private static String DB_NAME = "sinaweibo.db";
    //数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public UserDBHelper(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public Long saveUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put("LOGIN_NAME", user.getLoginName());
        values.put("PASSWORD", user.getPassword());
        values.put("TOKEN", user.getToken());
        values.put("TOKENSECRET", user.getTokenSecret());
        values.put("IS_ACT", user.getActivity());
        Long uid = db.insert(DBHelper.TB_NAME, "ID", values);
        return uid;
    }

    public UserInfo selectUserByLoginName(String loginName) {
        String hql = "select * from " + DBHelper.TB_NAME + " where LOGIN_NAME = ?";
        Cursor cursor = db.rawQuery(hql, new String[]{loginName});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setLoginName(loginName);
                userInfo.setPassword(cursor.getString(2));
                userInfo.setToken(cursor.getString(3));
                userInfo.setTokenSecret(cursor.getString(4));
                userInfo.setActivity(cursor.getString(5));
                cursor.close();
                return userInfo;
            }
            cursor.close();
        }

        return null;
    }

    public UserInfo selectKeepUserNameAndPwdUser() {
        String hql = "select * from " + DBHelper.TB_NAME + " where IS_ACT = ?";
        Cursor cursor = db.rawQuery(hql, new String[]{"T"});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setLoginName(cursor.getString(1));
                userInfo.setPassword(cursor.getString(2));
                userInfo.setToken(cursor.getString(3));
                userInfo.setTokenSecret(cursor.getString(4));
                userInfo.setActivity(cursor.getString(5));
                cursor.close();
                return userInfo;
            }
            cursor.close();
        }

        return null;
    }

    public void updatePageKeepPassword(String loginName, String password, boolean keep) {
        updateAllActFlagToFalse();
        ContentValues values = new ContentValues();
        values.put("PASSWORD", password);
        if (keep) {
            values.put("IS_ACT", "T");
        }
        db.update(DBHelper.TB_NAME, values, "LOGIN_NAME='" + loginName + "'", null);
    }

    public void updateAllActFlagToFalse() {
        ContentValues values = new ContentValues();
        values.put("IS_ACT", "F");
        db.update(DBHelper.TB_NAME, values, "IS_ACT='T'", null);
    }

    //删除users表的记录
    public int deleteUserInfo(String loginName) {
        int id = db.delete(DBHelper.TB_NAME, "LOGIN_NAME" + "='" + loginName + "'", null);
        return id;
    }
}
