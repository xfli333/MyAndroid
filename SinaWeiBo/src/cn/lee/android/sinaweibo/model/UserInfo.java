package cn.lee.android.sinaweibo.model;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 上午9:58
 */
public class UserInfo {
    private String loginName;
    private String password;
    private String token;
    private String tokenSecret;
    private String isActivity;//T OR F

    public String getActivity() {
        return isActivity;
    }

    public void setActivity(String activity) {
        isActivity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (loginName != null ? !loginName.equals(userInfo.loginName) : userInfo.loginName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return loginName != null ? loginName.hashCode() : 0;
    }

    public UserInfo() {
        
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }
}
