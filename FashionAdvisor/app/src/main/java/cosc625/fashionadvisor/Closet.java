package cosc625.fashionadvisor;

import java.util.Hashtable;
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

    private static Hashtable<Integer, Article> hashTable = new Hashtable<Integer, Article>();

    private Closet(){ }

    public static void add(Article article) {
        hashTable.put(article.id, article);
    }

    public static void remove(Integer id) { hashTable.remove(id); }

    public static boolean contains(Integer id) { return hashTable.containsKey(id); }

    public static void update(Integer id, Article newArticle) {
        remove(id);
        add(newArticle);
    }

}
