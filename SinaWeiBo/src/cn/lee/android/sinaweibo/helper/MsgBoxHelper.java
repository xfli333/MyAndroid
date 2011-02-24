package cn.lee.android.sinaweibo.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 下午5:53
 */
public class MsgBoxHelper {
    public static void showMsg(Context context, String msg, String title) {
        new AlertDialog.Builder(context).setMessage(msg)
                .setTitle(title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        }).show();
    }

    public static void showConfirmMsg(Context context, String msg, String title, final MsgCallBack callBack) {
        new AlertDialog.Builder(context).setMessage(msg).setTitle(title).
                setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.execute();
                        dialog.cancel();
                    }
                }).
                setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }
}
