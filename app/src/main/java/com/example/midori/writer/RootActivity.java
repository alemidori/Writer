package com.example.midori.writer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class RootActivity extends Activity {
    private static InputStream fileInput;
    private static RootActivity rootActivity;
    private static Context context;
    private static SafeButton selectableButton, nextButton;
    private static TextView topText;
    private static int layoutValue;

    public static InputStream getFileInput() {
        return fileInput;
    }

    public static void setLayoutValue(int layoutValue) {
        RootActivity.layoutValue = layoutValue;
    }

    public static Context getContext() {
        return context;
    }

    public static EditText getInputSection() {
        return inputSection;
    }

    private static EditText inputSection;

    public static SafeButton getNextButton() {
        return nextButton;
    }

    public static SafeButton getSelectableButton() {
        return selectableButton;
    }

    public static TextView getTopText() {
        return topText;
    }

    public static RootActivity getInstanceRootActivity() {
        return rootActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileInput = this.getResources().openRawResource(R.raw.tree_structure);

        rootActivity = this;
        context = this.getApplicationContext();

        selectableButton = (SafeButton) findViewById(R.id.button);
        nextButton = (SafeButton) findViewById(R.id.button2);

        switch (layoutValue) {
            case 1:
                setContentView(R.layout.two_buttons);
                selectableButton = (SafeButton) findViewById(R.id.button);
                nextButton = (SafeButton) findViewById(R.id.button2);
                break;
            case 2:
                setContentView(R.layout.four_buttons);
                nextButton = (SafeButton) findViewById(R.id.button2);
                break;
            case 3:
                setContentView(R.layout.eight_buttons);
                break;
            default:
                setContentView(R.layout.two_buttons);
                selectableButton = (SafeButton) findViewById(R.id.button);
                nextButton = (SafeButton) findViewById(R.id.button2);
                break;
        }


        topText = (TextView) findViewById(R.id.textView);
        inputSection = (EditText) findViewById(R.id.editText);
        MainController.getInstance();

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
