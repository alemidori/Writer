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
    private TreeNode actual;
    private int i;


    public void start() {
        Tree.getInstance();
        RootActivity.getSelectableButton().setText("main");
        button = RootActivity.getSelectableButton();
        RootActivity.getTopText().setText((CharSequence) button.getNode().getTreeNode().parent);
        i = 0;
    }

    @Override
    public boolean onSafeTap(SafeButton safeButton) {
        button.setOnSafeTapListener(this);

        // NEXT BUTTON
        if (Objects.equals(button.getText(), "->")) {
            TreeNode next;
            if (Objects.equals(RootActivity.getSelectableButton().getText(), "^")) {
                i = 0;
                next = (TreeNode) actual.parent.children.get(i);
                actual = next;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            } else if (i < actual.parent.children.size() - 1) {
                Log.d("1", (String) actual.parent.data);
                i++;
                next = (TreeNode) actual.parent.children.get(i);
                actual = next;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            } else if (Objects.equals(actual.parent.data, "root")) {
                i = 0;
                next = (TreeNode) actual.parent.children.get(i);
                actual = next;
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            } else {
                RootActivity.getSelectableButton().setText("^");
            }
        }


        // SELECTABLE BUTTON
        else {
            node = button.getNode();
            //se è un nodo interno all'albero
            if (node.isInternal()) {
                actual = node.getTreeNode();
                if (Objects.equals(actual.data, "main") || Objects.equals(actual.data, "config"))
                    RootActivity.getTopText().setText((CharSequence) actual.data);
                else
                    RootActivity.getTopText().append(" > " + actual.data);
                actual = (TreeNode) actual.children.get(0);
                i = 0;
                Log.d("1", (String) actual.data);
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);
            }

            //se è un nodo goPrevLevel
            else if (Objects.equals(button.getText(), "^")) {
                if (!Objects.equals(actual.data, "main") || !Objects.equals(actual.data, "config")) {
                    RootActivity.getTopText().setText("");
                } else
                    RootActivity.getTopText().setText(RootActivity.getTopText().getText().toString().replace(" > " + actual.parent.data, ""));
                actual = node.getTreeNode().parent;
                i = actual.parent.children.indexOf(actual);
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
