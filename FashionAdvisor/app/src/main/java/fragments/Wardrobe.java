package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cosc625.fashionadvisor.AddItemActivity;
import cosc625.fashionadvisor.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Matt on 5/4/17.
 *
 * The wardrobe class allows the user to view and edit their saved clothes.
 */

//TODO: Allow users to modify clothing here
public class Wardrobe extends Fragment {

    TextView textView;
    ImageView imageView;
    FloatingActionButton button;

    private final int REQUEST_IMAGE_CAPTURE = 1;

    public Wardrobe() {
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
        View view = inflater.inflate(R.layout.wardrobe, container, false);
        textView = (TextView) view.findViewById(R.id.myText);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        button = (FloatingActionButton) view.findViewById(R.id.addClothingButton);

        //we could use the xml solution of android:onClick, but then our button methods
        //need to be defined in MainTabbed rather than the fragment classes
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhotoClick(v);
            }
        });

        return view;
    }

    public void addPhotoClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            textView.setText("Adding article to wardrobe.");

            //load the addItem activity
            Intent addItem = new Intent(getActivity(), AddItemActivity.class);
            addItem.putExtra("Image", imageBitmap);
            startActivity(addItem);
        } else {
            textView.setText("Article not added to wardrobe.");
        }
    }
}
