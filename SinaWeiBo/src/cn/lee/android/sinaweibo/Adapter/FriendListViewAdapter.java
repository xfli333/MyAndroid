package cn.lee.android.sinaweibo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lee.android.sinaweibo.R;
import cn.lee.android.sinaweibo.Utils.ImageUtil;
import cn.lee.android.sinaweibo.helper.MsgBoxHelper;
import cn.lee.android.sinaweibo.helper.MsgCallBack;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-6
 * Time: 下午8:42
 */
public class FriendListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private LayoutInflater mInflater;
    private Map<String, Bitmap> iconCache = new HashMap<String, Bitmap>();
    Weibo weibo = new Weibo(WeiBoHelper.loginName, WeiBoHelper.password);
    private Context context;

    public FriendListViewAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        super();
        this.data = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        iconCache = new HashMap<String, Bitmap>();
        iconCache.clear();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friendsrow, null);
            holder = new ViewHolder();
            holder.usericon = (ImageView) convertView.findViewById(R.id.userIcon);
            holder.friendName = (TextView) convertView.findViewById(R.id.friendName);
            holder.userLocation = (TextView) convertView.findViewById(R.id.friendlocation);
            holder.addAction = (ImageView) convertView.findViewById(R.id.addAction);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.friendName.setText(((HashMap<String, Object>) data.get(position)).get("friendName").toString());
        holder.userLocation.setText(((HashMap<String, Object>) data.get(position)).get("userLocation").toString());
        holder.friendId = ((HashMap<String, Object>) data.get(position)).get("friendId").toString();
        String iconUrl = ((HashMap<String, Object>) data.get(position)).get("usericon").toString();

        if (iconCache.containsKey(iconUrl)) {
            holder.usericon.setImageBitmap(iconCache.get(iconUrl));
        } else {
            Bitmap value = ImageUtil.returnBitMap(iconUrl);
            holder.usericon.setImageBitmap(value);
            iconCache.put(iconUrl, value);
        }
        holder.addAction.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                MsgBoxHelper.showConfirmMsg(context, "确定取消对"+holder.friendName.getText()+"的关注？", "", new MsgCallBack() {
                    public void execute() {
                        try {
                            weibo.destroyFriendship(holder.friendId);
                            refreshList(position);
                        } catch (WeiboException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
        return convertView;
    }

    private void refreshList(int position) {
        data.remove(position);
        this.notifyDataSetChanged();
    }

    final class ViewHolder {
        TextView friendName;
        TextView userLocation;
        ImageView usericon;
        ImageView addAction;
        String friendId;
    }
}
