package com.nathan.fooddiary;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

    private static volatile FoodDatabase INSTANCE;

    public abstract FoodDAO foodDAO();

    static FoodDatabase getDatabase(final Context context) {

        if(INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FoodDatabase.class, "Food").build();
                }
            }
        }
        return INSTANCE;
    }


}
