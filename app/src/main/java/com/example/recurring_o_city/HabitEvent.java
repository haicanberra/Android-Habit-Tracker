package com.example.recurring_o_city;

import android.graphics.Bitmap;
import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is for triggering an event at a certain time and containing related information/
 */
public class HabitEvent implements Serializable {
    private Habit eventHabit;
    private String eventComment;
    private String eventPic;
    private GeoPoint eventLoc;
    private Date dateCreated;

    /**
     * Constructor for the {@link HabitEvent} object.
     * @param eventHabit
     *  {@link Habit} type object in which current {@link HabitEvent} spawned from.
     * @param dateCreated
     *  {@link Date} value holding the date when the {@link HabitEvent} was created.
     * @param eventComment
     *  Optional {@link String} comment of 20-char limit.
     * @param eventPic
     *  {@link String}-converted {@link Bitmap} photo information.
     * @param eventLoc
     *  {@link GeoPoint} coordinate of the {@link HabitEvent} occurrence.
     */
    public HabitEvent(Habit eventHabit, Date dateCreated, String eventComment, String eventPic, GeoPoint eventLoc) {
        this.eventHabit = eventHabit;
        this.eventComment = eventComment;
        this.eventPic = eventPic;
        this.eventLoc = eventLoc;
        this.dateCreated = dateCreated;
    }

    /**
     * Getter for the {@link Habit} object this {@link HabitEvent} is called from.
     * @return eventHabit
     *  {@link Habit} object that spawned this {@link HabitEvent}.
     */
    public Habit getEventHabit() {
        return eventHabit;
    }

    /**
     * Setter for the {@link Habit} object this {@link HabitEvent} is called from.
     * @param eventHabit
     *  New {@link Habit} to set this {@link HabitEvent} with.
     */
    public void setEventHabit(Habit eventHabit) {
        this.eventHabit = eventHabit;
    }

    /**
     * Getter for {@link String} comments for this {@link HabitEvent}.
     * @return eventComment
     *  {@link String} value of optional comments.
     */
    public String getEventComment() {
        return eventComment;
    }

    /**
     * Setter for {@link String} comments for this {@link HabitEvent}.
     * @param eventName
     *  New {@link String} comment value for {@link HabitEvent}.
     */
    public void setEventComment(String eventName) {
        this.eventComment = eventName;
    }

    /**
     * Getter for {@link Bitmap} image, converted to {@link String}.
     * @return eventPic
     *  {@link String} value of the {@link Bitmap} from the {@link HabitEvent}.
     */
    public String getEventPic() {
        return eventPic;
    }

    /**
     * Setter for {@link Bitmap} image, converted to {@link String}.
     * @param eventPic
     *  {@link String} value of the {@link Bitmap} image.
     */
    public void setEventPic(String eventPic) {
        this.eventPic = eventPic;
    }

    /**
     * Getter for {@link GeoPoint} coordinate of the {@link HabitEvent}.
     * @return eventLoc
     *  {@link GeoPoint} coordinate where {@link HabitEvent} occurred.
     */
    public GeoPoint getEventLoc() {
        return eventLoc;
    }

    /**
     * Setter for {@link GeoPoint} coordinate of the {@link HabitEvent}.
     * @param eventLoc
     *  {@link GeoPoint} coordinate for {@link HabitEvent}.
     */
    public void setEventLoc(GeoPoint eventLoc) {
        this.eventLoc = eventLoc;
    }

    /**
     * Getter for {@link Date} in which {@link HabitEvent} was created.
     * @return dateCreated
     *  {@link Date} value of the {@link HabitEvent} creation date.
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Setter for {@link Date} in which {@link HabitEvent} was created.
     * @param dateCreated
     *  New {@link Date} value to set the {@link HabitEvent} creation date with.
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
