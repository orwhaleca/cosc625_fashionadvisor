package clothing;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Matt on 5/9/17.
 */

public class Pants extends Bottom {

    private Boolean shorts;

    //we could just have one constructor and have some default id # that triggers one to generate
    public Pants(Context con, Texture tex, Temperature temp, Formality form,
                 String n, int col, Bitmap img, boolean isShorts) {
        super(con, tex, temp, form, n, col, img);
        shorts = isShorts;
    }

    public Pants(int id, Context con, Texture tex, Temperature temp, Formality form,
                 String n, int col, Bitmap img, boolean isShorts) {
        super(id, con, tex, temp, form, n, col, img);
        shorts = isShorts;
    }

}
