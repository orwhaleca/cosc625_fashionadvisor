package cosc625.fashionadvisor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.github.danielnilsson9.colorpickerview.*;
import org.florescu.android.rangeseekbar.*;

import clothing.*;

//TODO: add temperature range and color picker
//TODO: create article item from inputs
public class AddItemActivity extends AppCompatActivity {
    EditText itemName;
    Spinner itemType;
    Button confirm;
    ImageView articlePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        itemName = (EditText) findViewById(R.id.addItemName);
        itemType = (Spinner) findViewById(R.id.spinner_items);
        confirm = (Button) findViewById(R.id.confirmButton);
        articlePic = (ImageView) findViewById(R.id.articlePicture);

        articlePic.setImageBitmap((Bitmap) bundle.get("Image"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createArticleAndExit();
            }
        });
    }

    private void createArticleAndExit() {
        switch (itemType.getSelectedItem().toString()) {
            case "Shirt":

                break;
            default: //pants

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

