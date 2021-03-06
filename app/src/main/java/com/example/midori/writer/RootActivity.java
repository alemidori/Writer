package com.example.midori.writer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RootActivity extends Activity {
    private String contentInputSection;
    private Configurations configurations;
    private String toccoDefault, audioDefault;
    private static RootActivity rootActivity;
    private Context context;
    private TextView textView;
    private SafeButton lastButton, nextButton;
    private static int layoutValue;
    private List<SafeButton> buttonList;


    public TextView getTextView() {
        return textView;
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public String getTocco() {
        return toccoDefault;
    }

    public String getAudio() {
        return audioDefault;
    }

    public SafeButton getLastButton() {
        return lastButton;
    }

    public List<SafeButton> getButtonList() {
        return buttonList;
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

        System.out.println("Audio default " + audioDefault);

        if (!configurations.preferences.contains("layout")) {
            String layoutValueString = configurations.setConfigurations("layout", "1");
            layoutValue = Integer.parseInt(layoutValueString);
        } else
            layoutValue = Integer.parseInt(configurations.preferences.getString("layout", "1"));

        System.out.println("layout default " + layoutValue);

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

        inputSection = (EditText) findViewById(R.id.editText);

        Typeface font = Typeface.createFromAsset(getAssets(), "Abel-Regular.ttf");

        for (SafeButton sb : buttonList) {
            sb.setTypeface(font);
        }
        textView = (TextView) findViewById(R.id.textView);
        inputSection.setTypeface(font);
        inputSection.setText(contentInputSection);
        nextButton.setTypeface(font);
        textView.setTypeface(font);
        inputSection.setOnTouchListener(otl);


        MainController.getInstance().initialize();

    }

    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    public void configureLayout(LeafNode lf) {
        try {
            contentInputSection = inputSection.getText().toString();

            if (lf.getAttribute().toString().startsWith("due")) {
                layoutValue = 1;
            } else if (lf.getAttribute().toString().startsWith("quattro")) {
                layoutValue = 2;
            } else if (lf.getAttribute().toString().startsWith("otto")) {
                layoutValue = 3;
            } else {
                layoutValue = 1;
            }

            String path = getContext().getFilesDir().getAbsolutePath()+ "/"+lf.getAttribute();

            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(path)));
            Tree.getInstance().parseJSON(reader);
            //Tree.getInstance().setParentsOfFrasi();
            MainController.getInstance().setNameFileJson(((String)lf.getAttribute()));

            configurations.setConfigurations("layout", String.valueOf(layoutValue));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.recreate();

    }

    //distribuisce gli elementi della lista nei pulsanti in base al layout
    public List<TreeNode> spreadInButtons(List<TreeNode> list, int numButt) {
        for (SafeButton b : buttonList) {
            b.setClickable(true);
        }
        List<TreeNode> subList;
        lastButton.setBackground(getDrawable(R.drawable.rounded_button));
        if (numButt > 1) {
            //se i selectable sono meno della lista
            if (list.size() >= numButt) {
                int i = 0;
                while (i < numButt) {
                    buttonList.get(i).setText((CharSequence) list.get(i).data);
                    i++;
                }
                nextButton.setText("Avanti");
                nextButton.setBackground(getDrawable(R.drawable.rounded_button_last));
            } else {
                //se i selectable sono più della lista
                for (SafeButton b : buttonList) {
                    b.setText("");
                }
                int i = numButt - 2;
                int j = list.size() - 1;

                while (i >= 0 && j >= 0) {
                    buttonList.get(i).setText((CharSequence) list.get(j).data);
                    i--;
                    j--;
                }

                if (Objects.equals(list.get(0).parent.data, "root")) {
                    lastButton.setText("");
                    nextButton.setText("");
                    nextButton.setBackground(getDrawable(R.drawable.rounded_button));
                } else {
                    String nomeMenuPrec = (String) list.get(0).parent.parent.data;
                    if (Objects.equals(nomeMenuPrec, "root")) {
                        lastButton.setText("Torna al Menu principale");
                        nextButton.setText("Avanti");

                    } else {
                        if (Objects.equals(textView.getText(), "Inserisci indirizzo e-mail")) {
                            lastButton.setText("Vai a Invia e-mail");
                            nextButton.setText("Avanti");
                        } else if (Objects.equals(textView.getText(), "Inserisci numero di telefono")) {
                            lastButton.setText("Vai a Invia sms");
                            nextButton.setText("Avanti");
                        } else
                            lastButton.setText("Torna a " + nomeMenuPrec);
                    }

                    System.out.println("PRIMO ELEMENTO LISTA " + list.get(0).data);
                    if (list.get(0).parent.children.size() < numButt) {
                        nextButton.setText("");
                        nextButton.setBackground(getDrawable(R.drawable.rounded_button));
                    } else
                        nextButton.setText("Avanti");
                    lastButton.setBackground(getDrawable(R.drawable.rounded_button_last));
                }
            }

        } else {
            //se il selectable è unico
            buttonList.get(0).setText((CharSequence) list.get(0).data);
        }
        if (list.size() > 1 && list.size() > numButt) {
            subList = list.subList(numButt, list.size());
        } else {
            subList = null;
        }

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