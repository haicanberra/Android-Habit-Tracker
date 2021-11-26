package com.example.recurring_o_city;

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
                if (repeat.get(i).equals("week") && i < repeat.size()-2) {
                    repeat_display += " on ";
                } else if (!repeat.get(i).equals("week") &&
                        !repeat.get(repeat.size() - 1).equals("never")) {
                    repeat_display += ", ";
                }
            }
            if (repeat.get(1).equals("week") && i < repeat.size() - 1 && i > 1) {
                repeat_display += repeat.get(i);
                if (!repeat.get(i+1).equals("never")) {
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

}
