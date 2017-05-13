package cosc625.fashionadvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.github.danielnilsson9.colorpickerview.*;
import com.yahoo.mobile.client.android.util.rangeseekbar.*;

import clothing.*;

//TODO: add temperature range and color picker
//TODO: create article item from inputs
//TODO: add image handling
public class AddItemActivity extends AppCompatActivity {
    EditText itemName;
    Spinner itemType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.wardrobe, container, false);
        itemName = (EditText) findViewById(R.id.addItemName);
        itemType = (Spinner) findViewById(R.id.spinner_items);


        return view;
    }
}
