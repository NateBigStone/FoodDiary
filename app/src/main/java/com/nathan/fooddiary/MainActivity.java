package com.nathan.fooddiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodClickListener {

    private static String TAG = "MAIN_ACTIVITY";
    public static final String EXTRA_FOOD = "com.nathan.fooddiary.food";


    private static final int EDIT_REQUEST_CODE = 0;

    private RecyclerView mFoodRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mSearchButton;
    private EditText mSearchEditText;

    private List<Food> mFoodList;
    private FoodViewModel mFoodDatabase;
    private int mLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodDatabase = new FoodViewModel(getApplication());
        Food example0 = new Food("Mayo Salad", "Mayo and Lettuce", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example0);
        Food example1 = new Food("Mayo Burrito", "Mayo and Burrito", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example1);
        Food example2 = new Food("Mayo Burger", "Mayo and Bun", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example2);
        Food example3 = new Food("Mayo Taco", "Mayo and TacoShell", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example3);
        Food example4 = new Food("Mayo Sandwich", "Mayo and Bread", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example4);
        Log.d(TAG, "The database is: " + mFoodDatabase);

        mFoodDatabase.getAllRecords().observe(this, new Observer<List<Food>>() {

            @Override
            public void onChanged(List<Food> food) {
                mFoodList = food;
                mLength = food.size();
                Log.d(TAG, "Food records are: " + food);
                mAdapter = new FoodAdapter(mFoodList, mLength, MainActivity.this);
                mFoodRecyclerView.setAdapter(mAdapter);

            }
        });

        Log.d(TAG, "Food records are: " + mFoodList);

        mFoodRecyclerView = findViewById(R.id.food_list);
        mSearchButton = findViewById(R.id.search_button);
        mSearchEditText = findViewById(R.id.search);

        mFoodRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mFoodRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FoodAdapter(mFoodList, mLength,this);
        mFoodRecyclerView.setAdapter(mAdapter);

        mSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newSearch = mSearchEditText.getText().toString();
                if (newSearch.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a title or a tag", Toast.LENGTH_LONG).show();
                    return;
                }
                //TODO: Logic for the search
                mSearchEditText.getText().clear();
            }
        });
    }

    @Override
    public void onListClick(int position) {
        System.out.println(position);
        Food food = mFoodList.get(position);
        System.out.println(food);
        //TODO: create an intent for the add/edit activity
        Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
        editIntent.putExtra(EXTRA_FOOD, food);
        Log.d(TAG, "The extra is supposed to be: " + food);
        startActivityForResult(editIntent, EDIT_REQUEST_CODE);
    }
    @Override
    public void onListLongClick(int position) {
        final int itemPosition = position;
        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_food_message, mFoodList.get(position).getTitle() ))
                .setTitle(getString(R.string.delete_dialog_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove from database
                        mFoodDatabase.delete(mFoodList.get(itemPosition));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        confirmDeleteDialog.show();
    }
}

