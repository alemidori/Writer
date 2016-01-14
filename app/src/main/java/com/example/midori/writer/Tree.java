package com.example.midori.writer;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Alessandra on 10/10/15.
 */
public class Tree {
    private static Tree instance;
    private String obj, folder;
    ;
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
    private List<String> jsonList;
    private List<Node> listTreeNode;
    private List<JsonReader> jsonReaderList;
    private    Map<String, JsonReader> mapStringReader;

    public static Tree getInstance() {
        if (instance == null)
            instance = new Tree();
        return instance;
    }


    private Tree() {
        rootActivity = RootActivity.getInstanceRootActivity();
        gson = new Gson();
        jsonParser = new JsonParser();
        jsonList = new ArrayList<>();
        listTreeNode = new ArrayList<>();
        mapStringReader = new HashMap<>();
        //reader di default (due_base)
        folder = rootActivity.getContext().getFilesDir().getAbsolutePath();

        String path = folder + "/due_base";
        File file = new File(path);
        try {
            reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        listTreeNode = parseJSON(reader);
        System.out.println("abababababababababababaababa");
//        //aggiunto folder (/phraser/) nella cartella dell'internal storage
//        folder = rootActivity.getContext().getFilesDir().getAbsolutePath()+"/phraser/";
//        String path = rootActivity.getFilesDir().getAbsolutePath() + folder+"/json_tree_internal";
//        File file = new File(path);
//        if (file.exists()) {
//            try {
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
//                System.out.println("TROVATO! IN: " + rootActivity.getFilesDir().getAbsolutePath());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            InputStream in = rootActivity.getResources().openRawResource(R.raw.json_tree_raw);
//            InputStream in2 = rootActivity.getResources().openRawResource(R.raw.json_tree_raw);
//            br = new BufferedReader(new InputStreamReader(in));
//            reader = new JsonReader(new InputStreamReader(in2));
//        }
//
//
//        //writeJsonString();
//
//        //stampo il contenuto di /phraser
//        File f = new File(folder);
//        System.out.println(folder + "**********JJHVGCFXDFCHVJBKNLJBHVBCXFGCTFYUGKJBM VXDFGCHVJB");
//        File files[] = f.listFiles();
//        System.out.println("STAMPA FILE IN PHRASER**************");
//        System.out.println("Numero file: " + files.length);
//        for (File file1 : files) {
//            System.out.println("FileName:" + file1.getName());
//        }
        //________________________________

    }

//    public void writeJsonStrings(BufferedReader bufferedReader) {
//
//        jsonString = "";
//        String jsonLine;
//        try {
//            System.out.println("*********************************************");
//            while ((jsonLine = bufferedReader.readLine()) != null) {
//                jsonString += jsonLine + '\n';
//            }
//
//            System.out.println(jsonString);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void setMapStringsAndReader(Map<String,JsonReader> map) {
      mapStringReader = map;
    }

    //crea le istanze dei Treenode a partire dal json
    public List<Node> parseJSON(JsonReader jsonReader) {
        reader = jsonReader;
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
                    parseJSON(reader);
                } else {
                    //foglia

                    System.out.println(reader.peek());
                    int action = 99;
                    TreeNode childNode = lastParentNode.addChild(obj);
                    System.out.println(lastParentNode.data + " genera > " + childNode.data);

                    if (Objects.equals(childNode.data, "Layout")) {
                        addLayoutFromFolder(childNode);
                        nodeList.add(new InternalNode(childNode));
                    } else {
                        TreeNode lastGroup = childNode.parent;
                        while (action == 99) {
                            switch ((String) lastGroup.data) {
                                case "Alfabeto":
                                    action = LeafNode.ACTION_INSERT_TEXT;
                                    break;
                                case "Simboli":
                                    action = LeafNode.ACTION_INSERT_TEXT;
                                    break;
                                case "Numeri":
                                    action = LeafNode.ACTION_INSERT_TEXT;
                                    break;
                                case "Le mie frasi":
                                    action = LeafNode.ACTION_INSERT_TEXT;
                                    break;
                                case "Azioni":
                                    action = LeafNode.COMMANDS;
                                    break;
                                case "Invia come e-mail":
                                    action = LeafNode.COMMANDS;
                                    break;
                                case "Invia e-mail":
                                    action = LeafNode.COMMANDS;
                                    break;
                                case "Invia sms":
                                    action = LeafNode.COMMANDS;
                                    break;
                                case "Invia come sms":
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
                                    action = 99;
                                    new Exception("Foglia sconosciuta").printStackTrace();
                                    break;
                            }
                            lastGroup = lastGroup.parent;
                            System.out.print("lastGroup" + lastGroup.data);
                        }

                        nodeList.add(new LeafNode(childNode, action, obj));
                    }

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
        return nodeList;
    }

    public void addLayoutFromFolder(TreeNode t) {
        TreeNode layout;
        String path = rootActivity.getContext().getFilesDir().getAbsolutePath();
        File f = new File(path);
        File file[] = f.listFiles();
        for (File fl : file) {
            layout = t.addChild(fl.getName());
            nodeList.add(new LeafNode(layout, 3, fl.getName()));
        }
    }

    public boolean isNodeInList(String s) {
        boolean toReturn = false;
        for (Node n : nodeList) {
            if (Objects.equals(n.getTreeNode().data, s)) {
                toReturn = true;
            }
        }
        return toReturn;
    }

    public Node getNodeFromText(String s, List<Node> list) {
        Node toReturn = null;
        for (Node n : list) {
            if (Objects.equals(n.getTreeNode().data, s)) {
                toReturn = n;
            }
        }
        return toReturn;
    }

    public boolean savePeriod(String period) {
        Map<String,List<Node>> mapStringNodeList = new HashMap<>();
        List<Node> list;
        boolean toReturn;
        List<TreeNode> tList = new ArrayList<>();
        if (Objects.equals(period, ""))
            toReturn = false;
        else {
            for (Map.Entry<String, JsonReader> entry : mapStringReader.entrySet()) {
                list = parseJSON(entry.getValue());
                mapStringNodeList.put(entry.getKey(),list);

            }
            for (Map.Entry<String,List<Node>> entry: mapStringNodeList.entrySet()) {

                try {
                    rootJsonOBJ = new JSONObject(entry.getKey());
                    TreeNode rootNode = getNodeFromText("root", entry.getValue()).getTreeNode();
                    TreeNode tNode = rootNode;
                    while (!tNode.children.isEmpty()) {
                        for (TreeNode t : (List<TreeNode>) tNode.children) {
                            if (Objects.equals(t.data, "Le mie frasi")) {
                                while (!Objects.equals(t.parent.data, "root")){
                                    tList.add(t.parent);
                                }
                            }
                            tNode = t;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            try {

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