package com.serviceindeed.xuebao.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


//http://stackoverflow.com/questions/12114963/detecting-the-scrolling-direction-in-the-adapter-up-down
public class ScrollDetectingListView extends ListView {
    public ScrollDetectingListView(Context context) {
        super(context);
    }

    public ScrollDetectingListView(Context context, AttributeSet attrs) {
        super(context,attrs);
    } 

    public ScrollDetectingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getVerticalScrollOffset() {
        return computeVerticalScrollOffset();
    }
}