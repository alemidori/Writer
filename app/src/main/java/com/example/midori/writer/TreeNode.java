package com.example.midori.writer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alessandra on 06/10/15.
 */
public class TreeNode<T> implements Iterable<TreeNode<T>> {

    public T data;
    public TreeNode<T> parent;
    public List<TreeNode<T>> children;

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return null;
    }

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

//    public void addChild(TreeNode child) {
//        child.parent = this;
//        this.children.add(child);
//    }
//

}
