package cn.lee.android.sinaweibo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 上午9:47
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "users";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + "(ID integer primary key," +
                "LOGIN_NAME varchar," +
                "PASSWORD varchar," +
                "TOKEN varchar," +
                "TOKENSECRET varchar," +
                "IS_ACT varchar)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
