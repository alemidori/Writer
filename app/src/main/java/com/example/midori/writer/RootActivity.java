package com.example.midori.writer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class RootActivity extends Activity {
    private Configurations configurations;
    private String toccoDefault, audioDefault;
    private static RootActivity rootActivity;
    private InputStream fileInput;
    private InputStream fileFrasi;
    private InputStream fileFrasiJson;
    private Context context;
    private SafeButton lastButton, nextButton;
    private TextView topText;
    private static int layoutValue;
    private List<SafeButton> buttonList;
    private OutputStream fileFrasiOut;

    public Configurations getConfigurations() {
        return configurations;
    }

    public String getTocco() {
        return toccoDefault;
    }

    public String getAudio() {
        return audioDefault;
    }

    public static void setLayoutValue(int layoutValue) {
        RootActivity.layoutValue = layoutValue;
    }

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

    public InputStream getFileFrasi() {
        return fileFrasi;
    }

    public InputStream getFileFrasiJson() {
        return fileFrasiJson;
    }


    public OutputStream getFileFrasiOut() {
        return fileFrasiOut;
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

        rootActivity = this;
        context = this.getApplicationContext();

        configurations = new Configurations();
        if (!configurations.preferences.contains("tocco"))
            toccoDefault = configurations.setConfigurations("tocco", "breve");
        else
            toccoDefault = configurations.preferences.getString("tocco", "breve");

        System.out.println("Tocco default " + toccoDefault);

        if (!configurations.preferences.contains("audio"))
            audioDefault = configurations.setConfigurations("audio", "basso");
        else
            audioDefault = configurations.preferences.getString("audio", "basso");

        System.out.println("Audio default "+audioDefault);

        if (!configurations.preferences.contains("layout")) {
            String layoutValueString = configurations.setConfigurations("layout", "1");
            layoutValue = Integer.parseInt(layoutValueString);
        } else
            layoutValue = Integer.parseInt(configurations.preferences.getString("layout", "1"));

        System.out.println("layout default "+layoutValue);


        fileInput = this.getResources().openRawResource(R.raw.tree_structure);
        fileFrasi = this.getResources().openRawResource(R.raw.frasi);
        fileFrasiJson =this.getResources().openRawResource(R.raw.frasi);
        try {
            fileFrasiOut = new FileOutputStream(this.getResources().getResourcePackageName(R.raw.frasi));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        buttonList = new ArrayList<>();
        System.out.println("puls " + layoutValue);
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

                }
                break;
            default:

                setContentView(R.layout.two_buttons);
                buttonList.clear();
                newButton = (SafeButton) findViewById(R.id.button1);
                nextButton = (SafeButton) findViewById(R.id.next);
                buttonList.add(newButton);


                break;
        }

        String buttonID = "button" + (buttonList.size());
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        lastButton = (SafeButton) findViewById(resID);

        topText = (TextView) findViewById(R.id.textView);
        inputSection = (EditText) findViewById(R.id.editText);

        MainController.getInstance().initialize();

    }

    public List<TreeNode> spreadInButtons(List<TreeNode> list, int numButt) {
        List<TreeNode> subList;
        if (numButt > 1) {

            //se i selectable sono meno della lista
            if (list.size() >= numButt) {
                int i = 0;
                while (i < numButt) {
                    buttonList.get(i).setText((CharSequence) list.get(i).data);
                    i++;
                }

            } else {
                for (SafeButton b : buttonList) {
                    b.setText("");
                }
                //se i selectable sono più della lista
                int i = numButt - 2;
                int j = list.size() - 1;
                lastButton.setText("^");
                while (i >= 0 && j >= 0) {
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
