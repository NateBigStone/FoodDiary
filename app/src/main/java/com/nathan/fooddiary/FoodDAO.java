package com.nathan.fooddiary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Food... wr);

    @Update
    void update(Food... wr);

    @Delete
    void delete(Food... wr);

    @Query("SELECT * FROM Food WHERE title = :title LIMIT 1")
    LiveData<Food> getRecordForTitle(String title);

    @Query("SELECT * FROM Food WHERE dateCreated = :date LIMIT 1")
    LiveData<Food> getRecordForDate(String date);

    @Query("SELECT * FROM Food ORDER BY dateCreated")
    LiveData<List<Food>> getAllRecords();

    @Query("SELECT COUNT(title) FROM Food")
    LiveData<Integer> getRowCount();

}
