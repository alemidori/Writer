package com.example.midori.writer;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {
    private RootActivity rootActivity;
    private SafeButton next;
    private List<TreeNode> subList;
    private Node node;
    private TreeNode actualParent, rootTreeNode;
    private int numSelectableButton;


    public MainController() {
        rootActivity = RootActivity.getInstanceRootActivity();
        List<SafeButton> selectableButtons = rootActivity.getButtonList();

        for (SafeButton s : selectableButtons) {
            s.setOnSafeTapListener(this);
        }

        Node root = Tree.getInstance().getNodeFromText("root");
        rootTreeNode = root.getTreeNode();
        numSelectableButton = selectableButtons.size();
        System.out.println("Num puls "+numSelectableButton);
        subList = rootActivity.spreadInButtons(rootTreeNode.children, numSelectableButton);

        actualParent = rootTreeNode;

        next = rootActivity.getNextButton();
        next.setOnSafeTapListener(this);
        rootActivity.getTopText().setText((CharSequence) actualParent.data);
    }


    @Override
    public boolean onSafeTap(SafeButton safeButton) {

        node = Tree.getInstance().getNodeFromText((String) safeButton.getText());

        // NEXT BUTTON
        if (Objects.equals(safeButton, next)) {

            if (Objects.equals(rootActivity.getLastButton().getText(), "^")) {
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
            } else if (subList == null) {
                for(SafeButton b: rootActivity.getButtonList()){
                    b.setText("");
                }
                rootActivity.getLastButton().setText("^");
            } else {
                System.out.println(subList.size());
                List<TreeNode> newSubList = rootActivity.spreadInButtons(subList, numSelectableButton);
                subList = newSubList;
            }

        }


        // SELECTABLE BUTTON
        else
            //se è un nodo di goPrevLevel
            if (Objects.equals(rootActivity.getLastButton().getText(), "^")) {
                if (Objects.equals(actualParent, rootTreeNode)) {
                    rootActivity.getTopText().setText("");
                } else {
                    subList = rootActivity.spreadInButtons(actualParent.parent.children, numSelectableButton);
                    actualParent = actualParent.parent;
                    rootActivity.getTopText().setText(rootActivity.getTopText().getText().toString().replace(" > " + actualParent.data, ""));
                }
            }
            //se è un nodo interno all'albero
            else if (node.isInternal()) {

                rootActivity.getTopText().append(" > " + safeButton.getText());
                node = Tree.getInstance().getNodeFromText((String) safeButton.getText());
                actualParent = node.getTreeNode();
                subList = rootActivity.spreadInButtons(node.getTreeNode().children, numSelectableButton);

            }
            //se è una foglia
            else {
                doAction((LeafNode) safeButton.getNode());
            }
        return false;
    }


    private void doAction(LeafNode lf) {
        switch (lf.getAction()) {
            case LeafNode.ACTION_INSERT_TEXT:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    if (lf.getAttribute().toString().length() == 1)
                        rootActivity.getInputSection().append((CharSequence) lf.getAttribute());
                    else
                        rootActivity.getInputSection().setText((CharSequence) lf.getAttribute());
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
                    String toShow;
                    switch ((String) lf.getAttribute()) {
                        case "disabilita":
                            toShow = "Tocco 'safe' disabilitato.";
                            break;
                        case "breve":
                            toShow = "Tocco breve impostato.";
                            break;
                        case "medio":
                            toShow = "Tocoo medio impostato.";
                            break;
                        case "lungo":
                            toShow = "Tocco lungo impostato.";
                            break;
                        default:
                            toShow = "Errore nella scelta della durata del tocco.";
                            break;
                    }
                    toast = Toast.makeText(rootActivity.getContext(), toShow, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.ACTION_AUDIO_VOLUME:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    toast = Toast.makeText(rootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.LAYOUT:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "2x1":
                            rootActivity.setLayoutValue(1);
                            break;
                        case "2x2":
                            rootActivity.setLayoutValue(2);
                            break;
                        case "4x2":
                            rootActivity.setLayoutValue(3);
                            break;
                        default:
                            rootActivity.setLayoutValue(1);
                            break;
                    }

                    System.out.println("LAYOUT " + rootActivity.getLayoutValue());
                    rootActivity.recreate();

                }
                break;
            case LeafNode.COMMANDS:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "salva":
                            if (Tree.getInstance().savePeriod(rootActivity.getInputSection().getText().toString())) {
                                toast = Toast.makeText(rootActivity.getContext(), "Frase salvata.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "riproduci":
                            toast = Toast.makeText(rootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        case "cancella":
                            if (rootActivity.getInputSection().getText().length() > 0) {
                                Tree.getInstance().deleteChar(rootActivity.getInputSection().getText());
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "cancella tutto":
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
                //rootActivity.getSelectableButton().setText("eh?");
                break;
        }
    }

}
