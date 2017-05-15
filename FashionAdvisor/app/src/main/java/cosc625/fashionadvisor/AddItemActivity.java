package cosc625.fashionadvisor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;



import clothing.*;

//TODO: add temperature range and color picker
//TODO: create article item from inputs
public class AddItemActivity extends AppCompatActivity {
    EditText itemName;
    Spinner itemType, textureType, idealTemp, formalityLevel;
    CheckBox patternCheckBox;
    Button confirm;
    ImageView articlePic;
    ColorPickerView colorPicker;

    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        intent = getIntent();
        bundle = intent.getExtras();

        itemName = (EditText) findViewById(R.id.addItemName);
        itemType = (Spinner) findViewById(R.id.spinner_items);
        textureType = (Spinner) findViewById(R.id.textures);
        idealTemp = (Spinner) findViewById(R.id.idealTemps);
        formalityLevel = (Spinner) findViewById(R.id.formalityLevel);
        patternCheckBox = (CheckBox) findViewById(R.id.checkBox);
        confirm = (Button) findViewById(R.id.confirmButton);
        articlePic = (ImageView) findViewById(R.id.articlePicture);
        colorPicker = (ColorPickerView) findViewById(R.id.color_picker);

        articlePic.setImageBitmap((Bitmap) bundle.get("Image"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createArticleAndExit();
            }
        });
    }

    private void createArticleAndExit() {
        Context context = getApplicationContext();
        Texture tex = Texture.valueOf(textureType.getSelectedItem().toString());
        Temperature ideal = Temperature.valueOf(idealTemp.getSelectedItem().toString());
        Formality formality = Formality.valueOf(formalityLevel.getSelectedItem().toString());
        String name = itemName.getText().toString();
        int color = colorPicker.getColor();
        boolean patterned = patternCheckBox.isChecked();

        //we'll ignore shorts/longsleeves since they aren't used in matching right now
        switch (itemType.getSelectedItem().toString()) {
            case "Shirt":
                new Shirt(context, tex, ideal, formality, name, color, patterned,
                        (Bitmap) bundle.get("Image"), false).save();
                break;
            default: //pants
                new Pants(context, tex, ideal, formality, name, color, patterned,
                        (Bitmap) bundle.get("Image"), false).save();
        }
        finish();
    }

}
    /* This will not ever execute. This activity is not a fragment.
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.wardrobe, container, false);
        itemName = (EditText) findViewById(R.id.addItemName);
        itemType = (Spinner) findViewById(R.id.spinner_items);


        return view;
    }
    */

