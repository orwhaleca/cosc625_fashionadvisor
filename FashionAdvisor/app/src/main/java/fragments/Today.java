package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import cosc625.fashionadvisor.Closet;
import cosc625.fashionadvisor.R;

/**
 * Created by Matt on 5/4/17.
 */

//TODO: create our outfit matching algorithm here
//TODO: like/dislike system to learn from user input
public class Today extends Fragment {

    TextView textView;

    public Today() {
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
        View view = inflater.inflate(R.layout.today, container, false);
        textView = (TextView) view.findViewById(R.id.text);

        textView.setText(Closet.getString());
        System.out.println("Closet: " + Closet.getString());

        //TODO: Closet method to get the lists of tops and bottoms

        //TODO: add button and get button click event here

        return view;
    }

}
