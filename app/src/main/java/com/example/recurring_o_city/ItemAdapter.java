package com.example.recurring_o_city;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    public ItemAdapter(ArrayList<?> list) {
        for (Object obj : list) {
            if (obj.getClass().equals(Habit.class)) {
                this.habitList = (ArrayList<Habit>) list;
                break;
            }
            else if (obj.getClass().equals(HabitEvent.class)) {
                this.habitEventList = (ArrayList<HabitEvent>) list;
                break;
            }
        }

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_view);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        if (this.habitList != null && this.habitEventList == null) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
        }
        else if (this.habitList == null && this.habitEventList != null) {
            String name = habitEventList.get(position).getEventName();
            holder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if (this.habitList != null && this.habitEventList == null) {
            return this.habitList.size();
        }
        else if (this.habitList == null && this.habitEventList != null) {
            return this.habitEventList.size();
        }
        return -1;
    }
}
