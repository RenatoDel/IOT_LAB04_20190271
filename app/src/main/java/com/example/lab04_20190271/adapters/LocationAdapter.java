package com.example.lab04_20190271.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04_20190271.R;
import com.example.lab04_20190271.models.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> locationList;
    private LocationClickListener clickListener;

    public interface LocationClickListener {
        void onLocationClick(Location location);
    }

    public LocationAdapter(List<Location> locationList, LocationClickListener clickListener) {
        this.locationList = locationList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationName, tvLocationId, tvRegion, tvCoordinates;

        LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvLocationId = itemView.findViewById(R.id.tvLocationId);
            tvRegion = itemView.findViewById(R.id.tvRegion);
            tvCoordinates = itemView.findViewById(R.id.tvCoordinates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onLocationClick(locationList.get(position));
                    }
                }
            });
        }

        void bind(Location location) {
            tvLocationName.setText(location.getName());
            tvLocationId.setText("ID: " + location.getId());
            tvRegion.setText(location.getRegion() + ", " + location.getCountry());
            tvCoordinates.setText("Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
        }
    }
}
