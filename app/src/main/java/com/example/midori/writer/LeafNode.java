package com.example.midori.writer;

/**
 * Created by Alessandra on 06/10/15.
 */
public class LeafNode extends Node {
    private int action;
    private Object attribute;

    public final static int ACTION_INSERT_TEXT        = 0;
    public final static int ACTION_SET_TOUCH_DURATION = 1;

    public LeafNode(TreeNode treeNode, int action, Object attribute) {
        super(treeNode);
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
