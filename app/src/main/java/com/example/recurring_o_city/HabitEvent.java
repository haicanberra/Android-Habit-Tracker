package com.example.recurring_o_city;

import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;

public class HabitEvent {
    // private Habit eventHabit;
    private String eventName;
    private Picture eventPic;
    private GoogleMap eventLoc;

    public HabitEvent(/* Habit eventHabit, */ String eventName) {
        // this.eventHabit = eventHabit;
        this.eventName = eventName;
//        this.eventPic = eventPic;
//        this.eventLoc = eventLoc;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Picture getEventPic() {
        return eventPic;
    }

    public void setEventPic(Picture eventPic) {
        this.eventPic = eventPic;
    }

    public GoogleMap getEventLoc() {
        return eventLoc;
    }

    public void setEventLoc(GoogleMap eventLoc) {
        this.eventLoc = eventLoc;
    }
}
