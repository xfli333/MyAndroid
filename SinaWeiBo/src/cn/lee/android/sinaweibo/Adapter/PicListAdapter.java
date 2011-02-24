package cn.lee.android.sinaweibo.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lee.android.sinaweibo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-11
 * Time: 下午8:21
 */
public class PicListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private Cursor c;
    private LayoutInflater mInflater;

    public PicListAdapter(Context context, Cursor c) {
        mInflater = LayoutInflater.from(context);
        data = initMap(c);
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
            convertView = mInflater.inflate(R.layout.piclistrow, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.picName);
            holder.pic = (ImageView) convertView.findViewById(R.id.picShow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText("点我选择图片");
//        holder.name.setText(((HashMap<String, Object>) data.get(position)).get("name").toString());
        holder.pic.setImageBitmap((Bitmap) ((HashMap<String, Object>) data.get(position)).get("pic"));
        return convertView;
    }

    private ArrayList<HashMap<String, Object>> initMap(Cursor c) {
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
//    
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            items.add(map);
            int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = c.getString(column_index);
            Bitmap imageBitmap = BitmapFactory.decodeFile(path);
            map.put("pic", imageBitmap);
            map.put("name", path.substring(path.lastIndexOf("/"), path.length()));
        }
        return items;

    }

    final class ViewHolder {
        TextView name;
        ImageView pic;
    }
}
