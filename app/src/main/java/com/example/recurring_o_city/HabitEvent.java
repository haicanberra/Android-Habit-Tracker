package com.example.recurring_o_city;

import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;

import java.util.Date;

public class HabitEvent {
    private Habit eventHabit;
    private String eventComment;
    private Picture eventPic;
    private GoogleMap eventLoc;
    private Date dateCreated;

    public HabitEvent(Habit eventHabit, Date dateCreated, String eventComment, Picture eventPic, GoogleMap eventLoc) {
        this.eventHabit = eventHabit;
        this.eventComment = eventComment;
        this.eventPic = eventPic;
        this.eventLoc = eventLoc;
        this.dateCreated = dateCreated;
    }

    public Habit getEventHabit() {
        return eventHabit;
    }

    public void setEventHabit(Habit eventHabit) {
        this.eventHabit = eventHabit;
    }

    public String getEventComment() {
        return eventComment;
    }

    public void setEventComment(String eventName) {
        this.eventComment = eventName;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
