package com.example.midori.writer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alessandra on 06/10/15.
 */
public class ConfigActivity extends Activity {
    MyButton selectableButton,nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        selectableButton = (MyButton) findViewById(R.id.button);
        nextButton = (MyButton) findViewById(R.id.button2);

        TreeNode first = (TreeNode) RootActivity.config.children.get(0);
        selectableButton.setText((CharSequence) first.data);
        nextButton.setText("->");

        selectableButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MyButton.isSafeTouch(event)) {
                    //da definire
                }
                return true;
            }
        });

        nextButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MyButton.isSafeTouch(event)) {
                    int i;
                    for(i=0;i<RootActivity.config.children.size(); i++){

                    }

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
