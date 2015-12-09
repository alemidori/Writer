package com.example.midori.writer;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private JsonObject rootJsonObj;
    private JSONObject rootJsonOBJ, frasiJsonOBJ;
    private JsonReader reader;
    private TreeNode lastParentNode;
    private List<Node> nodeList = new ArrayList<>();
    private RootActivity rootActivity;
    private BufferedReader br;
    private Gson gson;
    private JsonParser jsonParser;

    public static Tree getInstance() {
        if (instance == null)
            instance = new Tree();
        return instance;
    }


    private Tree() {
        rootActivity = RootActivity.getInstanceRootActivity();
        gson = new Gson();
        jsonParser = new JsonParser();
        String path = rootActivity.getFilesDir().getAbsolutePath() + "/json_tree_internal";
        File file = new File(path);
        if (file.exists()) {
            try {
                br = new BufferedReader(new InputStreamReader(rootActivity.openFileInput("json_tree_internal")));
                reader = new JsonReader(new InputStreamReader(rootActivity.openFileInput("json_tree_internal")));
                System.out.println("TROVATO! IN: " + rootActivity.getFilesDir().getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            InputStream in = rootActivity.getResources().openRawResource(R.raw.json_tree_raw);
            InputStream in2 = rootActivity.getResources().openRawResource(R.raw.json_tree_raw);
            br = new BufferedReader(new InputStreamReader(in));
            reader = new JsonReader(new InputStreamReader(in2));
        }


        writeJsonString();
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

            System.out.println(jsonString);
            FileOutputStream fos = rootActivity.openFileOutput("json_tree_internal", Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //crea le istante dei Treenode a partire dal json
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
                        case "Alfabeto e caratteri":
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

    public boolean isNodeInList(String s) {
        boolean toReturn = false;
        for (Node n : nodeList) {
            if(Objects.equals(n.getTreeNode().data, s)){
                toReturn = true;
            }
        }
        return toReturn;
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
                rootJsonOBJ = new JSONObject(jsonString);
                JSONObject rootObj = rootJsonOBJ.getJSONObject("root");
                JSONObject mainObj = rootObj.getJSONObject("Menu");
                frasiJsonOBJ = mainObj.getJSONObject("Le mie frasi");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                frasiJsonOBJ.put(period, period);
                rootJsonObj = (JsonObject) jsonParser.parse(rootJsonOBJ.toString());
                jsonString = gson.toJson(rootJsonObj);
                System.out.println(jsonString);

                try {
                    FileOutputStream fos = rootActivity.openFileOutput("json_tree_internal", Context.MODE_PRIVATE);
                    fos.write(jsonString.getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            TreeNode frasi = getNodeFromText("Le mie frasi").getTreeNode();
            TreeNode newPeriod = frasi.addChild(period);
            nodeList.add(new LeafNode(newPeriod, LeafNode.ACTION_INSERT_TEXT, newPeriod.data));
            toReturn = true;
        }
        return toReturn;
    }


    public void deleteChar(String cs) {

        CharSequence withoutLast = cs.subSequence(0, rootActivity.getInputSection().getText().length() - 1);
        rootActivity.getInputSection().setText(withoutLast);
        rootActivity.getInputSection().setSelection(rootActivity.getInputSection().getText().length());
    }


    public boolean deletePeriod(String period) {
        Node n;
        int position = 0;
        boolean isIn = false;
        for (Iterator<Node> iterator = nodeList.iterator(); iterator.hasNext(); ) {
            n = iterator.next();
            if (n.getTreeNode().data.equals(period)) {
                List<TreeNode> list = n.getTreeNode().parent.children;
                for (TreeNode tnode : list) {
                    if (Objects.equals(tnode.data, period))
                        position = list.indexOf(tnode);
                    System.out.println(tnode.data);
                }
                list.remove(position);
                n.getTreeNode().parent.children.remove(n);
                iterator.remove();

                try {
                    rootJsonOBJ = new JSONObject(jsonString);
                    JSONObject rootObj = rootJsonOBJ.getJSONObject("root");
                    JSONObject mainObj = rootObj.getJSONObject("Menu");
                    frasiJsonOBJ = mainObj.getJSONObject("Le mie frasi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                frasiJsonOBJ.remove(period);
                rootJsonObj = (JsonObject) jsonParser.parse(rootJsonOBJ.toString());
                jsonString = gson.toJson(rootJsonObj);

                try {
                    FileOutputStream fos = rootActivity.openFileOutput("json_tree_internal", Context.MODE_PRIVATE);
                    fos.write(jsonString.getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                rootActivity.getInputSection().setText("");
                System.out.println(jsonString);
                isIn = true;
            }
        }
        if (isIn)
            rootActivity.getInputSection().setText("");
        for (Node node : nodeList) {
            System.out.println("NOMI NODI " + node.getTreeNode().data);
        }

        return isIn;
    }
}