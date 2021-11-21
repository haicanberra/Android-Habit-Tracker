package com.example.recurring_o_city;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/**
 * Generates a view that reuses views instead of creating/destroyer them when the user scrolls by
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;
    private ArrayList<Habit> habitList;
    private String currentFragment;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;

    /**
     * @param list
     */
    public ItemAdapter(ArrayList<Habit> list, String type) {
        this.currentFragment = type;
        this.habitList = list;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton button;
        CheckBox chk;

        /**
         * @param itemView
         * @param listener
         */
        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_view);
            button = itemView.findViewById(R.id.imageButton);
            chk = itemView.findViewById(R.id.checkBox2);


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
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false), myListener);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {

        if (this.currentFragment.equals("today")) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);

        }
        else if (this.currentFragment.equals("event")) {
//            String name = habitEventList.get(position).getEventHabit().getTitle();
//            holder.textView.setText(name);
//            holder.chk.setVisibility(View.GONE);
        }
        else if (this.currentFragment.equals("all")) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (habitList.size()>0) {
                    deleteItem(holder);
                }
//                else if (todayList == null && habitList == null && habitEventList != null && habitEventList.size()>0) {
//                    collectionReference
//                            .document(habitEventList.get(holder.getAdapterPosition()).getEventHabit().getTitle())
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void avoid) {
//                                    Log.d("Habit Event", "Data has been deleted successfully");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("Habit Event", "Data could not be deleted" + e.toString());
//                                }
//                            });
//                   habitEventList.remove(holder.getAdapterPosition());
//                   notifyDataSetChanged();
//                }
            }
        });
    }

    public void deleteItem(MyViewHolder holder){

        collectionReference
                .whereEqualTo("Title",habitList.get(holder.getAdapterPosition()).getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collectionReference.document(document.getId()).delete();
                        Log.d("Habit", "Data has been deleted successfully");
                    }
                } else {
                    Log.d("Habit", "Data could not be deleted" );
                }
            }
        });

//        collectionReference
//                .document(habitList.get(holder.getAdapterPosition()).getTitle())
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void avoid) {
//                        Log.d("Habit", "Data has been deleted successfully");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Habit", "Data could not be deleted" + e.toString());
//                    }
//                });
        habitList.remove(holder.getAdapterPosition());
        notifyDataSetChanged();
    }

    /**
     * @return int
     */
    @Override
    public int getItemCount() {
        if (this.currentFragment.equals("today") || this.currentFragment.equals("all")) {
            return this.habitList.size();
        }
//        else if (this.todayList == null && this.habitList == null && this.habitEventList != null) {
//            return this.habitEventList.size();
//        }
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
