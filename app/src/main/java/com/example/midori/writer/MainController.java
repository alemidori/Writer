package com.example.midori.writer;

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
            case "disabilita":
                duration = 0;
                break;
            case "breve":
                duration = 1;
                break;
            case "medio":
                duration = 2;
                break;
            case "lungo":
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
        rootActivity.getTopText().setText((CharSequence) actualParent.data);
    }



    //**********************************************************************************************************
    //ON SAFE TAP
    @Override
    public boolean onSafeTap(SafeButton safeButton) {

        String textButton = (String) safeButton.getText();
        System.out.println(textButton);

        // NEXT BUTTON
        if (Objects.equals(safeButton, next)) {

            if (Objects.equals(rootActivity.getLastButton().getText(), "^")) {

                System.out.println("/////" + actualParent.data);
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
            } else if (subList == null) {
                if (actualParent == rootTreeNode) {
                    subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
                } else {
                    for (SafeButton b : rootActivity.getButtonList()) {
                        b.setText("");
                    }
                    rootActivity.getLastButton().setText("^");
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
            if (Objects.equals(textButton, "^")) {

                if (Objects.equals(actualParent, rootTreeNode)) {
                    rootActivity.getTopText().setText("");
                } else {
                    subList = rootActivity.spreadInButtons(actualParent.parent.children, numSelectableButton);
                    actualParent = actualParent.parent;
                    rootActivity.getTopText().setText(rootActivity.getTopText().getText().toString().replace(" > " + actualParent.data, ""));
                }
            }
            //se è un nodo interno all'albero
            else {
                System.out.println(textButton);
                node = Tree.getInstance().getNodeFromText((String) safeButton.getText());

                if (node.isInternal()) {
                    if (Objects.equals(node.getTreeNode().data, "frasi")) {
                        if (Tree.getInstance().readFilePeriods()) {
                            actualParent = node.getTreeNode();
                            subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
                        }
                    } else {
                        System.out.println("internal " + node.getTreeNode().data);
                        if (node.getTreeNode().parent == actualParent) {
                            rootActivity.getTopText().append(" > " + safeButton.getText());
                        } else {
                            rootActivity.getTopText().setText(rootActivity.getTopText().getText().toString().replace(" > " + node.getTreeNode().parent.data, ""));
                            rootActivity.getTopText().append(" > " + safeButton.getText());
                        }

                        actualParent = node.getTreeNode();
                        System.out.println("***" + actualParent.data);
                        for (SafeButton b : rootActivity.getButtonList()) {
                            b.setText("");
                        }
                        subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
                    }


                }
                //se è una foglia
                else {
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
                rootActivity.getInputSection().setCursorVisible(true);
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

                    rootActivity.getConfigurations().setConfigurations("tocco", (String) lf.getAttribute());

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
                    rootActivity.getConfigurations().setConfigurations("audio", (String) lf.getAttribute());

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
                            RootActivity.setLayoutValue(1);
                            break;
                        case "2x2":
                            RootActivity.setLayoutValue(2);
                            break;
                        case "4x2":
                            RootActivity.setLayoutValue(3);
                            break;
                        default:
                            RootActivity.setLayoutValue(4);
                            break;
                    }

                    rootActivity.getConfigurations().setConfigurations("layout", String.valueOf(rootActivity.getLayoutValue()));

                    rootActivity.recreate();
                    System.out.println("LAYOUT " + rootActivity.getLayoutValue());


                }
                break;
            case LeafNode.COMMANDS:
                if (!(lf.getAttribute() instanceof CharSequence))
                    new Exception("Formato errato").printStackTrace();
                else {
                    switch ((String) lf.getAttribute()) {
                        case "spazio":
                            rootActivity.getInputSection().setCursorVisible(true);
                            if (rootActivity.getInputSection().length() > 0)
                                rootActivity.getInputSection().append(" ");
                            else
                                rootActivity.getInputSection().setText(" ");
                            break;
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
