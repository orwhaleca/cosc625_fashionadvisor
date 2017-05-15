package cosc625.fashionadvisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import clothing.*;

import com.mattyork.colours.*;

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
    private static final double minColorDistance = 5;
    private static final double clashThreshold = 10;
    private static final double maxColorDistance = 60;

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
     * in returned array, [0] is a Top object and [1] is a Bottom object. Returns null in
     * place of an Article when no good solutions are found.
     *
     * @return      Array holding articles for every slot. [0] is a top, [1] is a bottom.
     */
    public static Article[] getOutFit(int temperature, Formality formality, SharedPreferences prefs) {

        topPile.clear();
        botPile.clear();

        Iterator it = hashTable.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Article a = (Article) pair.getValue();
            Formality f = a.getFormality();

            switch(formality) {

                case CASUAL: //include casual, athletic, and b.c.
                    if(f == Formality.CASUAL || f == Formality.ATHLETIC ||
                            f == Formality.BUSINESS_CASUAL) {
                        addArticleToPile(a);
                    }
                    break;

                case ATHLETIC: //include athletic only
                    if(f == Formality.ATHLETIC) addArticleToPile(a);
                    break;

                case BUSINESS_CASUAL: //include b.c. and formal
                    if(f == Formality.BUSINESS_CASUAL || f == Formality.FORMAL) {
                        addArticleToPile(a);
                    }
                    break;

                case FORMAL: //include only formal
                    if(f == Formality.FORMAL) addArticleToPile(a);
                    break;

                case SLEEPWEAR: //include only sleepwear
                    if(f == Formality.SLEEPWEAR) addArticleToPile(a);
            }// end switch

        }// end iterator

        return clothesPileShuffle(temperature, prefs);
    }

    private static void addArticleToPile(Article a) {
        if(a instanceof Top) {
            topPile.add((Top) a);
        } else if(a instanceof  Bottom) {
            botPile.add((Bottom) a);
        }
    }

    private static Article[] clothesPileShuffle(int temperature, SharedPreferences prefs) {
        Collections.shuffle(topPile);
        Collections.shuffle(botPile);

        Article[] outfit = new Article[2];
        outfit[0] = pileSearch(topPile, temperature, prefs, false, false, 0);

        boolean blockPatterns = false;
        boolean matchColors = false;
        int firstColor = 0;
        if(outfit[0] != null) {
            blockPatterns = outfit[0].isPatterned();
            matchColors = true;
            firstColor = outfit[0].getColor();
        }
        outfit[1] = pileSearch(botPile, temperature, prefs, blockPatterns, matchColors, firstColor);

        return outfit;
    }

    private static Article pileSearch(Stack pile, int temperature,
                                      SharedPreferences prefs, boolean blockingPatterns,
                                      boolean matchColors, int colorToMatch) {
        Article a;
        Temperature articleTemp;
        boolean goodChoice;
        boolean hot = false;
        boolean cold = false;
        double distance;

        if(temperature < prefs.getInt("PreferredMin", 38)) cold = true;
        if(temperature > prefs.getInt("PreferredMax", 80)) hot = true;

        while(!pile.isEmpty()) {
            goodChoice = true;
            a = (Article) pile.peek();

            //Handle clashing patterns
            if(blockingPatterns) {
                if( a.isPatterned() ) {
                    goodChoice = false;
                }
            }

            //Handle current temperature
            articleTemp = a.getIdealTemp();
            if(hot && articleTemp != Temperature.HOT) goodChoice = false; //too hot
            else if (cold && articleTemp != Temperature.COLD) goodChoice = false; //too cold
            else if(!hot && !cold && articleTemp == Temperature.HOT) goodChoice = false; //too cold
            else if(!hot && !cold && articleTemp == Temperature.COLD) goodChoice = false; //too hot

            //handle clashing colors
            distance = Colour.distanceBetweenColorsWithFormula(a.getColor(), colorToMatch,
                    Colour.ColorDistanceFormula.ColorDistanceFormulaCIE94);
            //if(distance > minColorDistance && distance < clashThreshold) goodChoice = false; //similar clash
            //if(distance > maxColorDistance) goodChoice = false; //complimentary clash

            if(goodChoice) return (Article) pile.pop();
            pile.pop(); //remove this non-matching article and continue
        }

        return null; // return null if no good matches are found
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
