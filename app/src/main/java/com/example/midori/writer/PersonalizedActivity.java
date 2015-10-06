package com.example.midori.writer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Alessandra on 06/10/15.
 */
public class PersonalizedActivity extends LayoutActivity {

   public MyButton mainButton,configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        TreeNode root = new TreeNode("root");
        TreeNode main = root.addChild("main");
        TreeNode config = root.addChild("config");

        mainButton = (MyButton) findViewById(R.id.button);
        configButton = (MyButton) findViewById(R.id.button2);
        mainButton.setText((CharSequence) main.data);
        configButton.setText((CharSequence) config.data);

//        configButton.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (configButton.isSafeTouch(event)) {
//                    Intent conf = new Intent(v.getContext(), ConfigurationActivity.class);
//                    startActivity(conf);
//                }
//                return true;
//            }
//        });
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
