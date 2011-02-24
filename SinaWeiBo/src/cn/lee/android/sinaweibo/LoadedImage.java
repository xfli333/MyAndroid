package cn.lee.android.sinaweibo;

import android.graphics.Bitmap;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-12
 * Time: 上午9:11
 */
public  class LoadedImage {
    Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}

