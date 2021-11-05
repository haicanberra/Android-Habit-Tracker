package com.example.recurring_o_city;

import java.util.Date;

/**
 * represents a singular habit
 */
public class Habit
{
    String title;
    String reason;
    Date date;
    int privacy; //private = 1, public = 0

    /**
     * @param title
     * @param reason
     * @param date
     * @param privacy
     */
    public Habit (String title, String reason, Date date, int privacy)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.privacy = privacy;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return int
     */
    public int getPrivacy() {
        return privacy;
    }

    /**
     * @param privacy
     */
    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}
