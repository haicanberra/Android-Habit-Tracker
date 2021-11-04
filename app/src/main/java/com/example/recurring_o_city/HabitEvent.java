package com.example.recurring_o_city;

import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;

public class HabitEvent {
    // private Habit eventHabit;
    private String eventName;
    private Picture eventPic;
    private GoogleMap eventLoc;

    public HabitEvent(/* Habit eventHabit, */ String eventName, Picture eventPic, GoogleMap eventLoc) {
        // this.eventHabit = eventHabit;
        this.eventName = eventName;
        this.eventPic = eventPic;
        this.eventLoc = eventLoc;
    }
}
