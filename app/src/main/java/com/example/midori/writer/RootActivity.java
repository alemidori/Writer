package com.example.midori.writer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RootActivity extends Activity {
    private static InputStream fileInput;
    private static RootActivity rootActivity;
    private static Context context;
    private static SafeButton selectableButton, nextButton;
    private static TextView topText;
    private static int layoutValue;
    private static List<SafeButton> buttonList;

    public static List<SafeButton> getButtonList() {
        return buttonList;
    }

    public static int getLayoutValue() {
        return layoutValue;
    }

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
        buttonList = new ArrayList<>();

        switch (layoutValue) {
            case 1:
                setContentView(R.layout.two_buttons);
                buttonList.clear();
                selectableButton = (SafeButton) findViewById(R.id.button);
                nextButton = (SafeButton) findViewById(R.id.next);
                buttonList.add(selectableButton);
                break;
            case 2:
                setContentView(R.layout.four_buttons);
                buttonList.clear();
                for (int i = 0; i < 3; i++) {
                    String buttonID = "button" + (i + 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    SafeButton newButton = (SafeButton) findViewById(resID);
                    buttonList.add(newButton);
                }
                break;
            case 3:
                setContentView(R.layout.eight_buttons);
                break;
            default:
                setContentView(R.layout.two_buttons);
                buttonList.clear();
                selectableButton = (SafeButton) findViewById(R.id.button);
                nextButton = (SafeButton) findViewById(R.id.next);
                buttonList.add(selectableButton);
                break;
        }


        topText = (TextView) findViewById(R.id.textView);
        inputSection = (EditText) findViewById(R.id.editText);
        System.out.println("LISTA PULSANTI nel root "+buttonList.size());
        new MainController();

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
