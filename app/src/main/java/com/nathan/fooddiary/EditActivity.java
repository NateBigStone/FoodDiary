package com.nathan.fooddiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class EditActivity extends AppCompatActivity {

    Food diaryEntry;
    ImageView mEditImage;
    EditText mEditTitle;
    EditText mEditDescription;
    EditText mEditTags;
    ImageButton mBackButton;
    ImageButton mSaveButton;
    String TAG = "EDITACTIVITY";

    //TODO: Stretch goal - have tags be an array. The tags could be a recyclerview of buttons

    private FoodViewModel mFoodDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mEditImage =findViewById(R.id.edit_photo);
        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        mEditTags = findViewById(R.id.edit_tags);
        mBackButton = findViewById(R.id.back_button);
        mSaveButton = findViewById(R.id.save_button);

        mFoodDatabase = new FoodViewModel(getApplication());

        if (this.getIntent().getExtras() != null) {
            final String foodDate = getIntent().getStringExtra(MainActivity.EXTRA_FOOD);
            //TODO: put get intent in a try/catch
            Log.d(TAG, "The extra is: " + foodDate);
            mFoodDatabase.getRecordForDate(foodDate).observe(this, new Observer<Food>() {
                @Override
                public void onChanged(Food food) {
                    diaryEntry = food;
                    //mEditImage.setImageDrawable(null);
                    mEditTitle.setText(food.getTitle());
                    mEditDescription.setText(food.getDescription());
                    mEditTags.setText(food.getTags());
                }
            });
        }

        mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Do the photo things
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Check to see if there have been any changes and toast
                finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mEditTitle.getText().toString();
                String newDescription = mEditDescription.getText().toString();
                String newTags = mEditTags.getText().toString();

                if (diaryEntry != null) {
                    diaryEntry.setTitle(newTitle);
                    diaryEntry.setDescription(newDescription);
                    diaryEntry.setTags(newTags);
                    mFoodDatabase.update(diaryEntry);
                    Toast.makeText(EditActivity.this, "Entry Saved", Toast.LENGTH_LONG).show();
                } else {
                    Food newEntry = new Food(newTitle, newDescription, ".", newTags);
                    mFoodDatabase.insert(newEntry);
                    Toast.makeText(EditActivity.this, "New Entry Saved", Toast.LENGTH_LONG).show();
                }
            }});
    }
}