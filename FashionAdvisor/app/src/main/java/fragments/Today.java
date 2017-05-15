package fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import clothing.Article;
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

//TODO: like/dislike system to learn from user input
public class Today extends Fragment {

    TextView slideTrackerText;
    TextView minTemp, maxTemp;
    Button addSampleButton, makeOutfit;
    SeekBar tempSlider;
    ImageView topView, botView;
    RadioGroup radioGroup;
    RadioButton formal, businessCasual, casual, athletic, sleepwear;
    SharedPreferences prefs;

    int temperature = 70;
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
        radioGroup = (RadioGroup) view.findViewById(R.id.formalityRadio);
        formal = (RadioButton) view.findViewById(R.id.formalButton);
        businessCasual = (RadioButton) view.findViewById(R.id.businessCasualButton);
        casual = (RadioButton) view.findViewById(R.id.casualButton);
        athletic = (RadioButton) view.findViewById(R.id.athleticButton);
        sleepwear = (RadioButton) view.findViewById(R.id.sleepwearButton);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        //get 10dp
        tempTextPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        minTemp.setText(Settings.minTemp + "F");
        maxTemp.setText(Settings.maxTemp + "F");

        topView.setScaleType(ImageView.ScaleType.FIT_XY);
        botView.setScaleType(ImageView.ScaleType.FIT_XY);

        radioGroup.check(R.id.formalButton);

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

        makeOutfit();

