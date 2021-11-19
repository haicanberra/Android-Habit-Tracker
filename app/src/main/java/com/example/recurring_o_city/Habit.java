package com.example.recurring_o_city;

import java.util.Date;
import java.util.List;

public class Habit
{
    String title;
    String reason;
    Date date;
    List<String> repeat;
    int status; //private = 1, public = 0

    public Habit (String title, String reason, Date date, List<String> repeat, int status)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.status = status;
        this.repeat = repeat;
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
