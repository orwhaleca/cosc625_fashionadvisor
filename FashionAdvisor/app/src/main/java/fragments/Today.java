package fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
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
 *
 * Handles outfit matching and appropriate layouts
 */

//TODO: create our outfit matching algorithm here
//TODO: like/dislike system to learn from user input
public class Today extends Fragment {

    TextView slideTrackerText;
    TextView minTemp, maxTemp;
    Button addSampleButton, makeOutfit;
    SeekBar tempSlider;
    ImageView topView, botView;
    int temperature;
    float tempTextY, tempTextPadding;

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
        slideTrackerText = (TextView) view.findViewById(R.id.sliderTrackText);
        minTemp = (TextView) view.findViewById(R.id.minTemp);
        maxTemp = (TextView) view.findViewById(R.id.maxTemp);
        addSampleButton = (Button) view.findViewById(R.id.addSample);
        makeOutfit = (Button) view.findViewById(R.id.makeOutfit);
        tempSlider = (SeekBar) view.findViewById(R.id.tempBar);
        topView = (ImageView) view.findViewById(R.id.topView);
        botView = (ImageView) view.findViewById(R.id.botView);

        //get 10dp
        tempTextPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        minTemp.setText(Settings.minTemp + "F");
        maxTemp.setText(Settings.maxTemp + "F");

        topView.setScaleType(ImageView.ScaleType.FIT_XY);
        botView.setScaleType(ImageView.ScaleType.FIT_XY);

        //closetToText();

        addSampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSample(v);
            }
        });

        makeOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOutfit();
            }
        });

        tempSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                getSliderUpdate(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nothing to do right now
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nothing to do right now
            }
        });

        //TODO: Closet method to get the lists of tops and bottoms

        makeOutfit();

        return view;
    }

    private void closetToText() {
        slideTrackerText.setText(Closet.getString());
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

    private void makeOutfit() {
        int[] colors = new int[100];
        for(int i = 0; i < colors.length-1; i++) {
            colors[i] = 16008514;//red
        }
        System.out.println();
        Bitmap b = Bitmap.createBitmap(colors, 10, 10, Bitmap.Config.ALPHA_8);
        topView.setImageBitmap(b);
        botView.setImageBitmap(b);
        topView.invalidate();
        botView.invalidate();
    }

    private void getSliderUpdate(int progress) {
        int range = Settings.maxTemp - Settings.minTemp;
        temperature = (int) (progress * (range / 100.0) + Settings.minTemp);
        slideTrackerText.setText(String.valueOf(temperature));

        int width = tempSlider.getWidth()
                - tempSlider.getPaddingLeft()
                - tempSlider.getPaddingRight();
        int thumbPos = tempSlider.getPaddingLeft()
                + tempSlider.getLeft()
                + width
                * progress
                / tempSlider.getMax();

        int [] sliderLocation = new int[2];
        tempSlider.getLocationOnScreen(sliderLocation);
        tempTextY = sliderLocation[1] - tempTextPadding;

        slideTrackerText.setX(thumbPos);
        slideTrackerText.setY(tempTextY);
        slideTrackerText.bringToFront();
    }

}
