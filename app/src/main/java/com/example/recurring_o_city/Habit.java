package com.example.recurring_o_city;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Habit implements Serializable
{
    String title;
    String reason;
    Date date;
    Date next_date = null; // get the next date
    Date nextnext_date = null; // get the next date of next date
    List<String> repeat;
    Integer privacy;  //private = 1, public = 0
    String done = "false";  //task finished = 1, else 0.
    Integer goal = 1;
    Integer complete = 0;

    public Habit (String title, String reason, Date date, List<String> repeat, Integer status)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.privacy = status;
        this.repeat = repeat;
    }

    public Date getNextnext_date() {
        return nextnext_date;
    }

    public void setNextnext_date(Date nextnext_date) {
        this.nextnext_date = nextnext_date;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public List<String> getRepeat() {
        return repeat;
    }

    public void setRepeat(List<String> repeat) {
        this.repeat = repeat;
    }
    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public Date getNext_date() {
        return next_date;
    }

    public void setNext_date(Date next_date) {
        this.next_date = next_date;
    }

}
