package com.example.midori.writer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 06/10/15.
 */

//TODO lo schermo non va mai in sleep, quindi non devo aver bisogno di sbloccarlo

public class SafeButton extends Button {
    private List<SafeTapListener> safeTapListeners = new ArrayList<SafeTapListener>();
    private static RootActivity rootActivity;

    //costanti intere per rappresentare le durate del tocco
    public final static int NO_SAFE_TOUCH = 0;
    public final static int SHORT_SAFE_TOUCH = 1;
    public final static int MID_SAFE_TOUCH = 2;
    public final static int LONG_SAFE_TOUCH = 3;

    //costanti long che corrispondo alla durata in millisecondi del tocco
    private final static long SHORT_SAFE_TOUCH_DURATION = 200;
    private final static long MID_SAFE_TOUCH_DURATION = 400;
    private final static long LONG_SAFE_TOUCH_DURATION = 600;

    //variabile long alla quale è associata di default la durata breve
    private static long safeTouchDuration = SHORT_SAFE_TOUCH_DURATION;

    //metodo per settare la durata del tocco
    public static void setSafeTouchLength(int touchDuration) {
        switch (touchDuration) {
            case NO_SAFE_TOUCH:
                safeTouchDuration = 0;
                break;
            case SHORT_SAFE_TOUCH:
                safeTouchDuration = SHORT_SAFE_TOUCH_DURATION;
                break;
            case MID_SAFE_TOUCH:
                safeTouchDuration = MID_SAFE_TOUCH_DURATION;
                break;
            case LONG_SAFE_TOUCH:
                safeTouchDuration = LONG_SAFE_TOUCH_DURATION;
                break;
            default:
                new Exception("Unknown safe touch duration. Check this out").printStackTrace();
        }
    }

    public SafeButton actualButton; //per gestire il pulsante premuto
    public static long start; //tempo inizio
    private boolean isPressed;
    private final Handler handler = new Handler(); //ci serve per gestire la durata del tocco con il runnable

    //inizializza, chiamando a sua volta triggerSafeTap, la lista di listeners
    private void initializeTouchListener() {
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSafeTap(event)) {
                    triggerSafeTap();
                    return true;
                } else return false;
            }
        });
    }

    public SafeButton(Context context) {
        super(context);
        this.initializeTouchListener();
    }

    public SafeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initializeTouchListener();
    }

    public SafeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initializeTouchListener();
    }

    public void setOnSafeTapListener(SafeTapListener stl) {
        safeTapListeners.add(stl);
    }


    public Node getNode() {
        return Tree.getInstance().getNodeFromText((String) this.getText());
    }


    //fa in modo che per ogni listener della lista risulti avvenuto l'evento di tocco e quindi ci sia un break
    private void triggerSafeTap() {
        for (SafeTapListener stl : safeTapListeners) {
            if (stl.onSafeTap(this)) {
                break;
            }
        }
    }


    //metodo per calcolare la durata in base alla costante safeTouchDuration
    //restituisce true se la durata è uguale o maggiore
    private boolean isSafeTap(MotionEvent event) {
        actualButton = this;
        Runnable runnable = new Runnable() {
            public void run() {
                if (isPressed) {
                    if (!Objects.equals(actualButton.getText(), ""))
                        actualButton.setBackgroundColor(Color.argb(255, 46, 170, 171));
                    else
                        actualButton.setClickable(false);
                }

            }
        };
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(runnable, safeTouchDuration);
                isPressed = true;
                start = System.currentTimeMillis();
                System.out.println(event.getAction());
                break;
            case MotionEvent.ACTION_UP:
                actualButton.setBackgroundColor(Color.argb(255, 35, 130, 131));
                if (isPressed) {
                    isPressed = false;
                    handler.removeCallbacks(runnable);
                }
                long duration = (System.currentTimeMillis() - start);
                System.out.println(event.getAction());
                if (duration > safeTouchDuration) {
                    return true;
                }
                break;
            default:
                System.out.println(event.getAction());
        }
        return false;
    }

    public static String configureTouch(LeafNode lf) {
        rootActivity = RootActivity.getInstanceRootActivity();
        rootActivity.getConfigurations().setConfigurations("tocco", (String) lf.getAttribute());
        String toShow;
        switch ((String) lf.getAttribute()) {
            case "Disabilita":
                toShow = "Tocco 'safe' disabilitato.";
                break;
            case "Breve":
                toShow = "Tocco breve impostato.";
                break;
            case "Medio":
                toShow = "Tocoo medio impostato.";
                break;
            case "Lungo":
                toShow = "Tocco lungo impostato.";
                break;
            default:
                toShow = "Errore nella scelta della durata del tocco.";
                break;
        }
        return toShow;
    }
}

