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

    //icons from icons8.com

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
    private int mNewLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodDatabase = new FoodViewModel(getApplication());
//        Food example0 = new Food("Kale and black bean quesadilla",
//                "The Black Bean Quesarito is a flour tortilla filled with black beans, " +
//                        "seasoned rice, cheddar cheese, nacho cheese sauce, reduced-fat sour cream " +
//                        "and a creamy chipotle sauce. The dishes can be customized to be vegan by " +
//                        "ditching the nacho cheese sauce, sour cream, cheddar cheese and creamy " +
//                        "chipotle sauce.", "","bean quesadilla cheese nacho creamy");
//        mFoodDatabase.insert(example0);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Food example1 = new Food("Turkey Burrito", "A big ol’ soft flour tortilla " +
//                "with some spiced ground turkey and a few veggies –> who knew this combo could be so " +
//                "humble meets revolutionary? Not me, until I started making these a few months ago. " +
//                "That’s right – it’s been months and I haven’t shared it here because, um, I don’t " +
//                "know. It just seemed too Basic Girl of me. That being said, you and I know the truth: " +
//                "that sometimes a grab-and-go lunch from the freezer, even if it is almost as simple " +
//                "as spicy ground turkey and veggies rolled up in a flour tortilla, is a Meal Prep Win. " +
//                "Plus, I shared them on Snapchat a few weeks ago and have gotten several emails and " +
//                "snaps since asking me to share the recipe on the blog. I mean, I’m blushing. Thanks " +
//                "for making me feel like I’m a special turkey burrito maker, guys",
//                "","creamy burrito sour cream");
//        mFoodDatabase.insert(example1);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Food example2 = new Food("Light Salmon Burger", "These burgers can be formed " +
//                "earlier in the day, or even the night before. Refrigerating them before cooking helps " +
//                "them hold together firm during cooking. Another tip is to put some of the salmon in " +
//                "the food processor or chopper. It creates \"glue\" that keeps everything together.",
//                "","fish tomato light");
//        mFoodDatabase.insert(example2);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Food example3 = new Food("Zesty Taco", "Line the taco shells with lettuce " +
//                "and fill with beef mixture. Top with salsa, cheese, and sour cream, if desired.",
//                "","taco cheese lettuce messy");
//        mFoodDatabase.insert(example3);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Food example4 = new Food("Three bean chili with hominy and kale", "If you " +
//                "happen to be making this at a non-Thanksgiving time of year (i.e. you don’t have " +
//                "gobs of roast turkey sitting around) you can substitute ground turkey. Just add in " +
//                "about 1 pound of raw ground turkey to the recipe after the onion, garlic, and spices. " +
//                "Saute until it turns white (about 3-4 minutes) then proceed with the recipe as written.",
//                "","gobs saute garlic recipe");
//        mFoodDatabase.insert(example4);
        Log.d(TAG, "The database is: " + mFoodDatabase);

        mFoodDatabase.getAllRecords().observe(this, new Observer<List<Food>>() {

            @Override
            public void onChanged(List<Food> food) {
                mFoodList = food;
                mLength = food.size();
                mNewLength = food.size();
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
                    mNewLength = mLength;
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
                mNewLength = mFoodReducedList.size();
                mAdapter = new FoodAdapter(mFoodReducedList, mNewLength, MainActivity.this);
                mFoodRecyclerView.setAdapter(mAdapter);
                //TODO: have the keyboard disappear
            }
        });

    }

    @Override
    public void onListClick(int position) {
        System.out.println(position);
        Food food;
        if(mLength != mNewLength){
            food = mFoodReducedList.get(position);
        }
        else {
            food = mFoodList.get(position);
        }
        System.out.println(food);
        Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
        editIntent.putExtra(EXTRA_FOOD, food.getDateCreated());
        startActivityForResult(editIntent, EDIT_REQUEST_CODE);
    }
    @Override
    public void onListLongClick(int position) {
        final Food deleteFood;
        if(mLength != mNewLength){
            deleteFood = mFoodReducedList.get(position);
        }
        else {
            deleteFood = mFoodList.get(position);
        }
        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_food_message, deleteFood.getTitle() ))
                .setTitle(getString(R.string.delete_dialog_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove from database
                        mFoodDatabase.delete(deleteFood);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        confirmDeleteDialog.show();
        mSearchEditText.setText("");
    }
}

