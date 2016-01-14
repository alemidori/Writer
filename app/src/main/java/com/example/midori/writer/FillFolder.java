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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alessandra on 16/12/15.
 */
public class FillFolder {
    private static Tree instance;
    private String obj, folder;
    private JsonObject rootJsonObj;
    private JSONObject rootJsonOBJ, frasiJsonOBJ;
    private JsonReader reader;
    private TreeNode lastParentNode;
    private List<Node> nodeList = new ArrayList<>();
    private RootActivity rootActivity;
    private BufferedReader br;
    private Gson gson;
    private List<JsonReader> jsonReaderList;
    private List<String> jsonList;
    private JsonParser jsonParser;


    public FillFolder() {
        rootActivity = RootActivity.getInstanceRootActivity();
        gson = new Gson();
        jsonParser = new JsonParser();
        folder = rootActivity.getContext().getFilesDir().getAbsolutePath();
        jsonReaderList = new ArrayList<>();
        jsonList = new ArrayList<>();
    }

    public void fill() {
        // loop for every file in raw folder
        Field[] fields = R.raw.class.getFields();
        Map<String, JsonReader> mapStringReader = new HashMap<>();
        try {
            int rid = 0;
            int numFiles = 0;
            for (Field field : fields) {
                numFiles++;
                String jsonString = "";
                try {
                    rid = field.getInt(field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // Use that if you just need the file name
                String filename = field.getName();
                System.out.println("PAPAPAPAPA"+filename);
                String path = folder + File.separator + filename;
                System.out.println(path);
                File file = new File(path);
                System.out.println(file.getName());
                if (file.exists()) {
                    try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
                        System.out.println("TROVATO! IN: " + rootActivity.getFilesDir().getAbsolutePath());
                        String jsonLine;
                        while ((jsonLine = br.readLine()) != null) {
                            jsonString += jsonLine + '\n';
                            System.out.println("line json\n" + jsonLine);
                        }
                        jsonList.add(jsonString);
                        jsonReaderList.add(reader);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("CACACACACA"+filename);
                    InputStream in = rootActivity.getResources().openRawResource(rid);
                    InputStream in2 = rootActivity.getResources().openRawResource(rid);
                    br = new BufferedReader(new InputStreamReader(in));
                    reader = new JsonReader(new InputStreamReader(in2));
                    try {
                        String jsonLine;
                        while ((jsonLine = br.readLine()) != null) {
                            jsonString += jsonLine + '\n';
                            System.out.println("line json\n" + jsonLine);
                        }
                        jsonList.add(jsonString);
                        jsonReaderList.add(reader);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fos = rootActivity.openFileOutput(filename, 0);
                    //OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
                    try {
                        fos.write(jsonString.getBytes());
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Tree.getInstance().setMapStringsAndReader(jsonList, jsonReaderList);
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
