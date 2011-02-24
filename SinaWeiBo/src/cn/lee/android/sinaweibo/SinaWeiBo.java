package cn.lee.android.sinaweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import cn.lee.android.sinaweibo.helper.MsgBoxHelper;
import cn.lee.android.sinaweibo.helper.UserDBHelper;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import cn.lee.android.sinaweibo.model.UserInfo;
import weibo4j.User;
import weibo4j.Weibo;

public class SinaWeiBo extends Activity {
    /**
     * Called when the activity is first created.
     */
    private UserInfo userInfo;
    EditText userName;
    EditText password;
    CheckBox isAct;
    ProgressBar progressBar;
    Button loginButton;
    Button cancelButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        userName = (EditText) this.findViewById(R.id.widget27);
        password = (EditText) this.findViewById(R.id.widget30);
        isAct = (CheckBox) this.findViewById(R.id.widget31);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        loginButton = (Button) this.findViewById(R.id.widget33);
        cancelButton = (Button) this.findViewById(R.id.widget34);
        new LoadUserNameAndPasswordThread().start();
        addActionToButton();
    }


    private final Handler loadHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (userInfo != null) {
                userName.setText(userInfo.getLoginName());
                password.setText(userInfo.getPassword());
                isAct.setChecked("T".equals(userInfo.getActivity()));
            }
        }
    };

    private final Handler checkHandler = new Handler() {
        public void handleMessage(Message msg) {

            if ("F".equals(msg.getData().getString("PASSED"))) {
                progressBar.setVisibility(View.GONE);
                showErrorMsg();
                enableAllWidget(true);
                return;
            }
            String loginName = userName.getText().toString();
            String pwd = password.getText().toString();
            String isact = isAct.isChecked() ? "T" : "F";
            UserInfo userInfo = checkUserIsExist(userName.getText().toString());
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setLoginName(loginName);
                userInfo.setPassword(pwd);
                userInfo.setActivity(isact);
                saveUserInfoToDB(userInfo);
            } else {
                updateUserInfoAndSetToAct(loginName, pwd, isAct.isChecked());
            }
            enableAllWidget(true);
            progressBar.setVisibility(View.GONE);
            Intent in = new Intent();
            in.setClass(SinaWeiBo.this, MainTabWindow.class);
            WeiBoHelper.setLoginName(loginName);
            WeiBoHelper.setPassword(pwd);
            startActivity(in);
            overridePendingTransition(R.anim.activity_open_enter, 0);
        }
    };


    private void addActionToButton() {
        final Button login = (Button) this.findViewById(R.id.widget33);
        final Button cancel = (Button) this.findViewById(R.id.widget34);
        final EditText userName = (EditText) this.findViewById(R.id.widget27);
        final EditText password = (EditText) this.findViewById(R.id.widget30);
        final CheckBox isAct = (CheckBox) this.findViewById(R.id.widget31);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loginName = userName.getText().toString();
                String pwd = password.getText().toString();
                if ("".equals(loginName) && "".endsWith(pwd)) {
                    showErrorMsg();
                    return;
                }
                enableAllWidget(false);
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.VISIBLE);

                new CheckUserAndNetThread(loginName, pwd).start();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                userName.setText("");
                password.setText("");
                isAct.setChecked(false);
            }
        });
    }

    private class CheckUserAndNetThread extends Thread {
        private String loginName;
        private String password;

        public CheckUserAndNetThread(String loginName, String password) {
            this.loginName = loginName;
            this.password = password;
        }

        @Override
        public void run() {
            String passed = "F";
            try {
                Weibo weibo = new Weibo(loginName, password);
                User user=weibo.verifyCredentials();
                user.isVerified();
                passed = "T";
                WeiBoHelper.setUserId(user.getId());
            } catch (Exception e) {
                passed = "F";
            }
            Message msg = checkHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("PASSED", passed);
            msg.setData(b);
            checkHandler.sendMessage(msg);
        }
    }

    private class LoadUserNameAndPasswordThread extends Thread {
        public LoadUserNameAndPasswordThread() {
        }

        @Override
        public void run() {
            userInfo = selectKeepUserInfo();
            Message msg = loadHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("msgType", "LOAD");
            msg.setData(b);
            loadHandler.sendMessage(msg);
        }
    }

    private void saveUserInfoToDB(UserInfo info) {
        UserDBHelper helper = new UserDBHelper(this);
        helper.saveUserInfo(info);
        helper.close();
    }


    private void enableAllWidget(boolean enabled) {
        userName.setEnabled(enabled);
        password.setEnabled(enabled);
        isAct.setEnabled(enabled);
        loginButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    private void updateUserInfoAndSetToAct(String loginName, String password, boolean keep) {
        UserDBHelper helper = new UserDBHelper(this);
        helper.updatePageKeepPassword(loginName, password, keep);
        helper.close();
    }

    private UserInfo selectKeepUserInfo() {
        UserDBHelper helper = new UserDBHelper(this);
        UserInfo userInfo = helper.selectKeepUserNameAndPwdUser();
        helper.close();
        return userInfo;
    }

    private UserInfo checkUserIsExist(String loginName) {
        UserDBHelper helper = new UserDBHelper(this);
        UserInfo info = helper.selectUserByLoginName(loginName);
        helper.close();
        return info;
    }

    private void showErrorMsg() {
        MsgBoxHelper.showMsg(this, "出错啦", "登录失败，请检查用户名密码和网络。");
    }
}
