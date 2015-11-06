package com.example.midori.writer;

import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {
    private static MainController instance;
    private SafeButton button, next;
    private Node node;
    private TreeNode actual;
    private int i;

    public static MainController getInstance() {
        if (instance == null)
            instance = new MainController();
        return instance;
    }

    private MainController() {
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
                    node = Tree.getInstance().getNodeFromText((String) actual.data);
                    Log.d("1", (String) node.getTreeNode().data);
                } else {
                    Log.d("1", (String) node.getTreeNode().data);
                    RootActivity.getTopText().setText(RootActivity.getTopText().getText().toString().replace(" > " + actual.parent.data, ""));
                    actual = actual.parent;
                    node = Tree.getInstance().getNodeFromText((String) actual.data);
                    i = actual.parent.children.indexOf(actual);
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
                Log.d("1", (String) node.getTreeNode().data);
                actual = (TreeNode) actual.children.get(i);
                node = Tree.getInstance().getNodeFromText((String) actual.data);
                RootActivity.getSelectableButton().setText((CharSequence) actual.data);

            }
            //se è una foglia
            else {
                doAction((LeafNode) button.getNode());
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
                    if (lf.getAttribute().toString().length() == 1)
                        RootActivity.getInputSection().append((CharSequence) lf.getAttribute());
                    else
                        RootActivity.getInputSection().setText((CharSequence) lf.getAttribute());
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
                    toast = Toast.makeText(RootActivity.getContext(), toShow, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.ACTION_AUDIO_VOLUME:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    toast = Toast.makeText(RootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case LeafNode.LAYOUT:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "2x1-portrait":
                            RootActivity.setLayoutValue(1);
                            break;
                        case "2x2-portrait":
                            RootActivity.setLayoutValue(2);
                            break;
                        case "4x2-portrait":
                            RootActivity.setLayoutValue(3);
                            break;
                        default:
                            RootActivity.setLayoutValue(1);
                            break;
                    }

                    RootActivity.getInstanceRootActivity().recreate();

                }
                break;
            case LeafNode.COMMANDS:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "salva":
                            if (Tree.getInstance().savePeriod(RootActivity.getInputSection().getText().toString())) {
                                toast = Toast.makeText(RootActivity.getContext(), "Frase salvata.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(RootActivity.getContext(), "Nessuna frase selezionata!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "riproduci":
                            toast = Toast.makeText(RootActivity.getContext(), "Funzione non disponibile.", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        case "cancella":
                            if (RootActivity.getInputSection().getText().length() > 0) {
                                Tree.getInstance().deleteChar(RootActivity.getInputSection().getText());
                            } else {
                                toast = Toast.makeText(RootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "cancella tutto":
                            node = Tree.getInstance().deletePeriod(RootActivity.getInputSection().getText().toString());
                            if (node != null) {
                                RootActivity.getSelectableButton().setText((CharSequence) node.getTreeNode().data);
                                toast = Toast.makeText(RootActivity.getContext(), "Frase eliminata.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(RootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        default:
                            toast = Toast.makeText(RootActivity.getContext(), "Comando sconosciuto.", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                    }

                }
                break;
            default:
                RootActivity.getSelectableButton().setText("foglia");
                break;
        }
    }

}
