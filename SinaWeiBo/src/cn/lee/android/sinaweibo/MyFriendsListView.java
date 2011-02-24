package cn.lee.android.sinaweibo;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import cn.lee.android.sinaweibo.Adapter.FriendListViewAdapter;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 下午8:34
 */
public class MyFriendsListView extends ListActivity {
    private FriendListViewAdapter adapter;
    private ProgressBar progressBar;

    private ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
    private Weibo weibo = new Weibo(WeiBoHelper.loginName, WeiBoHelper.password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselistview);
        progressBar = (ProgressBar) this.findViewById(R.id.widget108);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        new LoadFriendsThread(this).start();
    }

    private class LoadFriendsThread extends Thread {
        private Context context;

        public LoadFriendsThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            initFriendsToMap(items);
            adapter = new FriendListViewAdapter(context, items);
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    private void initFriendsToMap(ArrayList<HashMap<String, Object>> items) {
        try {
            List<User> friends = weibo.getFriendsStatuses();
            for (User friend : friends) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("friendName", friend.getName());
                map.put("userLocation", friend.getLocation());
                map.put("usericon", friend.getProfileImageURL().toString());
                map.put("friendId", friend.getId());
                items.add(map);
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }


    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            setListAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    };
}
