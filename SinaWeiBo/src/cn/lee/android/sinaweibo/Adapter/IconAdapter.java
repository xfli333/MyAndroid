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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 下午4:07
 */
public class IconAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private LayoutInflater mInflater;
    private Map<String, Bitmap> iconCache = new HashMap<String, Bitmap>();
    private Map<String, Bitmap> midPicCache = new HashMap<String, Bitmap>();
    private Map<String, Bitmap> retweetPicCache = new HashMap<String, Bitmap>();

    public IconAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        super();
        this.data = data;
        mInflater = LayoutInflater.from(context);
        iconCache = new HashMap<String, Bitmap>();
        iconCache.clear();
        midPicCache = new HashMap<String, Bitmap>();
        midPicCache.clear();
        retweetPicCache = new HashMap<String, Bitmap>();
        retweetPicCache.clear();
    }

   final class ViewHolder {
        TextView text;
        TextView content;
        TextView userlocation;
        TextView location;
        ImageView icon;
        ImageView midPic;
        TextView retweetText;
        ImageView retweetPic;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listpicrow, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.ItemTitle);
            holder.icon = (ImageView) convertView.findViewById(R.id.widget33);
            holder.content = (TextView) convertView.findViewById(R.id.ItemText);
            holder.location = (TextView) convertView.findViewById(R.id.infos);
            holder.userlocation = (TextView) convertView.findViewById(R.id.userlocation);
            holder.midPic = (ImageView) convertView.findViewById(R.id.midPic);

            holder.retweetText = (TextView) convertView.findViewById(R.id.retweetText);
            holder.retweetPic = (ImageView) convertView.findViewById(R.id.retweetPic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(((HashMap<String, Object>) data.get(position)).get("user").toString());
        holder.content.setText(((HashMap<String, Object>) data.get(position)).get("content").toString());
        holder.location.setText(((HashMap<String, Object>) data.get(position)).get("location").toString());
        holder.userlocation.setText(((HashMap<String, Object>) data.get(position)).get("userlocation").toString());
//        if (!((HashMap<String, Object>)data.get(position)).get("retweetText").equals("")) {
            holder.retweetText.setText(((HashMap<String, Object>) data.get(position)).get("retweetText").toString());
//        }else{
//            holder.retweetText.setBackgroundDrawable(null);
//        }
        String iconurl = ((HashMap<String, Object>) data.get(position)).get("icon").toString();
        String midPicUrl = ((HashMap<String, Object>) data.get(position)).get("midPic").toString();
//        if (((HashMap<String, Object>)data.get(position)).get("retweetPic")!=null) {
            String retweetPic = ((HashMap<String, Object>) data.get(position)).get("retweetPic").toString();
            if (retweetPicCache.containsKey(retweetPic)) {
                holder.retweetPic.setImageBitmap(retweetPicCache.get(retweetPic));
            } else {
                if (retweetPic != null && !"".equals(retweetPic)) {
                    Bitmap value = ImageUtil.returnBitMap(retweetPic);
                    holder.retweetPic.setImageBitmap(value);
                    retweetPicCache.put(retweetPic, value);
                } else {
                    holder.retweetPic.setImageBitmap(null);
                }
            }
              holder.retweetPic.setMaxHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
        if (iconCache.containsKey(iconurl)) {
            holder.icon.setImageBitmap(iconCache.get(iconurl));
        } else {
            Bitmap value = ImageUtil.returnBitMap(iconurl);
            holder.icon.setImageBitmap(value);
            iconCache.put(iconurl, value);
        }

        if (midPicCache.containsKey(midPicUrl)) {
            holder.midPic.setImageBitmap(midPicCache.get(midPicUrl));
        } else {
            if (midPicUrl != null && !"".equals(midPicUrl)) {
                Bitmap value = ImageUtil.returnBitMap(midPicUrl);
                holder.midPic.setImageBitmap(value);
                midPicCache.put(midPicUrl, value);
            } else {
                holder.midPic.setImageBitmap(null);
            }
        }


        return convertView;
    }


}
