package com.example.emon.tourmate.Other_class;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emon.tourmate.EventDetailsActivity;
import com.example.emon.tourmate.R;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.CustomViewHolder> {
    Context context;
    List<EventClass> eventList;

    public EventListAdapter(Context context, List<EventClass> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.event_item,viewGroup,false);
        CustomViewHolder customViewHolder=new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int i) {

        customViewHolder.TourNameTv.setText(eventList.get(i).getTourName());
        customViewHolder.StartDateTv.setText(eventList.get(i).getStartDate());
        customViewHolder.endDate.setText(eventList.get(i).getEndDate());


        customViewHolder.mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EventDetailsActivity.class);
                intent.putExtra("id",eventList.get(i).getId());
                intent.putExtra("tourname",eventList.get(i).getTourName());
                intent.putExtra("startDate",eventList.get(i).getStartDate());
                intent.putExtra("endDate",eventList.get(i).getEndDate());
                intent.putExtra("startingLocation",eventList.get(i).getStartingLocation());
                intent.putExtra("destination",eventList.get(i).getDestination());
                int budget=eventList.get(i).getBudget();
                intent.putExtra("budget",budget);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
         TextView TourNameTv, StartDateTv, endDate;
         CardView mycard;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            TourNameTv=itemView.findViewById(R.id.tournameTVID);
            StartDateTv=itemView.findViewById(R.id.startdateTvid);
            endDate=itemView.findViewById(R.id.endDatetvId);
            mycard=itemView.findViewById(R.id.myCardid);
        }
    }
}
