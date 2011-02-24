package cn.lee.android.sinaweibo.Utils;

import java.lang.reflect.Array;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-4
 * Time: 下午5:55
 */
public class WeiBoUtil {

    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }
    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

    public static String formatSource(String source){
        int lastAtagIndex=source.lastIndexOf("</a>");
        int startIndex=source.substring(0,lastAtagIndex).lastIndexOf(">");
        return source.substring(startIndex+1,lastAtagIndex);
    }


}
