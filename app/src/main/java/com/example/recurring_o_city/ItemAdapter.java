package com.example.recurring_o_city;

import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> todayList;
    private ArrayList<HabitEvent> habitEventList;



    public ItemAdapter(ArrayList<?> list, String type) {
        for (Object obj : list) {
            if (obj.getClass().equals(Habit.class) && type == "today") {
                this.todayList = (ArrayList<Habit>) list;
                this.habitList = null;
                this.habitEventList = null;
                break;
            }
            else if (obj.getClass().equals(Habit.class) && type == "all") {
                this.todayList = null;
                this.habitList = (ArrayList<Habit>) list;
                this.habitEventList = null;
                break;
            }
            else if (obj.getClass().equals(HabitEvent.class) && type == "event") {
                this.todayList = null;
                this.habitList = null;
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
        CollectionReference collectionReference;
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");

        if (this.todayList != null && this.habitList == null && this.habitEventList == null) {
            String name = todayList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setOnCheckedChangeListener(null);


        }
        else if (this.todayList == null && this.habitList == null && this.habitEventList != null) {
            String name = habitEventList.get(position).getEventHabit().getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
        }
        else if (this.todayList == null && this.habitList != null && this.habitEventList == null) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todayList == null && habitList != null && habitEventList == null && habitList.size()>0) {
                    collectionReference
                            .document(habitList.get(holder.getAdapterPosition()).getTitle())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d("Habit", "Data has been deleted successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Habit", "Data could not be deleted" + e.toString());
                                }
                            });
                    habitList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                else if (todayList != null && habitList == null && habitEventList == null && todayList.size()>0) {
                    collectionReference
                            .document(todayList.get(holder.getAdapterPosition()).getTitle())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d("Habit", "Data has been deleted successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Habit", "Data could not be deleted" + e.toString());
                                }
                            });
                    todayList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();

                }
                else if (todayList == null && habitList == null && habitEventList != null && habitEventList.size()>0) {
                    collectionReference
                            .document(habitEventList.get(holder.getAdapterPosition()).getEventHabit().getTitle())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d("Habit Event", "Data has been deleted successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Habit Event", "Data could not be deleted" + e.toString());
                                }
                            });
                   habitEventList.remove(holder.getAdapterPosition());
                   notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.todayList != null && this.habitList == null && this.habitEventList == null) {
            return this.todayList.size();
        }
        else if (this.todayList == null && this.habitList == null && this.habitEventList != null) {
            return this.habitEventList.size();
        }
        else if (this.todayList == null && this.habitList != null && this.habitEventList == null) {
            return this.habitList.size();
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
