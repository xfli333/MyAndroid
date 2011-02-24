package cn.lee.android.sinaweibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.lee.android.sinaweibo.helper.MsgBoxHelper;
import cn.lee.android.sinaweibo.helper.WeiBoHelper;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.BASE64Encoder;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 下午5:13
 */
public class WriteWeiBoView extends Activity {
    Cursor cursor;
    private ImageView imageView;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeweibowiew);
        imageView = (ImageView) this.findViewById(R.id.disPic);
        addActionToButton();

//  取得所有图片代码
//        String[] proj = {MediaStore.Images.Media.DATA};
//        cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                proj, null, null, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            System.out.println(cursor.getString(column_index));
//        }


    }

    private void addActionToButton() {
        final EditText content = (EditText) this.findViewById(R.id.writecontent);
        final Button submit = (Button) this.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    submit.setEnabled(false);
//                    Weibo weibo = new Weibo("xfli6786@sina.com", "lxf2000");
//                    Weibo weibo = new Weibo("xfli333@163.com", "982911");
                    Weibo weibo = new Weibo(WeiBoHelper.loginName, WeiBoHelper.password);
                    weibo.setToken("be323bc5c3b0958a7894e8b3574eb37e", "baebe005b0c65743bc31eca13b617e04");
                    String weiboContent=content.getText().toString();
                    if (filePath == null) {
                        weibo.updateStatus(weiboContent);
                    } else {
                        File image = new File(filePath);
                        weibo.uploadStatus(weiboContent, image);
                    }
                    MsgBoxHelper.showMsg(WriteWeiBoView.this,"发布成功啦","提示");
                    filePath=null;
                    imageView.setImageBitmap(null);
                    content.setText("");
                } catch (WeiboException te) {
                    System.out.println("Failed to get timeline: " + te.getMessage());
                } catch (Exception ioe) {
                    System.out.println("Failed to read the system input.");
                }finally {
                     submit.setEnabled(true);
                }
            }
        });

        Button addPicButton = (Button) this.findViewById(R.id.addPic);
        addPicButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showPopWindow();
            }
        });
        Button delPicButton = (Button) this.findViewById(R.id.delPic);
        delPicButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                filePath = null;
                imageView.setImageBitmap(null);
            }
        });
    }

    private void showPopWindow() {

//        String[] projection = {MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME};
//        Uri uri = MediaStore.Images.Thumbnails.getContentUri("external");
//        Cursor c = MediaStore.Images.Thumbnails.queryMiniThumbnails(getContentResolver(), uri, MediaStore.Images.Thumbnails.MINI_KIND, null);
//        String[] proj = {MediaStore.Images.Media.DATA};
//        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                proj, null, null, null);

        Intent in = new Intent();
        in.setClass(WriteWeiBoView.this, LoadImagesFromSDCardActivity.class);
        startActivityForResult(in, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE)//请求码
        {
            if (resultCode == CALL_REQUEST)//返回码
            {
                System.out.println(data.getDataString());
                this.filePath = data.getDataString();
                this.imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            }
        }
    }

    static final int REQUEST_CODE = 0;
    static final int CALL_REQUEST = 1;
}
