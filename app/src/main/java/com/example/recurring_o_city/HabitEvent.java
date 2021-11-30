package com.example.recurring_o_city;

import android.graphics.Bitmap;
import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * this class is for triggering an event at a certain time and containing related information
 */
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
     * get the habit occurring
     * @return Habit
     */
    public Habit getEventHabit() {
        return eventHabit;
    }

    /**
     * set the habit occurring
     * @param eventHabit
     */
    public void setEventHabit(Habit eventHabit) {
        this.eventHabit = eventHabit;
    }

    /**
     * get any comments about the event
     * @return String
     */
    public String getEventComment() {
        return eventComment;
    }

    /**
     * set comments about the event
     * @param eventName
     */
    public void setEventComment(String eventName) {
        this.eventComment = eventName;
    }

    /**
     * get any event pics
     * @return eventPic
     */
    public String getEventPic() {
        return eventPic;
    }

    /**
     * set event pics
     * @param eventPic
     */
    public void setEventPic(String eventPic) {
        this.eventPic = eventPic;
    }

    /**
     * get location of event
     * @return eventLoc
     */
    public GeoPoint getEventLoc() {
        return eventLoc;
    }

    /**
     * set even location
     * @param eventLoc
     */
    public void setEventLoc(GeoPoint eventLoc) {
        this.eventLoc = eventLoc;
    }

    /**
     * get creation date of Habit
     * @return dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * set creation date of Habit
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
