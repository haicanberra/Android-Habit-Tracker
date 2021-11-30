package com.example.recurring_o_city;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * this class is a data class to organize all relevant information of a habit
 */
public class Habit implements Serializable
{
    String title;
    String reason;
    Date date;
    Date next_date = null; // get the next date
    List<String> repeat;
    Integer privacy;  //private = 1, public = 0
    String done = "false";  //task finished = 1, else 0.
    Integer goal = 1;
    Integer complete = 0;

    /**
     * @param title
     * @param reason
     * @param date
     * @param repeat
     * @param status
     */

    public Habit (String title, String reason, Date date, List<String> repeat, Integer status)

    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.privacy = status;
        this.repeat = repeat;
    }

    /**
     * @return goal
     * an int
     */
    public Integer getGoal() {
        return goal;
    }

    /**
     * @param goal, set goal
     */
    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    /**
     * @return complete
     * check if habit is complete
     */
    public Integer getComplete() {
        return complete;
    }

    /**
     * @param complete
     * toggle the complete state
     */
    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    /**
     * get title or name
     * @return title
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
     * @return privacy
     * get privacy setting
     */
    public Integer getPrivacy() {
        return privacy;
    }

    /**
     * set privacy setting
     * @param privacy
     */
    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    /**
     * get the repeat settings
     * @return repeat
     */
    public List<String> getRepeat() {
        return repeat;
    }

    /**
     * set the repeat settings
     * @param repeat
     */
    public void setRepeat(List<String> repeat) {
        this.repeat = repeat;
    }

    /**
     * is the habit complete
     * @return done
     */
    public String getDone() {
        return done;
    }

    /**
     * set if the habit is done
     * @param done
     */
    public void setDone(String done) {
        this.done = done;
    }

    /**
     * get the date for the next reminder
     * @return next_date
     */
    public Date getNext_date() {
        return next_date;
    }

    /**
     * set the next date
     * @param next_date
     */
    public void setNext_date(Date next_date) {
        this.next_date = next_date;
    }

}
