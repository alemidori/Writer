package com.example.midori.writer;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {
    private static MainController instance;
    private RootActivity rootActivity;
    private SafeButton next;
    private List<TreeNode> subList;
    private Node node;
    private TreeNode actualParent, rootTreeNode;
    private int numSelectableButton;


    public static MainController getInstance() {
        if (instance == null)
            instance = new MainController();
        return instance;
    }

    private MainController() {
        Node root = Tree.getInstance().getNodeFromText("root");
        rootTreeNode = root.getTreeNode();
    }

    public void initialize() {
        rootActivity = RootActivity.getInstanceRootActivity();
        String durationTouch = rootActivity.getTocco();
        System.out.println("durationTouch " + durationTouch);

        int duration;
        switch (durationTouch) {
            case "Disabilita":
                duration = 0;
                break;
            case "Breve":
                duration = 1;
                break;
            case "Medio":
                duration = 2;
                break;
            case "Lungo":
                duration = 3;
                break;
            default:
                duration = 1;
                break;
        }
        SafeButton.setSafeTouchLength(duration);

        actualParent = rootTreeNode;
        List<SafeButton> selectableButtons = rootActivity.getButtonList();
        numSelectableButton = selectableButtons.size();
        subList = rootActivity.spreadInButtons(rootTreeNode.children, numSelectableButton);
        next = rootActivity.getNextButton();
        for (SafeButton s : selectableButtons) {
            s.setOnSafeTapListener(this);
        }
        System.out.println("Num puls " + numSelectableButton);
        next.setOnSafeTapListener(this);
        //  rootActivity.getTopText().setText((CharSequence) actualParent.data);
    }


    //**********************************************************************************************************
    //ON SAFE TAP
    @Override
    public boolean onSafeTap(SafeButton safeButton) {

        String textButton = (String) safeButton.getText();
        System.out.println(textButton);

        // NEXT BUTTON
        if (Objects.equals(safeButton, next)) {

            if (rootActivity.getLastButton().getText().toString().startsWith("Torna a")) {

                System.out.println("/////" + actualParent.data);
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
            } else if (subList == null) {
                if (actualParent == rootTreeNode) {
                    subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
                } else {
                    for (SafeButton b : rootActivity.getButtonList()) {
                        b.setText("");
                    }
                    if (Objects.equals(actualParent.parent.data, "root"))
                        rootActivity.getLastButton().setText("Torna al Menu principale");
                    else
                        rootActivity.getLastButton().setText("Torna a "+actualParent.parent.data);
                    rootActivity.getLastButton().setBackgroundColor(Color.argb(255, 46, 170, 171));
                }
            } else {

                for (SafeButton b : rootActivity.getButtonList()) {
                    b.setText("");
                }
                List<TreeNode> newSubList = rootActivity.spreadInButtons(subList, numSelectableButton);
                subList = newSubList;
            }

        }


        // SELECTABLE BUTTON
        else {
            //se è un nodo di goPrevLevel
            if ((safeButton.getText()).toString().startsWith("Torna a")) {

                subList = rootActivity.spreadInButtons(actualParent.parent.children, numSelectableButton);
                actualParent = actualParent.parent;

            }
            //se è un nodo interno all'albero
            else {
                System.out.print(safeButton.getText());
                System.out.println(textButton);
                node = Tree.getInstance().getNodeFromText((String) safeButton.getText());

                if (node.isInternal()) {

                    System.out.println("internal " + node.getTreeNode().data);
                    actualParent = node.getTreeNode();
                    System.out.println("***" + actualParent.data);
                    for (SafeButton b : rootActivity.getButtonList()) {
                        b.setText("");
                    }
                    subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);


                }
                //se è una foglia
                else {
                    System.out.print(safeButton.getText());
                    System.out.println("leaf " + node.getTreeNode().data);
                    doAction((LeafNode) safeButton.getNode());
                }
            }
        }

        return false;
    }


    private void doAction(LeafNode lf) {
        switch (lf.getAction()) {
            case LeafNode.ACTION_INSERT_TEXT:

                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    if (lf.getAttribute().toString().length() == 1) {
                        rootActivity.getInputSection().append((CharSequence) lf.getAttribute());
                    } else {
                        if (Objects.equals(lf.getAttribute().toString(), "(spazio)")) {
                            rootActivity.getInputSection().append(" ");
                        } else
                            rootActivity.getInputSection().setText((CharSequence) lf.getAttribute());
                    }
                }
                break;
            case LeafNode.ACTION_SET_TOUCH_DURATION:
                Toast toast;
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    int index = lf.getDurationTouchID(lf.getAttribute().toString());
                    SafeButton.setSafeTouchLength(index);
                    Log.d("2", (String) lf.getAttribute());

                    toast = Toast.makeText(rootActivity.getContext(), SafeButton.configureTouch(lf), Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.ACTION_AUDIO_VOLUME:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    rootActivity.getConfigurations().setConfigurations("audio", (String) lf.getAttribute());

                    toast = Toast.makeText(rootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.LAYOUT:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {

                    rootActivity.configureLayout(lf);

                }
                break;
            case LeafNode.COMMANDS:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "Salva":
                            if (Tree.getInstance().savePeriod(rootActivity.getInputSection().getText().toString())) {
                                toast = Toast.makeText(rootActivity.getContext(), "Frase salvata.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Riproduci":
                            toast = Toast.makeText(rootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        case "Cancella":
                            if (rootActivity.getInputSection().getText().length() > 0) {
                                Tree.getInstance().deleteChar(rootActivity.getInputSection().getText());
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Cancella tutto":
                            node = Tree.getInstance().deletePeriod(rootActivity.getInputSection().getText().toString());
                            if (node != null) {
                                //rootActivity.getSelectableButton().setText((CharSequence) node.getTreeNode().data);
                                toast = Toast.makeText(rootActivity.getContext(), "Frase eliminata.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        default:
                            toast = Toast.makeText(rootActivity.getContext(), "Comando sconosciuto.", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                    }

                }
                break;
            default:
                toast = Toast.makeText(rootActivity.getContext(), "Comando sconosciuto.", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }

}
