package cosc625.fashionadvisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import clothing.*;

/**
 * Created by Matt on 5/9/17.
 *
 * The closet is the internal storage structure used by this app to hold articles
 * and make matching outfits.
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
            prefs.edit().putInt(highestID, id).commit();
        }
    }

    public static void remove(Integer id) { hashTable.remove(id); }

    public static boolean contains(Integer id) { return hashTable.containsKey(id); }

    public static void update(Integer id, Article newArticle) {
        remove(id);
        add(newArticle);
    }

    public static int getSize() { return hashTable.size(); }

    /**
     * Gets a matching outfit based on our set of criteria and returns it as an Article array.
     * in returned array, [0] is a Top object and [1] is a Bottom object.
     *
     * @return      Array holding articles for every slot. [0] is a top, [1] is a bottom.
     */
    public static Article[] getOutFit(int temperature, Formality formality) {

        //switch formality and use flowthrough to add appropriate clothes
        Article[] articles = new Article[2];
        Random random = new Random();
        articles[0] = hashTable.get(random.nextInt(getSize()));//this is not going to work when articles get deleted
        articles[1] = hashTable.get(random.nextInt(getSize()));
        return articles;
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
