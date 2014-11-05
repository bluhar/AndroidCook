package com.example.draganddraw;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoxDrawingView extends View {

    private static final String TAG    = "BoxDrawingView";
    private Box                 mCurrentBox;
    private ArrayList<Box>      mBoxes = new ArrayList<Box>();
    
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    //AttributeSet 来自与XML中定义的属性
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mBoxPaint = new Paint();
        mBoxPaint.setColor(Color.RED);
        
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.CYAN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF curr = new PointF(event.getX(), event.getY());
        Log.i(TAG, "Recevied event at x=" + curr.x + ", y=" + curr.y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, " ACTION_DOWN");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, " ACTION_MOVE");
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(curr);
                    invalidate(); //强制View重新绘制自己
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, " ACTION_UP");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, " ACTION_CANCEL");
                mCurrentBox = null;
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        
        canvas.drawPaint(mBackgroundPaint);
        
        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
//            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
//            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.min(box.getOrigin().y, box.getCurrent().y);
            float top = Math.max(box.getOrigin().y, box.getCurrent().y);
            
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }
    
    
    

}
