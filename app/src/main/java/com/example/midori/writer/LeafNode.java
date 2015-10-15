package com.example.midori.writer;

/**
 * Created by Alessandra on 06/10/15.
 */
public class LeafNode {
    private int action;
    private Object attribute;

    public static int ACTION_INSERT_TEXT        = 0;
    public static int ACTION_SET_TOUCH_DURATION = 1;

    public LeafNode(int action, Object attribute) {
        this.action = action;
        this.attribute = attribute;
    }

    public int getAction() {
        return action;
    }

    public Object getAttribute() {
        return attribute;
    }

}
/*
Nel controller
    public void doAction(LeafNode lf) {
        switch (ln.getAction()) {
            case ACTION_INSERT_TEXT:
                if (!(ln.getAttribute() instanceof String)) --> ti incazzi come una biscia;
                String textToAdd = (String) ln.getAttribute();
                this (cioè il main controller). addtexttolabel(textToadd);
                break;
            case ACTION_SET_TOUCH_DURATION:
                if (!(ln.getAttribute() instanceof Integer)) --> ti incazzi come una biscia;
                Integer index = (Integer) ln.getAttribute();
                SafeButton.setSafeTouchLength(index);
                break;
        }
    }
*/