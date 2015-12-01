package com.example.midori.writer;

import android.util.JsonReader;
import android.util.JsonToken;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 10/10/15.
 */
public class Tree {
    private static Tree instance;
    private String obj;
    private String jsonString;
    private JSONObject frasiObject;
    private JsonReader reader;
    private TreeNode lastParentNode, frasi;
    private List<Node> nodeList = new ArrayList<>();
    private RootActivity rootActivity;
    private BufferedReader br;

    public static Tree getInstance() {
        if (instance == null)
            instance = new Tree();
        return instance;
    }


    private Tree() {
        rootActivity = RootActivity.getInstanceRootActivity();
     
        br = new BufferedReader(new InputStreamReader(rootActivity.getFileFrasi()));
        writeJsonString();
        reader = new JsonReader(new InputStreamReader(rootActivity.getFileFrasiJson()));
        parseJSON();

    }


    private void writeJsonString() {
        jsonString = " ciao ";
        String jsonLine;
        try {
            System.out.println("Io sono qui");
            while ((jsonLine = br.readLine()) != null) {
                jsonString += jsonLine;
                //System.out.println("succhia cazzi" + jsonLine);
            }
            System.out.println("\n\n\nAAAAAAAAAAA PROVAAAAAA" + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rootActivity.getFileFrasi().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON() {

        boolean flag = false;
        try {
            reader.beginObject();

            while (reader.hasNext()) {
                if (obj == null) {
                    System.out.println("PRIMA CHIAMATA");
                    obj = reader.nextName();
                    TreeNode firstNode = new TreeNode<>(obj);
                    lastParentNode = firstNode;
                    nodeList.add(new Node(lastParentNode));
                    System.out.println("PRIMO " + lastParentNode.data);

                } else {
                    flag = true;
                    obj = reader.nextName();

                }

                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    //nodo interno
                    if (flag) {
                        System.out.println("NODO INTERNO " + lastParentNode.data);
                        lastParentNode = lastParentNode.addChild(obj);
                        nodeList.add(new InternalNode(lastParentNode));
                        System.out.println("Genera > " + lastParentNode.data);
                    }
                    parseJSON();
                } else {
                    //foglia

                    System.out.println(reader.peek());
                    int action = 0;
                    TreeNode childNode = lastParentNode.addChild(obj);
                    System.out.println(lastParentNode.data + " genera > " + childNode.data);
                    switch ((String) lastParentNode.data) {
                        case "lettere":
                            action = LeafNode.ACTION_INSERT_TEXT;
                            break;
                        case "raw/frasi":
                            action = LeafNode.ACTION_INSERT_TEXT;
                            break;
                        case "comandi":
                            action = LeafNode.COMMANDS;
                            break;
                        case "tocco":
                            action = LeafNode.ACTION_SET_TOUCH_DURATION;
                            break;
                        case "audio":
                            action = LeafNode.ACTION_AUDIO_VOLUME;
                            break;
                        case "layout":
                            action = LeafNode.LAYOUT;
                            break;
                        default:
                            new Exception("Foglia sconosciuta").printStackTrace();
                            break;
                    }

                    nodeList.add(new LeafNode(childNode, action, obj));
                    reader.skipValue();

                }
            }
            reader.endObject();
            System.out.println("Chiusura " + lastParentNode.data);
            if (!Objects.equals(lastParentNode.data, "root")) {
                lastParentNode = lastParentNode.parent;
                System.out.println("Genitore nodo chiuso " + lastParentNode.data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Node getNodeFromText(String s) {
        Node toReturn = null;
        for (Node n : nodeList) {
            if (Objects.equals(n.getTreeNode().data, s)) {
                toReturn = n;
            }
        }
        return toReturn;
    }

    public boolean savePeriod(String period) {
        boolean toReturn;
        if (Objects.equals(period, ""))
            toReturn = false;
        else {

            try {
                frasiObject.put(period,period);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            frasi = getNodeFromText("frasi").getTreeNode();
            TreeNode newPeriod = frasi.addChild(period);
            nodeList.add(new LeafNode(newPeriod, LeafNode.ACTION_INSERT_TEXT, newPeriod.data));
            toReturn = true;
        }
        return toReturn;
    }

    public boolean readFilePeriods() {
        frasi = getNodeFromText("frasi").getTreeNode();
        String s;
        try {
            while ((s = br.readLine()) != null) {
                frasi.addChild(s);
                nodeList.add(new LeafNode(new TreeNode(s), LeafNode.ACTION_INSERT_TEXT, s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !frasi.children.isEmpty();
    }

    public void deleteChar(CharSequence cs) {
        CharSequence withoutLast = cs.subSequence(0, rootActivity.getInputSection().getText().length() - 1);
        rootActivity.getInputSection().setText(withoutLast);
    }

    public Node deletePeriod(String period) {
        Node nextPeriod = null;
        Node n;
        for (Iterator<Node> iterator = nodeList.iterator(); iterator.hasNext(); ) {
            n = iterator.next();
            if (n.getTreeNode().data.equals(period)) {
                if (nodeList.indexOf(n) < nodeList.size() - 1)
                    nextPeriod = nodeList.get(nodeList.indexOf(n) + 1);
                else nextPeriod = nodeList.get(0);
                iterator.remove();
            }
        }
        if (nextPeriod != null) {
            rootActivity.getInputSection().setText("");
            return nextPeriod;
        } else return null;

    }
}
