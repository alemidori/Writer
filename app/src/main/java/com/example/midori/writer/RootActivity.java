package com.example.midori.writer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

//Menu dell'applicazione: contiene la visualizzazione di default (2 pulsanti) delle funzionalità principali (Lettere, Frasi e Comdandi)
public class RootActivity extends Activity {

    public MyButton mainButton, configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Tree.populate();

        mainButton = (MyButton) findViewById(R.id.button);
        configButton = (MyButton) findViewById(R.id.button2);
        mainButton.setText((CharSequence) Tree.main.data);
        configButton.setText((CharSequence) Tree.config.data);
        mainButton.setBackgroundColor(Color.GRAY);
        configButton.setBackgroundColor(Color.GRAY);


        mainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mainButton.isSafeTouch(event)) {
                    Intent main = new Intent(v.getContext(), LayoutActivity.class);
                    main.putExtra("activity", 1);
                    startActivity(main);
                }
                return true;
            }
        });

        configButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (configButton.isSafeTouch(event)) {
                    Intent main = new Intent(v.getContext(), LayoutActivity.class);
                    main.putExtra("activity", 2);
                    startActivity(main);
                }
                return true;
            }
        });
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
