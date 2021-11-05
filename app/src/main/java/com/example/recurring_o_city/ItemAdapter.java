package com.example.recurring_o_city;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;
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
        ImageButton button;
        CheckBox chk;

        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_view);
            button = itemView.findViewById(R.id.imageButton);
            chk = itemView.findViewById(R.id.checkBox2);


            itemView.setOnClickListener(new View.OnClickListener() {
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

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false), myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        if (this.habitList != null && this.habitEventList == null) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
        }
        else if (this.habitList == null && this.habitEventList != null) {
            String name = habitEventList.get(position).getEventHabit().getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (habitList != null && habitEventList == null && habitList.size()>0) {
                    habitList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                else if (habitList == null && habitEventList != null && habitEventList.size()>0) {
                    habitEventList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }

            }
        });
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }
}
