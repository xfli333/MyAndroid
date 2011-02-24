package cn.lee.android.sinaweibo;

import android.content.Intent;
import cn.lee.android.sinaweibo.helper.WeibOTimeLine;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 下午2:24
 */
public class PublicTimeLineListView extends BaseListView {

    @Override
    protected int getTimeLineType() {
        return WeibOTimeLine.PUBLIC_TYPE;
    }

    @Override
    protected void doSelectItemAction(HashMap<String, Object> item) {
//        try {
//            weibo.createFriendship(item.get("userId").toString());
//            MsgBoxHelper.showMsg(this,"关注成功","");
//        } catch (WeiboException e) {
//            e.printStackTrace();
//        }
        Intent in = new Intent();
        in.setClass(PublicTimeLineListView.this, StatusDetailView.class);
        for(String key : item.keySet()){
            in.putExtra(key,item.get(key).toString());
        }
        startActivity(in);
        overridePendingTransition(R.anim.activity_open_enter, 0);
    }
}
