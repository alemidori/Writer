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
    private static RootActivity rootActivity;
    private InputStream fileInput;
    private Context context;
    private SafeButton lastButton, nextButton;
    private TextView topText;
    private int layoutValue;
    private int numSelectableButtons;
    private List<SafeButton> buttonList;

    public SafeButton getLastButton() {
        return lastButton;
    }

    public List<SafeButton> getButtonList() {
        return buttonList;
    }

    public int getLayoutValue() {
        return layoutValue;
    }

    public InputStream getFileInput() {
        return fileInput;
    }

    public void setLayoutValue(int layoutValue) {
        this.layoutValue = layoutValue;
    }

    public Context getContext() {
        return context;
    }

    public EditText getInputSection() {
        return inputSection;
    }

    private EditText inputSection;

    public SafeButton getNextButton() {
        return nextButton;
    }

    public TextView getTopText() {
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
        numSelectableButtons = 0;
        SafeButton newButton;


        switch (layoutValue) {
            case 1:
                setContentView(R.layout.two_buttons);
                buttonList.clear();
                String bID = "button" + 1;
                int res = getResources().getIdentifier(bID, "id", getPackageName());
                newButton = (SafeButton) findViewById(res);
                nextButton = (SafeButton) findViewById(R.id.next);
                buttonList.add(newButton);
                break;
            case 2:
                setContentView(R.layout.four_buttons);
                buttonList.clear();
                for (int i = 0; i < 3; i++) {
                    String buttonID = "button" + (i + 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    newButton = (SafeButton) findViewById(resID);
                    buttonList.add(newButton);
                    nextButton = (SafeButton) findViewById(R.id.next);
                    numSelectableButtons = buttonList.size();
                }
                break;
            case 3:
                setContentView(R.layout.eight_buttons);
                buttonList.clear();
                for (int i = 0; i < 7; i++) {
                    String buttonID = "button" + (i + 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    newButton = (SafeButton) findViewById(resID);
                    buttonList.add(newButton);
                    nextButton = (SafeButton) findViewById(R.id.next);
                    numSelectableButtons = buttonList.size();
                }
                break;
            default:
                setContentView(R.layout.four_buttons);
                buttonList.clear();
                for (int i = 0; i < 3; i++) {
                    String buttonID = "button" + (i + 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    newButton = (SafeButton) findViewById(resID);
                    buttonList.add(newButton);
                    nextButton = (SafeButton) findViewById(R.id.next);
                    numSelectableButtons = buttonList.size();
                }

//                setContentView(R.layout.two_buttons);
//                buttonList.clear();
//                newButton = (SafeButton) findViewById(R.id.button1);
//                nextButton = (SafeButton) findViewById(R.id.next);
//                buttonList.add(newButton);
//                numSelectableButtons = buttonList.size();

                break;
        }

        String buttonID = "button" + (buttonList.size());
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        lastButton = (SafeButton) findViewById(resID);

        topText = (TextView) findViewById(R.id.textView);
        inputSection = (EditText) findViewById(R.id.editText);

        new MainController();

    }

    public List<TreeNode> spreadInButtons(List<TreeNode> list, int numButt) {
        List<TreeNode> subList;
        if (numButt > 1) {

            //se i selectable sono meno della lista
            if (list.size() >= numButt) {
                for (int j = 0; j < numButt; j++) {
                    buttonList.get(j).setText((CharSequence) list.get(j).data);
                }
            } else {
                //se i selectable sono più della lista
                int i = buttonList.size()-1;
                int j = list.size()-1;
                    while (i>0) {
                        buttonList.get(i).setText((CharSequence) list.get(j).data);
                        i--;
                        j--;
                }

            }

        } else {
            //se il selectable è unico
            buttonList.get(0).setText((CharSequence) list.get(0).data);
        }
        if (list.size() > 1 && list.size() > numButt) {
            subList = list.subList(numButt, list.size());
        } else
            subList = null;

        return subList;
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
