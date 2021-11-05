package com.example.recurring_o_city;

import java.util.Date;

public class Habit
{
    String title;
    String reason;
    Date date;
    int privacy; //private = 1, public = 0

    public Habit (String title, String reason, Date date, int privacy)
    {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.privacy = privacy;
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

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}
