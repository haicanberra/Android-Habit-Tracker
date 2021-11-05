package com.example.recurring_o_city;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Generates a view that reuses views instead of creating/destroyer them when the user scrolls by
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;

    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    /**
     * @param list
     */
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

        /**
         * @param itemView
         * @param listener
         */
        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * @param parent
     * @param viewType
     * @return ViewHolder, or the thing that wraps views to allow reuse
     */
    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false), myListener);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        if (this.habitList != null && this.habitEventList == null) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
        }
        else if (this.habitList == null && this.habitEventList != null) {
            String name = habitEventList.get(position).getEventHabit().getTitle();
            holder.textView.setText(name);
        }
    }

    /**
     * @return int
     */
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }
}
