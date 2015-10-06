package com.example.midori.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationActivity extends Activity {

    public static String orient;
    public static String numPuls;

    private long start;
    private List<ConfigurationNode> configs;
    private TextView textView, textView2, textView3;
    private int p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Intent i = getIntent();

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        Button select = (Button) findViewById(R.id.button2);
        Button next = (Button) findViewById(R.id.button3);

        configs = new ArrayList<>();
        configs.add(new ConfigurationNode(1, "a"));
        configs.add(new ConfigurationNode(2, "a"));
        configs.add(new ConfigurationNode(3, "a"));
        configs.add(new ConfigurationNode(1, "b"));
        configs.add(new ConfigurationNode(2, "b"));
        configs.add(new ConfigurationNode(3, "b"));


        p = 0;
        textView.setText("\nConfig n. " + (p + 1) + "/" + String.valueOf(configs.size()));
        textView2.setText(configs.get(p).getDescrNum());
        textView3.setText(configs.get(p).getDescrOrient());

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onTouchEvent(event)) {
                    if (p < configs.size() - 1) {
                        textView.setText("\nConfig n. " + (p + 2) + "/" + String.valueOf(configs.size()));
                        textView2.setText(configs.get(p + 1).getDescrNum());
                        textView3.setText(configs.get(p + 1).getDescrOrient());
                        p++;
                    } else p = 0;
                    textView.setText("\nConfig n. " + (p + 1) + "/" + String.valueOf(configs.size()));
                    textView2.setText(configs.get(p).getDescrNum());
                    textView3.setText(configs.get(p).getDescrOrient());
                }
                return true;
            }
        });

        select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onTouchEvent(event)) {

                    numPuls = textView2.getText().toString();
                    orient = textView3.getText().toString();
                    Intent menu = new Intent(v.getContext(), MenuActivity.class);
                    startActivity(menu);
                }
                return true;
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
