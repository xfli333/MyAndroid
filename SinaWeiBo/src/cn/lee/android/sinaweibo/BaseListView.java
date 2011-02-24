package cn.lee.android.sinaweibo;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.lee.android.sinaweibo.Adapter.IconAdapter;
import cn.lee.android.sinaweibo.Utils.WeiBoUtil;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import cn.lee.android.sinaweibo.helper.WeibOTimeLine;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 上午10:07
 */
public abstract class BaseListView extends ListActivity {
    public static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    IconAdapter adapter;
    ProgressBar progressBar;

    ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
    Weibo weibo = new Weibo(WeiBoHelper.loginName, WeiBoHelper.password);

    ImageView back;
    ImageView refreshImg;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselistview);
        progressBar = (ProgressBar) this.findViewById(R.id.widget108);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        context = this;
        back = (ImageView) this.findViewById(R.id.backImg);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseListView.this.finish();
            }
        });
        refreshImg = (ImageView) this.findViewById(R.id.refreshImg);
        refreshImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.VISIBLE);
                new ProgressThread(context).start();

            }
        });
        new ProgressThread(this).start();
    }

    private class ProgressThread extends Thread {
        private Context context;

        public ProgressThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            items = initMap();
            adapter = new IconAdapter(context, items);
            adapter.notifyDataSetChanged();
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            setListAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    };

    protected abstract int getTimeLineType();

    protected abstract void doSelectItemAction(HashMap<String, Object> item);


    private ArrayList<HashMap<String, Object>> initMap() {
        items.clear();
        try {
            List<Status> statuses = new ArrayList<Status>();
            switch (getTimeLineType()) {
                case WeibOTimeLine.PUBLIC_TYPE:
                    statuses = weibo.getPublicTimeline();
                    break;
                case WeibOTimeLine.FRIENDS_TYPE:
                    statuses = weibo.getFriendsTimeline();
                    break;
                case WeibOTimeLine.SELF_TYPE:
                    statuses = weibo.getUserTimeline();
                    break;
            }

            for (Status status : statuses) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("icon", status.getUser().getProfileImageURL().toString());
                map.put("user", "  " + status.getUser().getName());
                map.put("userlocation", "    " + status.getUser().getLocation());
                map.put("content", status.getText());
                map.put("midPic", status.getThumbnail_pic());
                map.put("location", formatter.format(status.getCreatedAt()) + " 来自:" + WeiBoUtil.formatSource(status.getSource()));
                map.put("retweetText", "");
                map.put("retweetPic", "");
                if (status.getRetweetedStatus() != null && !"".equals(status.getRetweetedStatus().toString())) {
                    Status retweetStatus = status.getRetweetedStatus();
                    if (!"".equals(retweetStatus.getText())) {

                        map.put("retweetText", "转发微薄:\r\n\t @" + retweetStatus.getUser().getName() + ":" + retweetStatus.getText());
                    }
                    if (!retweetStatus.getThumbnail_pic().equals("")) {
                        map.put("retweetPic", retweetStatus.getThumbnail_pic());
                    }
                }
                map.put("statusId", status.getId() + "");
                map.put("userId", status.getUser().getId() + "");
                map.put("bmiddlePic", status.getBmiddle_pic());
                items.add(map);
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return items;
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        doSelectItemAction((HashMap) items.get(position));
//        try {
////            weibo.createFriendship(((HashMap) items.get(position)).get("userId").toString());
//            weibo.destroyFriendship(((HashMap) items.get(position)).get("userId").toString());
//        } catch (WeiboException e) {
//
//            e.printStackTrace();
//        }
    }


}
