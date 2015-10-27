package com.example.midori.writer;

import android.util.Log;

import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {

    //controlla tutto il comportamento del sistema; c'è un unico safebutton che gestisce tutti i tocchi;
    //è controllato tutto a livello inferiore

    private SafeButton button, next;
    private Node node;
    private TreeNode actual;
    private int i;

    public MainController() {
        Tree.getInstance();
        button = RootActivity.getSelectableButton();
        button.setOnSafeTapListener(this);
        next = RootActivity.getNextButton();
        next.setOnSafeTapListener(this);
        node = button.getNode();
        actual = node.getTreeNode();
        RootActivity.getTopText().setText((CharSequence) button.getNode().getTreeNode().parent.data);
        i = 0;
    }


    @Override
    public boolean onSafeTap(SafeButton safeButton) {

        // NEXT BUTTON
        if (Objects.equals(safeButton, next)) {
            TreeNode nextNode;
            if (Objects.equals(RootActivity.getSelectableButton().getText(), "^")) {
                i = 0;
                nextNode = (TreeNode) actual.parent.children.get(i);
                actual = nextNode;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            } else if (i < actual.parent.children.size() - 1) {
                Log.d("1", (String) actual.parent.data);
                i++;
                nextNode = (TreeNode) actual.parent.children.get(i);
                actual = nextNode;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            } else if (Objects.equals(actual.parent.data, "root")) {
                i = 0;
                nextNode = (TreeNode) actual.parent.children.get(i);
                actual = nextNode;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
                Log.d("1", (String) actual.data);
            } else {
                RootActivity.getSelectableButton().setText("^");
            }
        }


        // SELECTABLE BUTTON
        else {
            //se è un nodo di goPrevLevel
            if (Objects.equals(button.getText(), "^")) {
                if (Objects.equals(actual.data, "main") || Objects.equals(actual.data, "config")) {
                    RootActivity.getTopText().setText("");
                    actual = node.getTreeNode().parent;
                } else {
                    Log.d("1", (String) actual.data);
                    RootActivity.getTopText().setText(RootActivity.getTopText().getText().toString().replace(" > " + actual.parent.data, ""));
                    i = actual.parent.children.indexOf(actual);
                    actual = actual.parent;
                    RootActivity.getSelectableButton().setText((CharSequence) actual.data);
                }
            }
            //se è un nodo interno all'albero
            else if (node.isInternal()) {
                if (Objects.equals(actual.data, "main") || Objects.equals(actual.data, "config"))
                    RootActivity.getTopText().setText((CharSequence) actual.data);
                else
                    RootActivity.getTopText().append(" > " + actual.data);
                i = 0;
                actual = (TreeNode) actual.children.get(i);
                Log.d("1", (String) actual.data);
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            }
            //se è una foglia
            else doAction((LeafNode) button.getNode());

        }
        return false;
    }


    private void doAction(LeafNode lf) {
        switch (lf.getAction()) {
            case LeafNode.ACTION_INSERT_TEXT:
                if (!(lf.getAttribute() instanceof String))
                    new Exception("Comando sconosciuto.").printStackTrace();
                String textToAdd = (String) lf.getAttribute();
                //inserire testo nel textInput
                break;
            case LeafNode.ACTION_SET_TOUCH_DURATION:
                if (!(lf.getAttribute() instanceof Integer))
                    new Exception("Comando sconosciuto.").printStackTrace();
                Integer index = (Integer) lf.getAttribute();
                SafeButton.setSafeTouchLength(index);
                break;
        }
    }

}
