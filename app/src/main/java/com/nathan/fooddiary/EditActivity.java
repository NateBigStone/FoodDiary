package com.nathan.fooddiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    Food diaryEntry;
    ImageButton mEditImage;
    String mImagePath;
    EditText mEditTitle;
    EditText mEditDescription;
    EditText mEditTags;
    ImageButton mBackButton;
    ImageButton mSaveButton;
    String TAG = "EDITACTIVITY";

    //TODO: Stretch goal - have tags be an array. The tags could be a recyclerview of buttons

    private FoodViewModel mFoodDatabase;

    private final static String BUNDLE_KEY_MOST_RECENT_FILE_PATH = "bundle key most recent file path";
    private static final int PHOTO_REQUEST_CODE = 1;

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
                    mImagePath = food.getImagePath();
                    mEditTitle.setText(food.getTitle());
                    mEditDescription.setText(food.getDescription());
                    mEditTags.setText(food.getTags());
                    loadImage();
                }
            });
        }

        mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d(TAG, "Photo intent");
                if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        File imageFile = createImageFile();
                        if (imageFile != null) {
                            Uri imageURI = FileProvider.getUriForFile(EditActivity.this, "com.nathan.fooddiary.fileprovider", imageFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                            startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
                        } else {
                            Log.e(TAG, "Image file is null");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error creating image file " + e);
                    }
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Check to see if there have been any changes and toast
                if (diaryEntry != null) {
                    if (!mImagePath.equals(diaryEntry.getImagePath()) ||
                            !mEditTitle.getText().toString().equals(diaryEntry.getTitle()) ||
                            !mEditDescription.getText().toString().equals(diaryEntry.getDescription()) ||
                            !mEditTags.getText().toString().equals(diaryEntry.getTags())
                    ) {
                        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(EditActivity.this)
                                .setMessage(getString(R.string.go_back_message))
                                .setTitle(getString(R.string.go_back_dialog_title))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //go back
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .create();
                        confirmDeleteDialog.show();
                        return;
                    }
                }
                finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newImagePath = mImagePath;
                String newTitle = mEditTitle.getText().toString();
                String newDescription = mEditDescription.getText().toString();
                String newTags = mEditTags.getText().toString();

                if (diaryEntry != null) {
                    diaryEntry.setImagePath(newImagePath);
                    diaryEntry.setTitle(newTitle);
                    diaryEntry.setDescription(newDescription);
                    diaryEntry.setTags(newTags);
                    mFoodDatabase.update(diaryEntry);
                    Toast.makeText(EditActivity.this, "Entry Saved", Toast.LENGTH_LONG).show();
                } else {
                    Food newEntry = new Food(newTitle, newDescription, newImagePath, newTags);
                    mFoodDatabase.insert(newEntry);
                    Toast.makeText(EditActivity.this, "New Entry Saved", Toast.LENGTH_LONG).show();
                }
            }});
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult for request code " + requestCode + " and current path " + mImagePath);
            loadImage();
            requestSaveImageToMediaStore();
        } else if (resultCode == RESULT_CANCELED) {
            mImagePath = "";
        }
    }

    private File createImageFile() throws IOException {
        //create unigue file nam w time stomp
        String imageFilename = "Diary_" + new Date().getTime();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFilename,
                ".jpg",
                storageDir
        );
        mImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void loadImage() {
        Log.d(TAG, "THE IMAGE PATH IS " + mImagePath);
        if (mImagePath != null && mImagePath.length() > 5) {
            Picasso.get()
                    .load(new File(mImagePath))
                    .error(android.R.drawable.stat_notify_error) // built-in error icon
                    .fit()
                    .centerCrop()
                    .into(mEditImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image loaded");
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "error loading image", e);
                        }
                    });

        }
        else{
            mEditImage.setImageResource(R.drawable.icons8_camera_80);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        //retains imagepath
        super.onSaveInstanceState(outBundle);
        outBundle.putString(BUNDLE_KEY_MOST_RECENT_FILE_PATH, mImagePath);
    }

    private void requestSaveImageToMediaStore() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            saveImage();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {
        if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImage();
        }
        else {
            Toast.makeText(this, "Images will NOT be saved to media store", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImage() {
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), mImagePath, "FoodDiary", "FoodDiary");
        } catch (IOException e) {
            Log.e(TAG, "Image file not found", e);
        }
    }
}