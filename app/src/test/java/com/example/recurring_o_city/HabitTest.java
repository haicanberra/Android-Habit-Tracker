package com.example.recurring_o_city;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Date;

public class HabitTest {

    private Habit mockHabit(){
        Date date = new Date();
        return new Habit("Walking", "just bored", date,1);
    }

    @Test
    public void testGetTitle(){
        Habit habit = mockHabit();
        assertEquals("Walking", habit.getTitle());
    }

    @Test
    public void testGetReason() {
        Habit habit = mockHabit();
        assertEquals("Just bored", habit.getReason());
    }

    @Test
    public void testGetPrivacy(){
        Habit habit = mockHabit();
        assertEquals(1, habit.getPrivacy());
    }



}
