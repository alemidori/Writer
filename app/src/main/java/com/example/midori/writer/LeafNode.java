package com.example.midori.writer;

/**
 * Created by Alessandra on 06/10/15.
 */
public class LeafNode extends Node {
    private int action;
    private Object attribute;

    public final static int ACTION_INSERT_TEXT = 0;
    public final static int ACTION_SET_TOUCH_DURATION = 1;
    public final static int ACTION_AUDIO_VOLUME = 2;
    public final static int LAYOUT = 3;
    public final static int COMMANDS = 4;


    public LeafNode(TreeNode treeNode, int action, Object attribute) {
        super(treeNode);
        setInternal(false);
        this.action = action;
        this.attribute = attribute;

    }

    public int getAction() {
        return action;
    }

    public Object getAttribute() {
        return attribute;
    }

    public int getDurationTouchID(String attribute) {
        int toReturn;
        switch (attribute) {
            case "Disabilita":
                toReturn = 0;
                break;
            case "Breve":
                toReturn = 1;
                break;
            case "Medio":
                toReturn = 2;
                break;
            case "Lungo":
                toReturn = 3;
                break;
            default:
                toReturn = 1;
                break;
        }
        return toReturn;
    }


}
