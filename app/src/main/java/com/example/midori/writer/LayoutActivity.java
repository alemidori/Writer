package com.example.midori.writer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

/**
 * Created by Alessandra on 06/10/15.
 */
public class LayoutActivity extends Activity {
    TreeNode activ;
    TextView topText;
    SafeButton selectableButton, nextButton;
    TreeNode first, actual;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent rootActivity = getIntent();
        final int activity = rootActivity.getExtras().getInt("activity");


        selectableButton = (SafeButton) findViewById(R.id.button);
        nextButton = (SafeButton) findViewById(R.id.button2);
        topText = (TextView) findViewById(R.id.textView);
        selectableButton.setBackgroundColor(Color.GRAY);
        nextButton.setBackgroundColor(Color.GRAY);

        first = (TreeNode) activ.children.get(0);
        actual = first;
        selectableButton.setText((CharSequence) actual.data);
        topText.setText((CharSequence) actual.parent.data);
        nextButton.setText("->");
        i = 0;

        selectableButton.setOnSafeTapListener(new SafeTapListener() {

            @Override
            public boolean onSafeTap(SafeButton safeButton) {

                if (Objects.equals(selectableButton.getText(), "^")) {

                    if (Objects.equals(topText.getText().toString(), "main") || Objects.equals(topText.getText().toString(), "config")) {
                        Intent backToRoot = new Intent(LayoutActivity.this, RootActivity.class);
                        startActivity(backToRoot);
                    } else {
                        topText.setText(topText.getText().toString().replace(" > " + actual.parent.data, ""));
                    }
                    actual = actual.parent;
                    i = actual.parent.children.indexOf(actual);
                    Log.d("1", (String) actual.data);
                    selectableButton.setText((CharSequence) actual.data);

                } else if (actual.isInternalNode()) {
                    if (Objects.equals(actual.data, "main") || Objects.equals(actual.data, "config"))
                        topText.setText((CharSequence) actual.data);
                    else
                        topText.append(" > " + actual.data);
                    actual = (TreeNode) actual.children.get(0);
                    i = 0;
                    Log.d("1", (String) actual.data);
                    selectableButton.setText((CharSequence) actual.data);
                } else {

                }
                return true;
            }

        });

        nextButton.setOnSafeTapListener(new SafeTapListener()

                                        {

                                            @Override
                                            public boolean onSafeTap(SafeButton safeButton) {


                                                TreeNode next;
                                                if (Objects.equals(selectableButton.getText(), "^")) {
                                                    i = 0;
                                                    next = (TreeNode) actual.parent.children.get(i);
                                                    actual = next;
                                                    selectableButton.setText((CharSequence) actual.data);
                                                } else if (i < actual.parent.children.size() - 1) {
                                                    Log.d("1", (String) actual.parent.data);
                                                    i++;
                                                    next = (TreeNode) actual.parent.children.get(i);
                                                    actual = next;
                                                    selectableButton.setText((CharSequence) actual.data);
                                                } else {
                                                    selectableButton.setText("^");
                                                }


                                                return true;
                                            }
                                        }

        );
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
