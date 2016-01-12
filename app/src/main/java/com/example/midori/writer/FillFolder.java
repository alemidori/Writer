package com.example.midori.writer;

import android.content.res.Resources;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import java.lang.reflect.Field;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandra on 16/12/15.
 */
public class FillFolder {
    private static Tree instance;
    private String obj, folder;
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

    public void fill() {
        jsonString = "";
        String jsonLine;
        rootActivity = RootActivity.getInstanceRootActivity();
        gson = new Gson();
        jsonParser = new JsonParser();
        folder = rootActivity.getContext().getFilesDir().getAbsolutePath() + "/phraser/";


        // loop for every file in raw folder
        Field[] fields = R.raw.class.getFields();
        try {
            for (Field field : fields) {
                int rid = 0;
                try {
                    rid = field.getInt(field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // Use that if you just need the file name
                String filename = field.getName();
                String path = folder + filename;
                File file = new File(path);
                if (file.exists()) {
                    try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
                        System.out.println("TROVATO! IN: " + rootActivity.getFilesDir().getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    InputStream in = rootActivity.getResources().openRawResource(rid);
                    InputStream in2 = rootActivity.getResources().openRawResource(rid);
                    br = new BufferedReader(new InputStreamReader(in));
                    reader = new JsonReader(new InputStreamReader(in2));
                    try {
                        while ((jsonLine = br.readLine()) != null) {
                            jsonString += jsonLine + '\n';
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file, true));
                    try {
                        outputStream.write(jsonString.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**

     Il metodo che farà ciò dovrà essere chiamato prima della creazione
     dell'albero nel main controller:
     Dopo aver creato i json manualmente nella cartella raw delle resources
     in questa classe vado a prendere i contenuti di quei file e li vado
     a copiare in altrettanti file in un path che sarà /phraser/...
     nella memoria principale come ho fatto per l'unico json.

     Nel json base fare in modo che "Layout" abbia come figli non più
     2x1, 2x2 e 4x2 ma i nomi di questi file (nella cartella phraser/)
     che cambieranno sempre il layout in quanto a numero pulsanti ma
     avranno come root il nome del layout (per esempio 2x1 base o 2x2 avanzato ecc)
     e avranno tutto l'albero sotto di sè: menu e config diviso in base
     al numero dei pulsanti.
     A questo punto sarà necessario fare il parsing di questi json per costruire
     l'albero:
     quindi cambiare il metodo parseJson in Tree passandogli come parametro
     un oggetto di tipo jsonReader e leggere quello anziché il reader che ha lui.
     Passare quindi il reader avente come riferimento il file che ha come nome
     il testo del pulsante.

     **/
}
