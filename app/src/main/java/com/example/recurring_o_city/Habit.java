package com.example.recurring_o_city;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This class is a data class to organize all relevant information of a habit.
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
     * Constructor for the object of type {@Link Habit}.
     * @param title
     *  {@link String} type title of the {@link Habit}.
     * @param reason
     *  {@link String} type reason for creating the {@link Habit}.
     * @param date
     *  {@link Date} type object, containing when the {@link Habit} starts.
     * @param repeat
     *  {@link List} of {@link String} holding the information about repeat frequency of {@link Habit}.
     * @param status
     *  {@link Integer} value that denotes whether a given {@link Habit} is private or public.
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
     * Getter for the {@link Integer} type variable goal.
     * @return goal
     *  {@link Integer} value tracking maximum limit of {@link Habit} repeat frequency.
     */
    public Integer getGoal() {
        return goal;
    }

    /**
     * Setter for the {@link Integer} type variable goal.
     * @param goal
     *  {@link Integer} value of new goal needed for this {@link Habit}'s completion.
     */
    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    /**
     * Getter for the {@link Integer} type variable complete.
     * @return complete
     *  {@link Integer} value counter showing how many times a habit was completed.
     */
    public Integer getComplete() {
        return complete;
    }

    /**
     * Setter for the {@link Integer} type variable complete.
     * @param complete
     *  Sets the new complete limit from {@link Integer} value received.
     */
    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    /**
     * Getter for the {@link Habit} title, in type {@link String}.
     * @return title
     *  {@link String} type variable denoting the {@link Habit}'s name.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the {@link Habit} title, in type {@link String}.
     * @param title
     *  {@link String} type variable for new {@link Habit} title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the {@link Habit}'s reason, in type {@link String}.
     * @return reason;
     *  {@link String} type variable holding the {@link Habit}'s creation reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Setter for the {@link Habit}'s reason, in type {@link String}.
     * @param reason
     *  {@link String} of new repeat to set in {@link Habit}.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Getter for the {@link Habit} start date.
     * @return date
     *  {@link Date} of the {@link Habit} start date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the {@link Habit} start date.
     * @param date
     *  {@link Date} of the {@link Habit}'s new start date.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the {@link Habit}'s privacy setting.
     * @return privacy
     *  {@link Integer} type value which can be 0 (public) or 1 (private).
     */
    public Integer getPrivacy() {
        return privacy;
    }

    /**
     * Setter for the {@link Habit}'s privacy setting.
     * @param privacy
     *  {@link Integer} type value denoting {@link Habit}'s privacy value.
     */
    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    /**
     * Getter for the {@link Habit}'s repeat frequency.
     * @return repeat
     *  {@link List<String>} of the {@link Habit}'s repeat frequency.
     */
    public List<String> getRepeat() {
        return repeat;
    }

    /**
     * Setter for the {@link Habit}'s repeat frequency.
     * @param repeat
     *  New {@link List<String>} of the {@link Habit} holding all the habit frequency information.
     */
    public void setRepeat(List<String> repeat) {
        this.repeat = repeat;
    }

    /**
     * Getter for {@link String} value denoting whether {@link Habit} is done.
     * @return done
     *  {@link String} type value, which can be either "Done" (finished) or "false" (in progress).
     */
    public String getDone() {
        return done;
    }

    /**
     * Setter for {@link String} value, indicate whether {@link Habit} is done.
     * @param done
     *  {@link String} type value denoting whether {@link Habit} is done for the day.
     */
    public void setDone(String done) {
        this.done = done;
    }

    /**
     * Getter for the next day in which {@link Habit} will recur.
     * @return next_date
     *  {@link Date} type variable showing which day the {@link Habit} will recur.
     */
    public Date getNext_date() {
        return next_date;
    }

    /**
     * Setter for the next day for {@link Habit} to recur.
     * @param next_date
     *  Sets the {@link Date} type variable, showing when the {@link Habit} will show up again.
     */
    public void setNext_date(Date next_date) {
        this.next_date = next_date;
    }

}
