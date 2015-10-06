package com.example.midori.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Alessandra on 30/09/15.
 */
public class MenuActivity extends Activity {
    long start;
    TextView conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent i = getIntent();

        conf = (TextView) findViewById(R.id.textView4);

        conf.setText("Numero pulsanti: " + ConfigurationActivity.numPuls +
        '\n' + "Orientamento: " + ConfigurationActivity.orient);
    }

    //calcola la durata del tocco
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long duration = (System.currentTimeMillis() - start);
                if (duration > 300)
                    return true;
        }
        return false;
    }

}
