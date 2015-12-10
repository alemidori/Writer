package com.example.midori.writer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Alessandra on 22/10/15.
 */
public class MainController implements SafeTapListener {
    private TextToSpeech tts;
    private TextView textView;
    private static MainController instance;
    private RootActivity rootActivity;
    private String smsMsg;
    private SafeButton next;
    private List<TreeNode> subList;
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
        smsMsg =null;
        rootActivity = RootActivity.getInstanceRootActivity();
        String durationTouch = rootActivity.getTocco();
        System.out.println("durationTouch " + durationTouch);

        tts = new TextToSpeech(rootActivity.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ITALIAN);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        System.out.println("Lingua non supportata!");
                    }
                } else
                    System.out.println("Errore durante la riproduzione");
            }
        });

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

    }


    //**********************************************************************************************************
    //ON SAFE TAP
    @Override
    public boolean onSafeTap(SafeButton safeButton) {

        String textButton = (String) safeButton.getText();
        System.out.println("TESTO PULSANTE "+textButton);

        // NEXT BUTTON
        if (Objects.equals(safeButton, next)) {

            if (rootActivity.getLastButton().getText().toString().startsWith("Torna a")) {
                System.out.println("TESTO PULSANTE "+textButton);
                System.out.println("/////" + actualParent.data);
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
            } else if (rootActivity.getLastButton().getText().toString().startsWith("Vai a Invia sms")) {
                actualParent = Tree.getInstance().getNodeFromText("Numeri").getTreeNode();
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
                        rootActivity.getLastButton().setText("Torna a " + actualParent.parent.data);
                    rootActivity.getLastButton().setBackgroundColor(Color.argb(255, 73, 73, 73));
                }
            } else {
                System.out.println("TESTO PULSANTE "+textButton);
                for (SafeButton b : rootActivity.getButtonList()) {
                    b.setText("");
                }
                List<TreeNode> newSubList = rootActivity.spreadInButtons(subList, numSelectableButton);
                subList = newSubList;
            }

        }


        // SELECTABLE BUTTON
        else {

            //se il nodo è presente nell'albero
            if (Tree.getInstance().isNodeInList((String) safeButton.getText())) {

                System.out.print(safeButton.getText());
                System.out.println(textButton);
                Node node = Tree.getInstance().getNodeFromText((String) safeButton.getText());

                //se è un nodo interno all'albero
                if (node.isInternal()) {

                    System.out.println("internal " + node.getTreeNode().data);
                    actualParent = node.getTreeNode();
                    System.out.println("***" + actualParent.data);
                    rootActivity.getTextView().setText((CharSequence) actualParent.data);
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

            //se è un nodo di goPrevLevel
            else if ((safeButton.getText()).toString().startsWith("Torna a")) {

                subList = rootActivity.spreadInButtons(actualParent.parent.children, numSelectableButton);
                actualParent = actualParent.parent;
                if (Objects.equals(actualParent.data, "root"))
                    rootActivity.getTextView().setText("Menu principale");
                else
                    rootActivity.getTextView().setText((CharSequence) actualParent.data);
            } else if (Objects.equals(safeButton.getText(), "Vai a Invia e-mail")) {
                actualParent = Tree.getInstance().getNodeFromText("Invia come e-mail").getTreeNode();
                rootActivity.getTextView().setText((CharSequence) actualParent.data);
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);

            } else if (Objects.equals(safeButton.getText(), "Vai a Invia sms")) {
                actualParent = Tree.getInstance().getNodeFromText("Invia come sms").getTreeNode();
                rootActivity.getTextView().setText((CharSequence) actualParent.data);
                subList = rootActivity.spreadInButtons(actualParent.children, numSelectableButton);
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
                        } else {
                            rootActivity.getInputSection().append((" " + lf.getAttribute()));
                            rootActivity.getInputSection().setSelection(rootActivity.getInputSection().getText().length());
                        }
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
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Riproduci":
                            if (rootActivity.getInputSection().getText().length() > 0) {
                                tts.speak(rootActivity.getInputSection().getText(), TextToSpeech.QUEUE_FLUSH, null, "play");
                                toast = Toast.makeText(rootActivity.getContext(), "In riproduzione...", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Cancella":
                            if (rootActivity.getInputSection().getText().length() > 0){
                                    String withoutLast = rootActivity.getInputSection().getText().toString().substring(0, rootActivity.getInputSection().getText().toString().length() - 1);
                                    rootActivity.getInputSection().setText(withoutLast);
                                    rootActivity.getInputSection().setSelection(rootActivity.getInputSection().getText().length());
                            }

                            break;
                        case "Cancella tutto":
                            if (rootActivity.getInputSection().getText().length() > 0) {
                                rootActivity.getInputSection().setText("");
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Elimina frase":

                            if (rootActivity.getInputSection().getText().length() > 0) {
                                boolean deleted = Tree.getInstance().deletePeriod(rootActivity.getInputSection().getText().toString());
                                if (deleted) {
                                    toast = Toast.makeText(rootActivity.getContext(), "Frase eliminata.", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    toast = Toast.makeText(rootActivity.getContext(), "La frase selezionata non è presente nella tua lista frasi.", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessuna frase selezionata.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            break;
                        case "Inserisci indirizzo":
                            CharSequence emailMsg = rootActivity.getInputSection().getText();
                            rootActivity.getTextView().setText("Inserisci indirizzo e-mail");
                            rootActivity.getInputSection().setText("");
                            subList = rootActivity.spreadInButtons(Tree.getInstance().getNodeFromText("Alfabeto e simboli").getTreeNode().children, numSelectableButton);
                            break;
                        case "Inserisci numero":
                            smsMsg = rootActivity.getInputSection().getText().toString();
                            rootActivity.getTextView().setText("Inserisci numero di telefono");
                            rootActivity.getInputSection().setText("");
                            subList = rootActivity.spreadInButtons(Tree.getInstance().getNodeFromText("Numeri").getTreeNode().children, numSelectableButton);
                            break;
                        case "Invia e-mail":
                            //TODO
                            break;
                        case "Invia sms":
                            String phoneNumber = rootActivity.getInputSection().getText().toString();
                            if(phoneNumber.matches("-?\\d+(\\.\\d+)?")){
                                if (Objects.equals((phoneNumber), "")) {
                                    toast = Toast.makeText(rootActivity.getContext(), "Nessun numero inserito.", Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (phoneNumber.length() < 10) {
                                    toast = Toast.makeText(rootActivity.getContext(), "Numero non valido.", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    if(Objects.equals(smsMsg, "")){
                                        toast = Toast.makeText(rootActivity.getContext(), "Messaggio vuoto.", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                    else {
                                        SmsManager sms = SmsManager.getDefault();
                                        sms.sendTextMessage(phoneNumber, null, smsMsg, null, null);
                                        toast = Toast.makeText(rootActivity.getContext(), "Messaggio inviato.", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                            else {
                                toast = Toast.makeText(rootActivity.getContext(), "Nessun numero inserito.", Toast.LENGTH_LONG);
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
