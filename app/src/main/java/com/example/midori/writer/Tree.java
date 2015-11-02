package com.example.midori.writer;

import android.util.JsonReader;
import android.util.Log;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 10/10/15.
 */
public class Tree {
    private static Tree instance;
    private TreeNode root, main, config, lettere, frasi, comandi, tocco, audio, layout;
    private Node mainNode, configNode, lettereNode, frasiNode, comandiNode;
    private List<Node> nodeList = new ArrayList<>();

    public static Tree getInstance() {
        if (instance == null)
            instance = new Tree();
        return instance;
    }


    private Tree() {

        try {

            JsonReader reader = new JsonReader(new FileReader("tree_structure"));
            reader.beginObject();
            while (reader.hasNext()){
                String obj = reader.nextName();
                Log.d("2", obj);
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }



        root = new TreeNode("root");
        main = root.addChild("main");
        config = root.addChild("config");
        lettere = main.addChild("lettere");
        frasi = main.addChild("frasi");
        comandi = main.addChild("comandi");

        lettere.addChild("A");
        lettere.addChild("B");
        lettere.addChild("C");

        frasi.addChild("Hello world!");
        frasi.addChild("ababababa");

        tocco = config.addChild("tocco");
        audio = config.addChild("audio");
        layout = config.addChild("layout");

        tocco.addChild("disabilita");
        tocco.addChild("breve");
        tocco.addChild("medio");
        tocco.addChild("lungo");

        audio.addChild("muto");
        audio.addChild("basso");
        audio.addChild("alto");

        layout.addChild("2x1-portrait");
        layout.addChild("2x2-portrait");
        layout.addChild("4x2-portrait");
        layout.addChild("2x1-landscape");
        layout.addChild("2x2-landscape");
        layout.addChild("4x2-landscape");


        nodeList.add(new InternalNode(main));
        nodeList.add(new InternalNode(config));
        nodeList.add(new InternalNode(lettere));
        nodeList.add(new InternalNode(frasi));
        nodeList.add(new InternalNode(comandi));
        nodeList.add(new InternalNode(tocco));
        nodeList.add(new InternalNode(audio));
        nodeList.add(new InternalNode(layout));

        List<TreeNode> lettereChildren = lettere.children;
        for (TreeNode t : lettereChildren) {
            nodeList.add(new LeafNode(t, LeafNode.ACTION_INSERT_TEXT, t.data));
        }

        List<TreeNode> frasiChildren = frasi.children;
        for (TreeNode t : frasiChildren) {
            nodeList.add(new LeafNode(t, LeafNode.ACTION_INSERT_TEXT, t.data));
        }

        List<TreeNode> toccoChildren = tocco.children;
        for (TreeNode t : toccoChildren) {
            nodeList.add(new LeafNode(t, LeafNode.ACTION_SET_TOUCH_DURATION, t.data));
        }

        List<TreeNode> audioChildren = audio.children;
        for (TreeNode t : audioChildren) {
            nodeList.add(new LeafNode(t, LeafNode.ACTION_AUDIO_VOLUME, t.data));
        }

        List<TreeNode> layoutChildren = layout.children;
        for (TreeNode t : layoutChildren) {
            nodeList.add(new LeafNode(t, LeafNode.LAYOUT, t.data));
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
}
