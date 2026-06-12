package com.example.transportbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class places_Adapter extends RecyclerView.Adapter<places_Adapter.PlaceViewHolder> {
     Context context;
     List<places_elements> placeList;

    public places_Adapter(Context context, List<places_elements> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rcview_places, parent, false);
        return new PlaceViewHolder(view);
    }

    public void updateList(List<places_elements> newList)
    {
        this.placeList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        places_elements place = placeList.get(position);
        holder.name.setText(place.getPlace_name());
        Glide.with(context).load(place.getPlace_image()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, activity_Places_Info.class);
            intent.putExtra("name", place.getPlace_name());
            intent.putExtra("imageUrl", place.getPlace_image());
            intent.putExtra("description", place.getPlace_desc());
            intent.putExtra("location", place.getPlace_location());
            intent.putExtra("latitude", place.getPlace_latitude());
            intent.putExtra("longitude", place.getPlace_longitude());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.place_image);
            name = itemView.findViewById(R.id.place_name);
        }
    }
}