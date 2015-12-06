package com.example.midori.writer;

import android.graphics.Path;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private JSONObject rootJsonObj, frasiJsonObj;
    private JsonReader reader;
    private TreeNode lastParentNode, frasi;
    private List<Node> nodeList = new ArrayList<>();
    private RootActivity rootActivity;
    private BufferedReader br;
    private Gson gson;

    public static Tree getInstance() {
        if (instance == null)
            instance = new Tree();
        return instance;
    }


    private Tree() {
        rootActivity = RootActivity.getInstanceRootActivity();

        gson = new Gson();

        try {
            FileInputStream fileInputStream = new FileInputStream("../../../../src/main/jsonTree.txt");
            System.out.println("File aperto");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(rootActivity.getJsonFilePath()));
        writeJsonString();
        reader = new JsonReader(new InputStreamReader(rootActivity.getJsonFilePathToModify()));
        parseJSON();

    }


    private void writeJsonString() {
        jsonString = "";
        String jsonLine;
        try {
            System.out.println("*********************************************");
            while ((jsonLine = br.readLine()) != null) {
                jsonString += jsonLine + '\n';
            }
            try {
                rootJsonObj = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                        case "Alfabeto":
                            action = LeafNode.ACTION_INSERT_TEXT;
                            break;
                        case "Le mie frasi":
                            action = LeafNode.ACTION_INSERT_TEXT;
                            break;
                        case "Azioni":
                            action = LeafNode.COMMANDS;
                            break;
                        case "Tocco":
                            action = LeafNode.ACTION_SET_TOUCH_DURATION;
                            break;
                        case "Audio":
                            action = LeafNode.ACTION_AUDIO_VOLUME;
                            break;
                        case "Layout":
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
            System.out.println("ASJNCKEBVIERBVBERVKRE\n" + gson.toJson(rootJsonObj));

            try {
                JSONObject rootObj = rootJsonObj.getJSONObject("root");
                JSONObject mainObj = rootObj.getJSONObject("Main");
                frasiJsonObj = mainObj.getJSONObject("Le mie frasi");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                frasiJsonObj.put(period, period);
                jsonString = gson.toJson(rootJsonObj);
                System.out.println(jsonString);
                System.out.println(gson.toJson(frasiJsonObj));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            frasi = getNodeFromText("Le mie frasi").getTreeNode();
            TreeNode newPeriod = frasi.addChild(period);
            nodeList.add(new LeafNode(newPeriod, LeafNode.ACTION_INSERT_TEXT, newPeriod.data));
            toReturn = true;
        }
        return toReturn;
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