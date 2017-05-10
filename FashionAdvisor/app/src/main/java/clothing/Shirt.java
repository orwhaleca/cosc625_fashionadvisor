package clothing;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Matt on 5/9/17.
 */

public class Shirt extends Top {

    boolean longSleeves;

    public Shirt(Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        super(con, tex, temp, form, n, col, img);
    }

}
