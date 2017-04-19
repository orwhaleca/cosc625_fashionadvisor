package cosc625.fashionadvisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

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

        Map<String, ?> m = prefs.getAll();

        for (Map.Entry<String, ?> entry : m.entrySet()) {
            Log.i("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }

        /* check shared preferences for our app to see if this is the first run
         * (we'll set first run to false after the tutorial screen close button is pressed.)
         */
        if(prefs.getBoolean("firstRun", true)) {

            //do our first time setup here
            System.out.println("It's our first run!");

            //set first run to false
            prefs.edit().putBoolean("firstRun", false).commit();
        }

        //read JSON files for clothes entries
        //any additional setup step
        //switch to tabbed view
    }
}
