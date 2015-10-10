package com.example.midori.writer;

/**
 * Created by Alessandra on 10/10/15.
 */
public class Tree {
    public static TreeNode root, main, config, lettere, frasi, comandi, tocco, audio, layout;

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

    }
}
