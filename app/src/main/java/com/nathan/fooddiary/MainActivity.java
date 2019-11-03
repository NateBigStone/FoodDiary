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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity implements FoodClickListener {

    private static String TAG = "MAIN_ACTIVITY";
    public static final String EXTRA_FOOD = "com.nathan.fooddiary.food";


    private static final int EDIT_REQUEST_CODE = 0;

    private RecyclerView mFoodRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mNewButton;
    private EditText mSearchEditText;

    private List<Food> mFoodList;
    private List<Food> mFoodReducedList;
    private FoodViewModel mFoodDatabase;
    private int mLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodDatabase = new FoodViewModel(getApplication());
        Food example0 = new Food("Light Mayo Salad", "Mayo and Lettuce", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Food example1 = new Food("Light Burrito", "Mayo and Burrito", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Food example2 = new Food("Light Burger", "Mayo and Bun", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example2);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Food example3 = new Food("Light Taco", "Mayo and TacoShell", ".","mayo lettuce tasty");
        mFoodDatabase.insert(example3);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Food example4 = new Food("Light Sandwich", "Mayo and Bread", ".","mayo lettuce tasty");
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
        mNewButton = findViewById(R.id.new_button);
        mSearchEditText = findViewById(R.id.search);

        mFoodRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mFoodRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FoodAdapter(mFoodList, mLength,this);
        mFoodRecyclerView.setAdapter(mAdapter);

        mNewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: Create New
                System.out.println("create new");
                Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(editIntent, EDIT_REQUEST_CODE);
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newSearch = mSearchEditText.getText().toString();
                if (newSearch.isEmpty()) {
                    mAdapter = new FoodAdapter(mFoodList, mLength, MainActivity.this);
                    mFoodRecyclerView.setAdapter(mAdapter);
                    return;
                }
                mFoodReducedList = new ArrayList<>();
                Log.d(TAG, "IN SEARCH");

                String search =  mSearchEditText.getText().toString().toLowerCase();
                for( int i = 0; i < mLength; i++){
                    Log.d(TAG, "LOOPING " + mFoodList.get(i).getTitle() + " SEARCH: " + search);
                    if (mFoodList.get(i).getTitle().toLowerCase().matches("(.*)" + search + "(.*)")
                            || mFoodList.get(i).getTags().toLowerCase().matches("(.*)" + search + "(.*)")){
                        Log.d(TAG, "THERE'S A MATCH: " + mFoodList.get(i).getTitle());
                        mFoodReducedList.add(mFoodList.get(i));
                    }
                }
                Log.d(TAG, "old LIst: " + mFoodList);
                Log.d(TAG, "New LIst: " + mFoodReducedList);
                int mNewLength = mFoodReducedList.size();
                mAdapter = new FoodAdapter(mFoodReducedList, mNewLength, MainActivity.this);
                mFoodRecyclerView.setAdapter(mAdapter);
            }
        });

    }

    @Override
    public void onListClick(int position) {
        System.out.println(position);
        Food food = mFoodList.get(position);
        System.out.println(food);
        Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
        editIntent.putExtra(EXTRA_FOOD, food.getDateCreated());
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

