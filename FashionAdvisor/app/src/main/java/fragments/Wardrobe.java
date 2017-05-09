package fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cosc625.fashionadvisor.PermissionHandler;
import cosc625.fashionadvisor.R;

/**
 * Created by Matt on 5/4/17.
 */

//TODO: Allow users to modify / add clothing here
public class Wardrobe extends Fragment {

    TextView textView;
    FloatingActionButton button;

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
        System.out.println("Button pressed");
        textView.setText("You've indicated that you would like to take a picture for the wardrobe.");

        /* This was all pointless?
        //not certain that getActivity is the best option here
        PermissionHandler.checkPermission(getActivity(), Manifest.permission.CAMERA);

        //I think we let the permission handler call us back to call the method which changes
        //the intent...


        //need to check permission for camera before executing
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
        */

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);//change this 1 to class constant
        }
    }
}
