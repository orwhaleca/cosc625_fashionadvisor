package clothing;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Matt on 5/9/17.
 */

public class Pants extends Bottom {

    private boolean shorts;

    public Pants(Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        super(con, tex, temp, form, n, col, img);
    }

}