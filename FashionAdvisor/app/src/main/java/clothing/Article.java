package clothing;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.gson.*;

import cosc625.fashionadvisor.BitmapHandler;
import cosc625.fashionadvisor.Closet;

/**
 * Created by Matt on 5/9/17.
 * <p>
 * Super class for all clothing classes
 */

public abstract class Article {

    protected Context context;
    protected int id;//start at 0 and go up.
    private Texture texture;
    private Temperature idealTemp;
    private Formality formality;
    private String name;
    private int color;  //expressed as hex RGB
    private Bitmap image;
    JsonObject articleData;

    public Article(Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        context = con;
        texture = tex;
        idealTemp = temp;
        formality = form;
        name = n;
        color = col;
        image = img;
        //assign an id
        String highestID = "highestID";
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        id = prefs.getInt(highestID, -1) + 1;//minimum id is 0, normal id is current highestID + 1
        articleData = new JsonObject();
        articleData.addProperty("type", getClass().getName());
    }

    public Article(int id, Context con, Texture tex, Temperature temp, Formality form, String n, int col, Bitmap img) {
        context = con;
        texture = tex;
        idealTemp = temp;
        formality = form;
        name = n;
        color = col;
        image = img;
        this.id = id;
        articleData = new JsonObject();
        articleData.addProperty("type", getClass().getName());
    }

    public int getID() {
        return id;
    }

    public Context getContext() {
        return context;
    }

    /**
     * This method will save the current article of clothing to the disk
     * and also add it to the current working set of clothing. For most areas
     * of the application, save() should be used. Only when loading in files
     * from the disk is it acceptable to use load() instead.
     *
     * @return positive integer id if save successful, -1 if save failed
     */
    public int save() {
        Gson gson = new Gson();

        //Since Article is abstract, the only objects calling save() are subclasses
        //.getFields() will not include inherited fields, so the fields of article
        //are statically added below.
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if(Modifier.isStatic(f.getModifiers())) continue;//ignore
                if(!f.isAccessible()) f.setAccessible(true);

                if (f.getType() == Integer.class) {
                    articleData.addProperty(f.getName(), (int) f.get(this));
                } else if (f.getType() == Boolean.class) {
                    articleData.addProperty(f.getName(), (boolean) f.get(this));
                } else if (f.getType() == String.class) {
                    articleData.addProperty(f.getName(), (String) f.get(this));
                } else {
                    //reserved for possible future data types
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

        articleData.addProperty("id", id);
        articleData.addProperty("texture", texture.name());
        articleData.addProperty("idealTemp", idealTemp.name());
        articleData.addProperty("formality", formality.name());
        articleData.addProperty("name", name);
        articleData.addProperty("color", color);
        articleData.addProperty("img", BitmapHandler.BitMapToString(image));

        String payload = gson.toJson(articleData);

        try {
            OutputStreamWriter streamWriter = new OutputStreamWriter(
                    context.openFileOutput(String.valueOf(id), Context.MODE_PRIVATE));
            BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
            bufferedWriter.write(payload);
            bufferedWriter.close();
            System.out.println("File written: " + id + " " + payload);

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return load();
    }

    public int load() {
        if (Closet.contains(id)) {
            //update this article in the closet
            Closet.update(id, this);
        } else {
            //add this new article to the closet
            Closet.add(this);
        }
        return id;
    }

    @Override
    public String toString() {
        //TODO: for efficiency, this should be StringBuilder
        String n;
        String temp = "id: " + id + ", texture: " + texture + ", idealTemp: " + idealTemp
                + ", formality: " + formality + ", name: " + name + ", color: #" + color;

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                //if(n.equals("$change") || n.equals("serialVersionUID")) continue; //ignore - a bug?
                if(Modifier.isStatic(f.getModifiers())) continue;//ignore
                if(!f.isAccessible()) f.setAccessible(true);
                temp = temp.concat(", " + f.getName() + ": " + f.get(this).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

        return temp;
    }
}