        return view;
    }

    private void makeOutfit() {
        if(Closet.getSize() > 1) makeOutfitHelper();
    }

    private void makeOutfitHelper() {
        Formality formality;
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.formalButton:
                formality = Formality.FORMAL;
                break;
            case R.id.businessCasualButton:
                formality = Formality.BUSINESS_CASUAL;
                break;
            case R.id.casualButton:
                formality = Formality.CASUAL;
                break;
            case R.id.athleticButton:
                formality = Formality.ATHLETIC;
                break;
            default:
                formality = Formality.SLEEPWEAR;
        }
        Article[] articles = Closet.getOutFit(temperature, formality, prefs);
        if(articles[0] != null) {
            topView.setImageBitmap(articles[0].getImage());
        } else {
            topView.setImageResource(android.R.drawable.ic_delete);
        }
        if(articles[1] != null) {
            botView.setImageBitmap(articles[1].getImage());
        } else {
            botView.setImageResource(android.R.drawable.ic_delete);
        }
    }

    private void addSample(View view) {

        String[] imageUrlArr = {"http://pngimg.com/uploads/tshirt/tshirt_PNG5434.png",//white shirt
                                "http://www.beautybyyasmin.com/wp-content/uploads/2015/07/denim-jeans-for-men-5.jpg",//denim jeans
                                "http://partycity6.scene7.com/is/image/PartyCity/_ml_p2p_pc_badge_taller15?$_ml_p2p_pc_thumb_taller15$&$product=PartyCity/P652754_full",//christmas sweater
                                "http://c.shld.net/rpx/i/s/pi/mp/22057/prod_5685140427?src=https%3A%2F%2Fd3d71ba2asa5oz.cloudfront.net%2F33000706%2Fimages%2Fezsnowpremiuma1027.jpg&d=65fb4f8d944aa749076aa9168b066417e949cbba&hei=245&wid=245&op_sharpen=1&qlt=85",//snow pants
                                "https://media.gazman.com.au/media/catalog/product/cache/1/image/700x700/9df78eab33525d08d6e5fb8d27136e95/g/a/gazman-mens-business-shirt-bshw17030t_508_0215.png",//dress shirt
                                "https://www.rvca.com/media/filter/m/img/mb901cht_grs_1.jpg",//tank top
                                "http://cdn.connor.com.au/live/connor/product/images/1541129/c15sj304_ink_1_220_265.png",//suit jacket
                                "http://images.menswearhouse.com/is/image/TMW/MW40_228X_02_JOSEPH_FEISS_BLACK_SOLID_MAIN?01AD=3R3VTebFqNhKneovj4hwr2dxCGszUEoSuWgDlD5Mn_AC_KLlsHqkxHg&01RI=8DE3BA8AFE17DB0&01NA=&$40GridLrg$",
                                "https://ae01.alicdn.com/kf/HTB1_AQnLVXXXXX_XpXXq6xXFXXXs/S-XL-Fashion-font-b-Women-b-font-Capris-font-b-pants-b-font-font-b.jpg"};//suit pants
        String[][] params = new String[imageUrlArr.length][2];
        for(int i = 0; i < params.length; i++) {
            params[i][0] = imageUrlArr[i];
            params[i][1] = String.valueOf(i);
            new AsyncGetBitmapFromUrl().execute(params[i]);//Do not overwrite async params
        }
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
                / tempSlider.getMax()
                - (int) tempTextPadding / 2;

        int [] sliderLocation = new int[2];
        tempSlider.getLocationOnScreen(sliderLocation);
        tempTextY = sliderLocation[1] - tempTextPadding;

        slideTrackerText.setX(thumbPos);
        slideTrackerText.setY(tempTextY);
        slideTrackerText.bringToFront();
    }

    private void makeArticle(Bitmap bitmap, int sampleNum) {

        Article a;
        switch(sampleNum) {
            case 0:
                a = new Shirt(this.getContext(), Texture.COTTON, Temperature.WARM,
                        Formality.CASUAL, "White Shirt", Color.rgb(227, 227, 227),//white in hex
                        false, bitmap, false);
                break;
            case 1:
                a = new Pants(this.getContext(), Texture.DENIM, Temperature.COOL,
                        Formality.CASUAL, "Denim Jeans", Color.rgb(47, 53, 67),//blue in hex
                        false, bitmap, false);
                break;
            case 2:
                a = new Shirt(this.getContext(), Texture.FLEECE, Temperature.COLD,
                        Formality.CASUAL, "Christmas Sweater", Color.rgb(0, 148, 70),//green
                        true, bitmap, true);
                break;
            case 3:
                a = new Pants(this.getContext(), Texture.POLYESTER, Temperature.COLD,
                        Formality.ATHLETIC, "Snow Pants", Color.rgb(28, 30, 27),//black
                        false, bitmap, false);
                break;
            case 4:
                a = new Shirt(this.getContext(), Texture.COTTON, Temperature.COOL,
                        Formality.BUSINESS_CASUAL, "Dress Shirt", Color.rgb(200, 178, 218),//plum?
                        true, bitmap, true);
                break;
            case 5:
                a = new Shirt(this.getContext(), Texture.COTTON, Temperature.HOT,
                        Formality.ATHLETIC, "Tank Top", Color.rgb(142,142,142),//gray
                        false, bitmap, false);
                break;
            case 6:
                a = new Shirt(this.getContext(), Texture.POLYESTER, Temperature.COOL,
                        Formality.FORMAL, "Suit Jacket", Color.rgb(48,56,70),//slate gray
                        false, bitmap, true);
                break;
            case 7:
                a = new Pants(this.getContext(), Texture.POLYESTER, Temperature.COOL,
                        Formality.FORMAL, "Suit Pants", Color.rgb(31,30,28),//black
                        false, bitmap, false);
                break;
            case 8:
                a = new Pants(this.getContext(), Texture.SPANDEX, Temperature.WARM,
                        Formality.CASUAL, "Checkered Capris", Color.rgb(235,241,143),
                        true, bitmap, false);
                break;
            default:
                a = new Shirt(this.getContext(), Texture.COTTON, Temperature.WARM,
                        Formality.CASUAL, "firstShirt", Color.rgb(227, 227, 227),//white in hex
                        false, bitmap, false);
        }
        a.save();
        makeOutfit();
    }

    private class AsyncGetBitmapFromUrl extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Object[] arr = new Object[2];
                arr[0] = myBitmap;
                arr[1] = Integer.valueOf(params[1]);
                return arr;
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Object[] arr) {
            Bitmap bitmap = (Bitmap) arr[0];
            Integer counter = (Integer) arr[1];
            if(bitmap == null) System.out.println("BITMAP IS NULL");
            System.out.println("Adding Sample " + counter);
            makeArticle(bitmap, counter);
        }
    }
}
