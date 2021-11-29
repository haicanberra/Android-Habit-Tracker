package com.example.recurring_o_city;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utility {
    public Utility() { }

    // Method to convert list of string to string
    public String convertRepeat(List<String> repeat) {
        String repeat_display = "Every ";

        //Display repeat on screen
        for (int i = 0; i < repeat.size(); i++) {
            if (i == 0) {
                repeat_display += repeat.get(i);
                repeat_display += " ";
            }
            if (i == 1) {
                repeat_display += repeat.get(i);
                if (repeat.get(i).equals("week") && i < repeat.size() - 2) {
                    repeat_display += " on ";
                } else if (!repeat.get(i).equals("week") &&
                        !repeat.get(repeat.size() - 1).equals("never")) {
                    repeat_display += ", ";
                }
            }
            if (repeat.get(1).equals("week") && i < repeat.size() - 1 && i > 1) {
                repeat_display += repeat.get(i);
                if (!repeat.get(i + 1).equals("never")) {
                    repeat_display += ", ";
                }
            }
            if (i == repeat.size() - 1 && !repeat.get(i).equals("never")) {
                repeat_display += repeat.get(i);
                repeat_display += " times";
            }
        }
        return repeat_display;
    }

//    public Date convertDate() {
//        Date newDate = null;
//        SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");
//        try {
//            newDate = d.parse(String.valueOf(habitDate.getText()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }
    // Get current date
    public Date getCurrentDate() {
        SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");
        Date today = Calendar.getInstance().getTime();
        String date_s = d.format(today);
        try {
            today = d.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today;
    }

    public Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public Date getNextDate(Habit newHabit) {
        Date dateGoals = null;

        //Date currentDate = util.getCurrentDate();
        // Update the next date
        if (newHabit.getRepeat() != null) {
            String repeatType = newHabit.getRepeat().get(1);
            Integer repeatNum = Integer.valueOf(newHabit.getRepeat().get(0));

            // Find next date based on next date
            Date currentDate = newHabit.getNext_date();
            if (repeatType.equals("Day")) {
                // Add the date to current date and set it as next_Date
                dateGoals = addDay(currentDate, repeatNum);
            } else if (repeatType.equals("Week")) {
                // Add the week to current date if none checkbox selected

                // Else

            }
        }
        return dateGoals;
    }




}
