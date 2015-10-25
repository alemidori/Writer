package com.example.midori.writer;

import android.util.Log;

import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {

    //controlla tutto il comportamento del sistema; c'è un unico safebutton che gestisce tutti i tocchi;
    //è controllato tutto a livello inferiore

    private SafeButton button;
    private Node node;

    public MainController(){
        button.setOnSafeTapListener(this);
    }

    @Override
    public boolean onSafeTap(SafeButton safeButton) {
        if(button.getNode().isInternal()){

        }
//        else doAction(button.getNode());
        return false;
    }


//    private void doAction(LeafNode lf) {
//        switch (ln.getAction()) {
//            case ACTION_INSERT_TEXT:
//                if (!(ln.getAttribute() instanceof String)) --> ti incazzi come una biscia;
//                String textToAdd = (String) ln.getAttribute();
//                this (cioè il main controller). addtexttolabel(textToadd);
//                break;
//            case ACTION_SET_TOUCH_DURATION:
//                if (!(ln.getAttribute() instanceof Integer)) --> ti incazzi come una biscia;
//                Integer index = (Integer) ln.getAttribute();
//                SafeButton.setSafeTouchLength(index);
//                break;
//        }
//    }

}
