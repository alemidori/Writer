package com.example.midori.writer;

/**
 * Created by Alessandra on 16/12/15.
 */
public class FillFolder {
    /**

     Il metodo che farà ciò dovrà essere chiamato prima della creazione
     dell'albero nel main controller:
     Dopo aver creato i json manualmente nella cartella raw delle resources
     in questa classe vado a prendere i contenuti di quei file e li vado
     a copiare in altrettanti file in un path che sarà /phraser/...
     nella memoria principale come ho fatto per l'unico json, dopo di che
     vado ad eliminare i file nel raw perché sarebbe uno spreco di memoria.

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
