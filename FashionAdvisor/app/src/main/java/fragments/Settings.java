package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import org.florescu.android.rangeseekbar.*;

import cosc625.fashionadvisor.R;

/**
 * Created by Matt on 5/4/17.
 * Modified by Alison on 5/10/17.
 */

//TODO: Add firstRun functionality.
public class Settings extends Fragment {

    public static final int minTemp = -20;
    public static final int maxTemp = 110;

    RadioGroup radioGender;
    EditText nameBox;
    Button enterButton;
    TextView display;
    RangeSeekBar<Integer> rangeSeekBar;
    SharedPreferences prefs;

    public Settings() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        final Context context = getActivity().getApplicationContext();
        final View view = inflater.inflate(R.layout.settings, container, false);
        radioGender = (RadioGroup) view.findViewById(R.id.radioSex);
        nameBox = (EditText)view.findViewById(R.id.editText);
        enterButton = (Button) view.findViewById(R.id.button2);
        display = (TextView)view.findViewById(R.id.displayName);
        rangeSeekBar = (RangeSeekBar<Integer>) view.findViewById(R.id.rangeSeekBar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        rangeSeekBar.setRangeValues(minTemp, maxTemp);
        rangeSeekBar.setTextAboveThumbsColor(Color.rgb(10, 10, 10));
        rangeSeekBar.setSelectedMinValue(prefs.getInt("PreferredMin", 38));//below this is COLD
        rangeSeekBar.setSelectedMaxValue(prefs.getInt("PreferredMax", 80));//above this is HOT

        enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newName = nameBox.getText().toString();
                String newGender = ((RadioButton)view.findViewById(radioGender.getCheckedRadioButtonId())).getText().toString();
                writeUser(newName,newGender,context);
                display.setText(newName);
                view.invalidate();
                //toast(context, "Button pressed!");
            }
        });

        String nameFromFile="";
        String genderFromFile="";
        try {
            File uinfo = new File(context.getFilesDir()+"/user.info");
            Scanner scan = new Scanner(uinfo);
            String name = scan.nextLine();
            String gender = scan.nextLine();
            scan.close();
            nameFromFile=name;
            genderFromFile=gender;
        } catch (FileNotFoundException e) {
            //toast(context, "user.info not found!");
        }

        switch(genderFromFile)
        {
            case "Male":
                radioGender.check(R.id.radioMale);
                break;
            case "Female":
                radioGender.check(R.id.radioFemale);
                break;
            default:
                radioGender.check(R.id.radioOther);
                
        }
        display.setText(nameFromFile);
        return view;
    }// end onCreateView()


    public void writeUser(String name, String gender, Context context)
    {
        try {
            FileOutputStream outputStream = context.openFileOutput("user.info", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            //toast(context,"Writing: "+name+"+"+gender);
            String out = name+"\n"+gender;
            System.out.println(out);
            osw.write(out);
            osw.close();
            outputStream.close();
        } catch (IOException e)
        {
            toast(context,"IOException");
        }

        prefs.edit().putInt("PreferredMin", rangeSeekBar.getSelectedMinValue()).apply();
        prefs.edit().putInt("PreferredMax", rangeSeekBar.getSelectedMaxValue()).apply();
    }
    public void toast(Context context, CharSequence message)
    {
        Toast mess = Toast.makeText(context, message,Toast.LENGTH_SHORT);
        mess.show();
    }

}
