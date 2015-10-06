package com.example.midori.writer;

/**
 * Created by Alessandra on 06/10/15.
 */
public class InternalNode extends TreeNode {
    public InternalNode(Object data) {
        super(data);
    }

    public TreeNode onSelect(){
        return (TreeNode) this.children.get(0);
    }

}
