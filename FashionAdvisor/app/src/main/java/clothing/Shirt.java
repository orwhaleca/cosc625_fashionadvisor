package clothing;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Matt on 5/9/17.
 *
 * Implements shirts in outfit matching
 */

public class Shirt extends Top {

    //If this is not public, it is not seen by getDeclaredFields()
    //seems to be a bug in the Android Runtime on my Mac?
    public Boolean longSleeves;

    public Shirt(Context con, Texture tex, Temperature temp, Formality form,
                 String n, int col, boolean pattern, Bitmap img, boolean longSleeve) {
        super(con, tex, temp, form, n, col, pattern, img);
        longSleeves = longSleeve;
    }

    public Shirt(int id, Context con, Texture tex, Temperature temp, Formality form,
                 String n, int col, boolean pattern, Bitmap img, boolean longSleeve) {
        super(id, con, tex, temp, form, n, col, pattern, img);
        longSleeves = longSleeve;
    }

    /*this should be unnecessary
    public int save() {
        articleData.addProperty("longSleeves", longSleeves);
        return super.save();
    }
    */

}
