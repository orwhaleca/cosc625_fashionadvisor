package cosc625.fashionadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Map;

import clothing.Article;
import clothing.Formality;
import clothing.Pants;
import clothing.Shirt;
import clothing.Temperature;
import clothing.Texture;

public class Splash extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        Shirt shirt = new Shirt(getApplicationContext(), Texture.COTTON, Temperature.WARM,
                Formality.INFORMAL, "firstShirt", 123,
                Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8), false);
        shirt.save();
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        printPrefs();//for debugging
        handleFirstRun();
        readClothes();

        //any additional setup steps
        //switch to tabbed view
        Intent intent = new Intent(this, MainTabbed.class);
        startActivity(intent);
    }

    private void printPrefs() {
        Map<String, ?> m = prefs.getAll();

        for (Map.Entry<String, ?> entry : m.entrySet()) {
            Log.i("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    /** check shared preferences for our app to see if this is the first run
     * (we'll set first run to false after the tutorial screen close button is pressed.)
     */
    private void handleFirstRun() {
        if (prefs.getBoolean("firstRun", true)) {

            //do our first time setup here
            System.out.println("It's our first run!");
            //TODO: create a layout for first-time tutorial

            //set first run to false
            prefs.edit().putBoolean("firstRun", false).commit();
        }
    }

    private void readClothes() {
        //read serialized files for clothes entries
        InputStream input;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        String json, temp;
        JsonObject jsonObject;

        int maxID = prefs.getInt("highestID", -1) + 1;
        for (int i = 0; i < maxID; i++) {
            try {
                //read in the files by id number
                input = getApplicationContext().openFileInput(String.valueOf(i));
                if ( input != null ) {
                    System.out.print("Read from file: " + i);
                    inputStreamReader = new InputStreamReader(input);
                    bufferedReader = new BufferedReader(inputStreamReader);

                    while( (temp = bufferedReader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }

                    json = stringBuilder.toString();

                    System.out.println(" " + json);

                    //parse json using Gson
                    Gson gson = new Gson();
                    jsonObject = gson.fromJson(json, JsonObject.class);

                    makeArticle(jsonObject);

                }//end if input!=null

            } catch (FileNotFoundException e) {
                System.out.println("File was not found: " + e.getLocalizedMessage());
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end for
    }

    private void makeArticle(JsonObject jsonObject) {
        String type = jsonObject.getAsJsonPrimitive("type").getAsString();

        switch (type) {
            case "clothing.Shirt" :
                //make a shirt and load it
                makeShirt(jsonObject);
                break;
            case "clothing.Pants" :
                //make pants and load them
                makePants(jsonObject);
                break;
        }
    }

    private void makeShirt(JsonObject jsonObject) {
        new Shirt(jsonObject.getAsJsonPrimitive("id").getAsInt(),
                getApplicationContext(),
                Texture.valueOf(jsonObject.getAsJsonPrimitive("texture").getAsString()),
                Temperature.valueOf(jsonObject.getAsJsonPrimitive("idealTemp").getAsString()),
                Formality.valueOf(jsonObject.getAsJsonPrimitive("formality").getAsString()),
                jsonObject.getAsJsonPrimitive("name").getAsString(),
                jsonObject.getAsJsonPrimitive("color").getAsInt(),
                BitmapHandler.StringToBitMap(jsonObject.getAsJsonPrimitive("img").getAsString()),
                jsonObject.getAsJsonPrimitive("longSleeves").getAsBoolean()).load();
    }

    private void makePants(JsonObject jsonObject) {
        new Pants(jsonObject.getAsJsonPrimitive("id").getAsInt(),
                getApplicationContext(),
                Texture.valueOf(jsonObject.getAsJsonPrimitive("texture").getAsString()),
                Temperature.valueOf(jsonObject.getAsJsonPrimitive("idealTemp").getAsString()),
                Formality.valueOf(jsonObject.getAsJsonPrimitive("formality").getAsString()),
                jsonObject.getAsJsonPrimitive("name").getAsString(),
                jsonObject.getAsJsonPrimitive("color").getAsInt(),
                BitmapHandler.StringToBitMap(jsonObject.getAsJsonPrimitive("img").getAsString()),
                jsonObject.getAsJsonPrimitive("shorts").getAsBoolean()).load();
    }
}
