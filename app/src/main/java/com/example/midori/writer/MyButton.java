package com.example.midori.writer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Alessandra on 06/10/15.
 */
public class MyButton extends Button {
    public long durationTouch = 500;
    public MyButton actualButton;
    public static long start;
    private boolean isPressed;
    private final Handler handler = new Handler();

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
        actualButton = this;
        Runnable runnable = new Runnable() {
            public void run() {
                if (isPressed) {
                    actualButton.setBackgroundColor(Color.argb(255,153,153,153));
                }

            }
        };
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(runnable, durationTouch);
                isPressed = true;
                start = System.currentTimeMillis();
                System.out.println(event.getAction());
                break;
            case MotionEvent.ACTION_UP:
                actualButton.setBackgroundColor(Color.GRAY);
                if (isPressed) {
                    isPressed = false;
                    handler.removeCallbacks(runnable);
                }
                long duration = (System.currentTimeMillis() - start);
                System.out.println(event.getAction());
                if (duration > durationTouch) {
                    return true;
                }
                break;
            default:
                System.out.println(event.getAction());
        }
        return false;
    }
}

