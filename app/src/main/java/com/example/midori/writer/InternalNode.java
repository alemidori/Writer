package com.example.midori.writer;

/**
 * Created by Alessandra on 25/10/15.
 */
public class InternalNode extends Node {

    public InternalNode(TreeNode treeNode) {
        super(treeNode);
        super.setInternal(true);
    }

}
