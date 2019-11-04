package com.nathan.fooddiary;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private String TAG = "Food_Adapter";
    private List<Food> data;
    private int length;

    private FoodClickListener listener;

    public FoodAdapter(List<Food> data, int length, FoodClickListener listener) {
        this.listener = listener;
        this.data = data;
        this.length = length;
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout layout;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView dateCreatedTextView;
        ImageView foodPhotoView;
        FoodClickListener listener;

        FoodViewHolder(LinearLayout layout, FoodClickListener listener) {
            super(layout);
            this.listener = listener;
            this.layout = layout;
            titleTextView = layout.findViewById(R.id.food_title);
            dateCreatedTextView = layout.findViewById(R.id.date_created);
            descriptionTextView = layout.findViewById(R.id.description);
            foodPhotoView = layout.findViewById(R.id.food_photo);

            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view){

            listener.onListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            listener.onListLongClick(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.food_element, parent, false);

        //Create a new viewHolder, to contain this TextView
        FoodViewHolder viewHolder = new FoodViewHolder(layout, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {

        Food food = data.get(position);
        holder.titleTextView.setText(food.getTitle());
        holder.dateCreatedTextView.setText("Date Created: " + new Date(Long.parseLong(food.getDateCreated())));
        holder.descriptionTextView.setText(food.getDescription());
        holder.foodPhotoView.setImageResource(R.drawable.icons8_food_80);
        String foodPhotoPath = food.getImagePath();
        if (foodPhotoPath != null && foodPhotoPath.length() > 5) {
            Picasso.get()
                    .load(new File(foodPhotoPath))
                    .error(android.R.drawable.stat_notify_error) // built-in error icon
                    .fit()
                    .centerCrop()
                    .into(holder.foodPhotoView, new Callback() {
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
        else {
            holder.foodPhotoView.setImageResource(R.drawable.icons8_food_80);
        }
    }

    @Override
    public int getItemCount() {
        return this.length;
    }

}