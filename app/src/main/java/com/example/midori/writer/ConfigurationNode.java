package com.example.midori.writer;

import org.w3c.dom.ProcessingInstruction;

/**
 * Created by Alessandra on 30/09/15.
 */
public class ConfigurationNode {

    /* 2x1 = 1
       2x2 = 2
       4x2 = 3

       portrait = a
       landscape = b
    * */

    private int num_puls;
    private String orient;

    public ConfigurationNode(int num_p, String or) {
        this.num_puls = num_p;
        this.orient = or;
    }


    public String getDescrNum() {
        String res;
        switch (this.num_puls) {
            case 1:
                res = "2x1";
                break;
            case 2:
                res = "2x2";
                break;
            case 3:
                res = "4x2";
                break;
            default:
                res = "Indefinito";
                break;
        }
        return res;
    }

    public String getDescrOrient() {
        String res;
        switch (this.orient) {
            case "a":
                res = "Portrait";
                break;
            case "b":
                res = "Landscape";
                break;
            default:
                res = "Indefinito";
                break;
        }
        return res;
    }

}
