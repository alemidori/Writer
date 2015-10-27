package com.example.midori.writer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class RootActivity extends Activity {
    private static SafeButton selectableButton;

    public static SafeButton getNextButton() {
        return nextButton;
    }

    private static SafeButton nextButton;
    private static TextView topText;

    public static SafeButton getSelectableButton() {
        return selectableButton;
    }

    public static TextView getTopText() {
        return topText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_buttons); //default

        selectableButton = (SafeButton) findViewById(R.id.button);
        nextButton = (SafeButton) findViewById(R.id.button2);
        topText = (TextView) findViewById(R.id.textView);
        selectableButton.setBackgroundColor(Color.GRAY);
        nextButton.setBackgroundColor(Color.GRAY);
        nextButton.setText("->");

        selectableButton.setText("main");

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
