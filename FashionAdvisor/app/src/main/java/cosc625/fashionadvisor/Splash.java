package cosc625.fashionadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import clothing.Article;

public class Splash extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        FileInputStream fInputStream;
        ObjectInputStream inputStream;

        //TODO: need a global variable stored in preferences that keeps track of the highest id #
        for (int i = 0; i < 999; i++) {
            try {
                //read in the files by id number
                fInputStream = getApplicationContext().openFileInput(String.valueOf(i));
                inputStream = new ObjectInputStream(fInputStream);
                //TODO: maybe this should be article.save()
                Closet.add((Article) inputStream.readObject());

            } catch (FileNotFoundException e) {
                System.out.println("File was not found: " + e.getLocalizedMessage());
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("An article's class was not read correctly at startup.");
            }

        }//end for
    }
}
