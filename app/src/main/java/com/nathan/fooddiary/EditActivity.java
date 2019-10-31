package com.nathan.fooddiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

    private FoodViewModel mFoodDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String foodDate = getIntent().getStringExtra(MainActivity.EXTRA_FOOD);
        Log.d(TAG, "The extra is: " + foodDate);
        mEditImage =findViewById(R.id.edit_photo);
        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        mEditTags = findViewById(R.id.edit_tags);
        mBackButton = findViewById(R.id.back_button);
        mSaveButton = findViewById(R.id.save_button);

        mFoodDatabase = new FoodViewModel(getApplication());

        mFoodDatabase.getRecordForDate(foodDate).observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                diaryEntry = food;
                //mEditImage.setImageDrawable(null);
                mEditTitle.setText(food.getTitle());
                mEditDescription.setText(food.getDescription());
                mEditTags.setText(food.getDescription());
            }
        });
    }
}
