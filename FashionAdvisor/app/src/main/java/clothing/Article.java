package clothing;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cosc625.fashionadvisor.Closet;

/**
 * Created by Matt on 5/9/17.
 */

public abstract class Article implements Serializable {

    //TODO: add data members shared by all clothing
    Context context;
    Texture texture;
    Temperature idealTemp;
    Formality formality;
    String name;
    public int id;     //start at 0 and go up. Maybe our global clothes list has some nextID() to use
    int color;  //expressed as hex RGB
    Bitmap image;
    /*maybe we store the image in the article rather than the path?
    can we compress this to JSON?
    we can store the objects directly without JSON for now:
    http://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android
    */

    /**
     * During deserialization, the fields of non-serializable classes will be initialized
     * using the public or protected no-arg constructor of the class. A no-arg constructor
     * must be accessible to the subclass that is serializable. The fields of serializable
     * subclasses will be restored from the stream.
     */
    public Article() {

    }

    // having context be something that gets saved but can be different when loaded seems dangerous,
    // may need to implement some other serialization method like shown here
    // https://developer.android.com/reference/java/io/Serializable.html
    public Article(Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        context = con;
        texture = tex;
        idealTemp = temp;
        formality = form;
        name = n;
        color = col;
        image = img;
        //assign an id
    }

    /**
     * This method will save the current article of clothing to the disk
     * and also add it to the current working set of clothing.
     *
     * @return  positive integer id if save successful, -1 if save failed
     */
    public int save() {
        /*  TODO: implement a global class which holds all the working clothing at run-time.
            This class should also read in all the clothing files at startup in Splash.
         */
        try {
            FileOutputStream fOutputStream = context.openFileOutput(String.valueOf(id), Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fOutputStream);
            outputStream.writeObject(this);
            outputStream.close();
            fOutputStream.close();
            if(Closet.contains(id)) {
                //update this article in the closet
                Closet.update(id, this);
            } else {
                //add this new article to the closet
                Closet.add(this);
            }
            return id;

        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Determines if a de-serialized file is compatible with this class.
     *
     * Maintainers must change this value if and only if the new version
     * of this class is not compatible with old versions. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html> details. </a>
     *
     * Not necessary to include in first version of the class, but
     * included here as a reminder of its importance.
     */
    private static final long serialVersionUID = 7526471155622776147L;
}
