package com.example.recurring_o_city;

import android.graphics.Bitmap;
import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class HabitEvent implements Serializable {
    private Habit eventHabit;
    private String eventComment;
    private String eventPic;
    private GeoPoint eventLoc;
    private Date dateCreated;

    public HabitEvent(Habit eventHabit, Date dateCreated, String eventComment, String eventPic, GeoPoint eventLoc) {
        this.eventHabit = eventHabit;
        this.eventComment = eventComment;
        this.eventPic = eventPic;
        this.eventLoc = eventLoc;
        this.dateCreated = dateCreated;
    }

    /**
     * @return Habit
     */
    public Habit getEventHabit() {
        return eventHabit;
    }

    /**
     * @param eventHabit
     */
    public void setEventHabit(Habit eventHabit) {
        this.eventHabit = eventHabit;
    }

    /**
     * @return String
     */
    public String getEventComment() {
        return eventComment;
    }

    /**
     * @param eventName
     */
    public void setEventComment(String eventName) {
        this.eventComment = eventName;
    }

    public String getEventPic() {
        return eventPic;
    }

    public void setEventPic(String eventPic) {
        this.eventPic = eventPic;
    }

    public GeoPoint getEventLoc() {
        return eventLoc;
    }

    public void setEventLoc(GeoPoint eventLoc) {
        this.eventLoc = eventLoc;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
