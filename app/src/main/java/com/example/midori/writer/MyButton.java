package com.example.midori.writer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Alessandra on 06/10/15.
 */
public class MyButton extends Button {
    long start;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public boolean isSafeTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start = System.currentTimeMillis();
                System.out.println(event.getAction());
                break;
            case MotionEvent.ACTION_UP:
                long duration = (System.currentTimeMillis() - start);
                System.out.println(event.getAction());
                if (duration > 300)
                    return true;
                break;
            default:
                System.out.println(event.getAction());
        }
        return false;
    }
}

