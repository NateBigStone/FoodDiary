package com.nathan.fooddiary;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {

    private FoodDAO foodDAO;

    public FoodRepository(Application application) {
        FoodDatabase db = FoodDatabase.getDatabase(application);
        foodDAO = db.foodDAO();
    }

    public void insert(Food record) {
        new InsertPlaceAsync(foodDAO).execute(record);
    }

    static class InsertPlaceAsync extends AsyncTask<Food, Void, Void> {

        private FoodDAO foodDAO;

        InsertPlaceAsync(FoodDAO foodDAO) {
            this.foodDAO = foodDAO;
        }

        @Override
        protected Void doInBackground(Food... foodList) {
            foodDAO.insert(foodList);
            return null;
        }
    }

    public void delete(Food record){
        new DeleteFoodAsync(foodDAO).execute(record);
    }

    static class DeleteFoodAsync extends AsyncTask<Food, Void, Void> {

        private FoodDAO foodDAO;

        DeleteFoodAsync(FoodDAO foodDAO) {
            this.foodDAO = foodDAO;
        }

        @Override
        protected Void doInBackground(Food... foodList) {
            foodDAO.delete(foodList);
            return null;
        }

    }

    public void update(Food record) {

        new UpdateFoodAsync(foodDAO).execute(record);

    }

    static class UpdateFoodAsync extends AsyncTask<Food, Void, Void> {

        private FoodDAO foodDAO;

        UpdateFoodAsync(FoodDAO travelDAO) {
            this.foodDAO = foodDAO;
        }

        @Override
        protected Void doInBackground(Food... foodList) {
            foodDAO.update(foodList);
            return null;
        }

    }

    public LiveData<List<Food>> getAllRecords() {
        return foodDAO.getAllRecords();
    }

    public LiveData<Food> getRecordForTitle(String title) {
        return foodDAO.getRecordForTitle(title);
    }
    public LiveData<Integer> getRowCount() {
        return foodDAO.getRowCount();
    }
}
