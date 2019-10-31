package com.nathan.fooddiary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private FoodRepository repository;

    private LiveData<List<Food>> allRecords;

    private LiveData<Integer> rowCount;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
        allRecords = repository.getAllRecords();
        rowCount = repository.getRowCount();
    }

    public LiveData<List<Food>> getAllRecords() {
        return allRecords;
    }

    public LiveData<Food> getRecordForTitle(String title) {
        return repository.getRecordForTitle(title);
    }

    public LiveData<Integer> getRowCount(){
        return rowCount;
    }

    public void insert(Food record) {
        repository.insert(record);
    }

    public void delete(Food record) {
        repository.delete(record);
    }

    public void update(Food record) {
        repository.update(record);
    }
}
