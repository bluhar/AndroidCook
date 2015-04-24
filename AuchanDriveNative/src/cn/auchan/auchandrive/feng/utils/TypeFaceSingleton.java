package cn.auchan.auchandrive.feng.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 
 * @author anelm
 * 字体单例
 * Reference:
 * 1. 一个简单的单例其实也挺复杂
 *      http://wuchong.me/blog/2014/08/28/how-to-correctly-write-singleton-pattern/
 */
public class TypeFaceSingleton {

    private volatile static TypeFaceSingleton instance; //声明成 volatile

    private Typeface                          iconfont;

    private TypeFaceSingleton(Context c) {

        this.iconfont = Typeface.createFromAsset(c.getAssets(), "font/iconfont.ttf");
    }

    public static TypeFaceSingleton getSingleton(Context c) {
        if (instance == null) {
            synchronized (TypeFaceSingleton.class) {
                if (instance == null) {
                    instance = new TypeFaceSingleton(c);
                }
            }
        }
        return instance;
    }

    public Typeface getIconfont() {
        return iconfont;
    }

}
