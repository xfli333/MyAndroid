package cn.lee.android.sinaweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.lee.android.sinaweibo.Utils.ImageUtil;
import cn.lee.android.sinaweibo.helper.MsgBoxHelper;
import cn.lee.android.sinaweibo.helper.MsgCallBack;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import weibo4j.Comment;
import weibo4j.RetweetDetails;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-7
 * Time: 下午2:15
 */
public class StatusDetailView extends Activity {
    private ImageView detailUserPic;
    private TextView detailItemTitle;
    private TextView detailsUserLocation;
    private TextView detailItemText;
    private TextView detailRetweetText;
    private TextView detailInfos;

    private ImageView detailRetweetPic;
    private ImageView detailMidPic;

    private Button backButton;
    private Button attention;
    private Button commitButton;
    private Button deleteButton;

    ListView listView;
    ProgressBar progressBar;
    private Intent intent;
    Weibo weibo = new Weibo(WeiBoHelper.loginName, WeiBoHelper.password);
    ArrayList<HashMap<String, String>> items;
    SimpleAdapter mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetails);
        this.intent = this.getIntent();
        progressBar = (ProgressBar) this.findViewById(R.id.detailProBar);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        deleteButton = (Button) this.findViewById(R.id.deleteButton);
        deleteButton.setEnabled(false);
        addActionToButtons(this.getIntent());
        initGUIData(this.getIntent());
        progressBar.setVisibility(View.GONE);

        listView = (ListView) this.findViewById(R.id.commentlist);
        items = initMap();
        mSchedule = new SimpleAdapter(StatusDetailView.this, items, R.layout.listrow, new String[]{"commentUser", "commentText"}, new int[]{R.id.commentUser, R.id.commentText});
        listView.setAdapter(mSchedule);
    }

    private void addActionToButtons(final Intent intent) {
        backButton = (Button) this.findViewById(R.id.detailBack);
        attention = (Button) this.findViewById(R.id.attention);
        commitButton = (Button) this.findViewById(R.id.commentButton);
        if ((WeiBoHelper.userId + "").equals(intent.getStringExtra("userId"))) {
            deleteButton.setEnabled(true);
        }
        deleteButton = (Button) this.findViewById(R.id.deleteButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StatusDetailView.this.finish();
            }
        });
        attention.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    weibo.createFriendship(intent.getStringExtra("userId"));
                    MsgBoxHelper.showMsg(StatusDetailView.this, "关注成功!", "");
                } catch (WeiboException e) {
                    e.printStackTrace();
                }

            }
        });
        commitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopWindow();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MsgBoxHelper.showConfirmMsg(StatusDetailView.this, "删除这条微薄?", "确认", new MsgCallBack() {
                    public void execute() {
                        deleteThisStatus();
                        StatusDetailView.this.finish();
                    }
                });

            }
        });
    }

    private void deleteThisStatus() {
        try {
            weibo.destroyStatus(Long.valueOf(intent.getStringExtra("statusId")));
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private void showPopWindow() {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View music_popunwindwow = mLayoutInflater.inflate(R.layout.writecomment, null);
        final PopupWindow mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        View slef = mLayoutInflater.inflate(R.layout.viewdetails, null);
        mPopupWindow.showAsDropDown(slef);
        Button btnOK = (Button) music_popunwindwow.findViewById(R.id.commentButton);
        final EditText comement = (EditText) music_popunwindwow.findViewById(R.id.commentContent);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
//                    weibo.updateComment(comement.getText().toString(), intent.getStringExtra("statusId"), null);
//                   for(Comment comment : weibo.getComments(intent.getStringExtra("statusId"))){
//                       System.out.println("111"+comment.getUser().getName());
//                       System.out.println("222"+comment.getText());
//                   }
                    String msg = "";
                    for (RetweetDetails rd : weibo.getRetweets(Long.valueOf(intent.getStringExtra("statusId")))) {
                        msg += weibo.showStatus(rd.getRetweetId()).getText();
                    }
                    MsgBoxHelper.showMsg(StatusDetailView.this, "评论成功!" + msg, "");
                } catch (WeiboException e) {
                    e.printStackTrace();
                }
                mPopupWindow.dismiss();
            }
        });
        Button backButton = (Button) music_popunwindwow.findViewById(R.id.commentBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    private void initGUIData(Intent intent) {
        detailUserPic = (ImageView) this.findViewById(R.id.detailUserPic);
        detailItemTitle = (TextView) this.findViewById(R.id.DetailItemTitle);
        detailsUserLocation = (TextView) this.findViewById(R.id.detailsUserLocation);
        detailItemText = (TextView) this.findViewById(R.id.DetailItemText);
        detailRetweetText = (TextView) this.findViewById(R.id.detailRetweetText);
        detailInfos = (TextView) this.findViewById(R.id.detailInfos);
        detailRetweetPic = (ImageView) this.findViewById(R.id.detailRetweetPic);
        detailMidPic = (ImageView) this.findViewById(R.id.detailMidPic);

        detailItemTitle.setText(intent.getStringExtra("user"));
        detailsUserLocation.setText(intent.getStringExtra("userlocation"));
        detailItemText.setText("\t" + intent.getStringExtra("content"));
        detailRetweetText.setText(intent.getStringExtra("retweetText"));
        detailInfos.setText(intent.getStringExtra("location"));

        detailUserPic.setImageBitmap(ImageUtil.returnBitMap(intent.getStringExtra("icon")));
        if (intent.getStringExtra("bmiddlePic") != null && !"".equals(intent.getStringExtra("bmiddlePic"))) {
            detailMidPic.setImageBitmap(ImageUtil.returnBitMap(intent.getStringExtra("bmiddlePic")));
        }
        if (intent.getStringExtra("retweetPic") != null && !"".equals(intent.getStringExtra("retweetPic"))) {
            detailRetweetPic.setImageBitmap(ImageUtil.returnBitMap(intent.getStringExtra("retweetPic")));
        }

//        new InitCommentListDataThread(intent.getStringExtra("statusId")).start();
    }

    private ArrayList<HashMap<String, String>> initMap() {
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

        try {
            for (Comment comment : weibo.getComments(intent.getStringExtra("statusId"))) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("commentUser", comment.getUser().getName()+"  评论:");
                map.put("commentText", "\t\t"+comment.getText());
                items.add(map);
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }

        return items;
    }

    class InitCommentListDataThread extends Thread {
        private String statusId;

        public InitCommentListDataThread(String statusId) {
            this.statusId = statusId;
        }

        @Override
        public void run() {
            initMap();
            mSchedule.notifyDataSetChanged();
        }


    }
}
