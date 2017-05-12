package clothing;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Matt on 5/9/17.
 *
 * This class exists to classify the 'slot' of an article of clothing
 * in the Today layout
 */

public abstract class Top extends Article {

    public Top(Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        super(con, tex, temp, form, n, col, img);
    }

    public Top(int id, Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        super(id, con, tex, temp, form, n, col, img);
    }

}
