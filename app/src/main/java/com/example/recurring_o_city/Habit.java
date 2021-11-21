package com.example.recurring_o_city;

import java.util.Date;
import java.util.List;

/**
 * represents a singular habit
 */
public class Habit
{
    String title;
    String reason;
    Date date;
    List<String> repeat;
    int status; //private = 1, public = 0

    /**
     * @param title
     * @param reason
     * @param date
     * @param repeat
     * @param status
     */

    public Habit (String title, String reason, Date date, List<String> repeat, int status)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.status = status;
        this.repeat = repeat;
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


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getRepeat() {
        return repeat;
    }

    public void setRepeat(List<String> repeat) {
        this.repeat = repeat;
    }
}
