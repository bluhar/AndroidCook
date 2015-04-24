package cn.auchan.auchandrive.feng.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconFontTextView extends TextView {

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTypeface(TypeFaceSingleton.getSingleton(context).getIconfont());
    }

}
