package com.nathan.fooddiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {

    ImageView mEditImage;
    EditText mEditTitle;
    EditText mEditDescription;
    EditText mEditTags;
    ImageButton mBackButton;
    ImageButton mSaveButton;
    String TAG = "EDITACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Food editFood = getIntent().getExtras((MainActivity.EXTRA_FOOD));
        //Log.d(TAG, "The extra is: " + editFood);
        mEditTitle = findViewById(R.id.edit_title);
        mEditTitle.setText("Title Placeholder");
    }
}
