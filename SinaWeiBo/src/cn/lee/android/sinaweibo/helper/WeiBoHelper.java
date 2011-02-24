package cn.lee.android.sinaweibo.helper;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 下午2:00
 */
public class WeiBoHelper {
    public static String loginName;
    public static String password;
    public static int userId;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        WeiBoHelper.userId = userId;
    }

    public static String getLoginName() {
        return loginName;
    }

    public static void setLoginName(String loginName) {
        WeiBoHelper.loginName = loginName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        WeiBoHelper.password = password;
    }
}
