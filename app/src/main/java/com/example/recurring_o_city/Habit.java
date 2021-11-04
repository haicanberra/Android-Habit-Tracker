package com.example.recurring_o_city;

import java.util.Date;

public class Habit
{
    String title;
    String reason;
    Date date;
    int priv; //private = 1, public = 0

    public Habit (String title, String reason, Date date, int priv)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.priv = priv;
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

    public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }
}
