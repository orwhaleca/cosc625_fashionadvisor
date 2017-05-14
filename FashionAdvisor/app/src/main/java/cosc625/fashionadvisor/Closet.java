package cosc625.fashionadvisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

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
    private static Hashtable<Integer, Article> hashTable = new Hashtable<>();
    private static Stack<Top> topPile = new Stack<>();
    private static Stack<Bottom> botPile = new Stack<>();

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

        topPile.clear();
        botPile.clear();

        Iterator it = hashTable.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Article a = (Article) pair.getValue();

            //switch formality and use flow-through to add appropriate clothes
            switch(formality) {

                case CASUAL://include casual, athletic, and b.c.
                    addArticleToPile(a);

                case ATHLETIC://include athletic only
                    addArticleToPile(a);
                    if(formality == Formality.ATHLETIC) break;
                    //else flow into b.c.

                case BUSINESS_CASUAL: //include b.c. and formal
                    addArticleToPile(a);

                case FORMAL:
                    addArticleToPile(a);
                    break;

                case SLEEPWEAR:
                    addArticleToPile(a);
            }// end switch

        }// end iterator

        return clothesPileShuffle();
    }

    private static void addArticleToPile(Article a) {
        if(a instanceof Top) {
            topPile.add((Top) a);
        } else if(a instanceof  Bottom) {
            botPile.add((Bottom) a);
        }
    }

    private static Article[] clothesPileShuffle() {
        Collections.shuffle(topPile);
        Collections.shuffle(botPile);

        Article[] outfit = new Article[2];
        outfit[0] = pileSearch(topPile);
        outfit[1] = pileSearch(botPile);

        return outfit;
    }

    private static Article pileSearch(Stack pile) {
       // while((top = topPile.pop()) != null) {
           // if(top.)
       // }
        /*
        Article[] articles = new Article[2];
        Random random = new Random();
        articles[0] = hashTable.get(random.nextInt(getSize()));//this is not going to work when articles get deleted
        articles[1] = hashTable.get(random.nextInt(getSize()));
        return articles;
        */

        return (Article) pile.pop();
    }

    public static String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = hashTable.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            stringBuilder.append(pair.getKey());
            stringBuilder.append(" = ");
            stringBuilder.append(pair.getValue());
            stringBuilder.append("\r\n");
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return stringBuilder.toString();
    }

}
