package com.example.midori.writer;

/**
 * Created by Alessandra on 25/10/15.
 */
public class Node {
    private boolean isInternalNode;
    protected TreeNode treeNode;

    public Node(TreeNode t){
        treeNode = t;
    }

    public boolean isInternal(){
        return isInternalNode;
    }

    protected void setInternal(boolean value){
        isInternalNode = value;
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

}
