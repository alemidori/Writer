package com.example.midori.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.Objects;

/**
 * Created by Alessandra on 06/10/15.
 */
public class LayoutActivity extends Activity {
    MyButton selectableButton, nextButton;
    TreeNode first, actual;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        selectableButton = (MyButton) findViewById(R.id.button);
        nextButton = (MyButton) findViewById(R.id.button2);

        first = (TreeNode) RootActivity.main.children.get(0);
        actual = first;
        selectableButton.setText((CharSequence) first.data);
        nextButton.setText("->");
        i = 0;

        selectableButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MyButton.isSafeTouch(event)) {
                    if (Objects.equals(selectableButton.getText(), "^")) {
                        actual = actual.parent;
                        selectableButton.setText((CharSequence) actual.data);
                    }
                    if (actual.isInternalNode()) {
                        actual = (TreeNode) actual.children.get(0);
                        selectableButton.setText((CharSequence) actual.data);
                    } else {
                        //action
                    }
                }
                return true;
            }
        });

        nextButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MyButton.isSafeTouch(event)) {
                    TreeNode next;
                    if (Objects.equals(selectableButton.getText(), "^")) {
                        i = 0;
                        next = (TreeNode) RootActivity.main.children.get(i);
                        actual = next;
                        selectableButton.setText((CharSequence) next.data);
                    } else if (i < RootActivity.main.children.size() - 1) {
                        next = (TreeNode) RootActivity.main.children.get(i + 1);
                        actual = next;
                        selectableButton.setText((CharSequence) next.data);
                        i++;
                    } else {
                        selectableButton.setText("^");
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
