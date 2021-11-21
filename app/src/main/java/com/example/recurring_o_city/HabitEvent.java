package com.example.recurring_o_city;

import android.graphics.Picture;

import com.google.android.gms.maps.GoogleMap;

/**
 * represents the event for whatever habit is occurring
 */
public class HabitEvent {
    private Habit eventHabit;
    private String eventComment;
    private Picture eventPic;
    private GoogleMap eventLoc;

    /**
     * @param eventHabit
     * @param eventComment
     */
    public HabitEvent(Habit eventHabit, String eventComment) {
        this.eventHabit = eventHabit;
        this.eventComment = eventComment;
        this.eventPic = eventPic;
        this.eventLoc = eventLoc;
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
