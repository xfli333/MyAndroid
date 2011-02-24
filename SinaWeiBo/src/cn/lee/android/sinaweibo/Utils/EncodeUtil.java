package cn.lee.android.sinaweibo.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-12
 * Time: 上午11:19
 */
public class EncodeUtil {
    public static String encodeToUTF8(String conent){
        try {
            return java.net.URLDecoder.decode(conent,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return conent;
    }
}
