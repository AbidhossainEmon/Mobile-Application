package com.example.emon.tourmate.Other_class;

public class EventClass {


    private String id;
    private String tourName;

    public EventClass(String id, String tourName, String startingLocation, String destination, String startDate, String endDate, int budget) {
        this.id = id;
        this.tourName = tourName;
        this.startingLocation = startingLocation;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    private String startingLocation;
    private String destination;
    private String startDate;
    private String endDate;
    private int budget;


    public EventClass() {
    }


    public String getId() { return id; }
    public String getTourName() {
        return tourName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getBudget() {
        return budget;
    }
}
