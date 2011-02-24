package cn.lee.android.sinaweibo;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 上午9:52
 */
public class MainTabWindow extends TabActivity {
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.tabwindow);
//         getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlelayout);

        tabHost = getTabHost();
//        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("TAB 1").setContent(R.id.widget30));

        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("主页", getResources().getDrawable(R.drawable.home)).setContent(new Intent(this, FriendsTimeLineListView.class)));
        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("大厅", getResources().getDrawable(R.drawable.dating)).setContent(new Intent(this, PublicTimeLineListView.class)));
        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("@我", getResources().getDrawable(R.drawable.me)).setContent(new Intent(this, MyTimeLineListView.class)));
        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("@关注", getResources().getDrawable(R.drawable.friends)).setContent(new Intent(this, MyFriendsListView.class)));
        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("写围脖", getResources().getDrawable(R.drawable.write)).setContent(new Intent(this, WriteWeiBoView.class)));
        final TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            final TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.YELLOW);
        }
        tabHost.setCurrentTab(2);
    }
}
