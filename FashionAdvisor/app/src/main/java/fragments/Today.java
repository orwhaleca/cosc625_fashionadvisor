package fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import clothing.Formality;
import clothing.Pants;
import clothing.Shirt;
import clothing.Temperature;
import clothing.Texture;
import cosc625.fashionadvisor.Closet;
import cosc625.fashionadvisor.R;

/**
 * Created by Matt on 5/4/17.
 */

//TODO: create our outfit matching algorithm here
//TODO: like/dislike system to learn from user input
public class Today extends Fragment {

    TextView textView;
    Button addSampleButton;

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
        addSampleButton = (Button) view.findViewById(R.id.addSample);

        closetToText();

        addSampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSample(v);
            }
        });

        //TODO: Closet method to get the lists of tops and bottoms

        //TODO: add button and get button click event here

        return view;
    }

    private void closetToText() {
        textView.setText(Closet.getString());
        System.out.println("Closet: " + Closet.getString());
    }

    private void addSample(View view) {
        new Shirt(view.getContext(), Texture.COTTON, Temperature.WARM,
                Formality.INFORMAL, "firstShirt", 16777215,//white in hex
                Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8), false).save();

        new Pants(view.getContext(), Texture.DENIM, Temperature.COOL,
                Formality.INFORMAL, "firstPants", 2574278,//blue in hex
                Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8), false).save();

        closetToText();
    }

}
