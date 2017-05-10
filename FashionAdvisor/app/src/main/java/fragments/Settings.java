package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cosc625.fashionadvisor.R;

/**
 * Created by Matt on 5/4/17.
 * Modified by Alison on 5/10/17.
 */

//TODO: Implement user settings - please use clothing.Temperature to define ranges of the temp slider
//TODO: Make buttons do things
public class Settings extends Fragment {

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
        return inflater.inflate(R.layout.settings, container, false);
    }

}
