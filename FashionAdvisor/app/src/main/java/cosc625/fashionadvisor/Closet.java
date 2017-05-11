package cosc625.fashionadvisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import clothing.*;

/**
 * Created by Matt on 5/9/17.
 */

/*
    Is it worth it to rebuild this closet every time the program is opened?
    I know it isn't very expensive to do, but I wonder if it would be better
    to save this object instead...
 */
public final class Closet {

    private static String highestID = "highestID";
    private static SharedPreferences prefs;
    private static Hashtable<Integer, Article> hashTable = new Hashtable<Integer, Article>();

    private Closet(){ }

    public static void add(Article article) {
        int id = article.getID();
        hashTable.put(id, article);
        prefs = PreferenceManager.getDefaultSharedPreferences(article.getContext());
        //if highestID does not exist, store -1
        //then if the new article id is the greatest we've seen,
        //update this value
        if(id > prefs.getInt(highestID, -1)) {
            prefs.edit().putInt(highestID, id);
        }
    }

    public static void remove(Integer id) { hashTable.remove(id); }

    public static boolean contains(Integer id) { return hashTable.containsKey(id); }

    public static void update(Integer id, Article newArticle) {
        remove(id);
        add(newArticle);
    }

    public static String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = hashTable.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            stringBuilder.append(pair.getKey() + " = " + pair.getValue() + "\r\n");
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return stringBuilder.toString();
    }

}
