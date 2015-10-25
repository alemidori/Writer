package com.example.midori.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alessandra on 10/10/15.
 */
public class Tree {
    public static TreeNode root, main, config, lettere, frasi, comandi, tocco, audio, layout;
    public static Node mainNode, configNode, lettereNode, frasiNode, comandiNode;
    public static List<Node> nodeList = new ArrayList<>();

    public static void populate() {

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

        List<TreeNode> l = lettere.children;
        for (TreeNode t : l) {
            nodeList.add(new LeafNode(t, LeafNode.ACTION_INSERT_TEXT, t.data));
        }
    }

    public static Node getNodeFromText(String s) {
        Node toReturn = null;
        for (Node n : nodeList) {
            if (Objects.equals(n.getTreeNode().data, s)) {
                toReturn = n;
            }
        }
        return toReturn;
    }
}
