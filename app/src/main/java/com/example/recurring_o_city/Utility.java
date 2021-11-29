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


    public Utility() {
    }



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

    public Date getNextDate(Date createdDate, Date nextDate, List<String> repeat) {
        //When first created, next date = null, just call get next date
        Date dateGoals = null;

        Date currentDate = getCurrentDate();
        //Date currentDate = util.getCurrentDate();
        // Update the next date
        String repeatType = repeat.get(1);
        Integer repeatNum = Integer.valueOf(repeat.get(0));
        //Find the next date after the created date
        if (nextDate == null) {
            if (createdDate.before(currentDate)) {
                Date temp = createdDate;
                while (temp.before(currentDate)) {
                    // Choose to repeat by days
                    if (repeatType.equals("day")) {
                        temp = addDay(temp, repeatNum);
                    } else {
                        // Choose to repeat by week, no checkbox
                        if (repeat.size() == 3) {
                            temp = addDay(temp, repeatNum * 7);
                        }
                    }
                }
                dateGoals = temp;
            } else {
                dateGoals = createdDate;
            }
        } else {
            // Find next date based on next date
            if (repeatType.equals("day")) {
                // Add the date to current date and set it as next_Date
                dateGoals = addDay(nextDate, repeatNum);
            } else if (repeatType.equals("week")) {
                // Add the week to current date if none checkbox selected
                if (repeat.size() == 3) {
                    //Next date is today date
                    dateGoals = addDay(nextDate, 7 * repeatNum);
                }
            }
        }
        return dateGoals;
    }

}



    //    public Date getNextDate(Habit newHabit) {
//        Date dateGoals = null;
//        Utility util = new Utility();
//        Date currentDate = util.getCurrentDate();
//        // If user chooses to repeat, find the next date. Else return null
//        if (newHabit.getRepeat() != null) {
//            String repeatType = newHabit.getRepeat().get(1);
//            Integer repeatNum = Integer.valueOf(newHabit.getRepeat().get(0));
//            String last = newHabit.getRepeat().get(newHabit.getRepeat().size() - 1);
//
//            // Update date goals
//            if (newHabit.getDate().after(currentDate)) {
//                // No need to find next date
//                dateGoals = (newHabit.getDate());
//            } else if (newHabit.getDate().equals(currentDate)) {
//                dateGoals = (newHabit.getDate()); // Need to find next date if time_occur >= 1
//            } else if (newHabit.getDate().before(currentDate)) {
//                Date temp = newHabit.getDate();
//                while (temp.before(currentDate)) {
//                    // Get the repeat result
//                    if (repeatType.equals("Day")) {
//                        // Add the day to temp till it pass the current date
//                        temp = util.addDay(temp, repeatNum);
//                    } else if (repeatType.equals("Week") && newHabit.getRepeat().size() == 3) {
//                        // Never ends but repeat... week on the name of date started
//                        temp = util.addDay(temp, repeatNum*7);
//                        // Case: repeat ... week on Mon,Tue,....
//                    } else if (repeatType.equals("Week") && newHabit.getRepeat().size() > 3) {
//                        // The earliest name of date would be the next item in list
//                        temp = currentDate;
//                        // Find the next date
//
//                    }
//                }
//                // For the case repeat on .....
//
//
//                // Now temp can either be equal or after current date
//                dateGoals = temp;
//            }
//        }
//        return dateGoals;
//    }


