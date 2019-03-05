package com.example.emon.tourmate.Other_class;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.emon.tourmate.Nearby.Result;
import com.example.emon.tourmate.R;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    List<Result> results;

    public NearbyAdapter(List<Result> results) {
        this.results= results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_place_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTV.setText(results.get(position).getName());
        holder.rateTV.setText(String.valueOf(results.get(position).getRating()));
        holder.addressTV.setText(results.get(position).getVicinity());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTV,rateTV,addressTV;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTVID);
            rateTV = itemView.findViewById(R.id.RateTVID);
            addressTV = itemView.findViewById(R.id.AddressTVID);
        }
    }

    public void NewList(List<Result>results){
        this.results = results;
        notifyDataSetChanged();
    }
}
