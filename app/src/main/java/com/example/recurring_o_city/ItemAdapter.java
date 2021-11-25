package com.example.recurring_o_city;

import android.graphics.Paint;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;
    private ArrayList<Habit> habitList;
    private String currentFragment;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;

    public ItemAdapter(ArrayList<Habit> list, String type) {
        this.currentFragment = type;
        this.habitList = list;
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
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false), myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        Habit selectedHabit = habitList.get(position);
        if (this.currentFragment.equals("today")) {
            String name = selectedHabit.getTitle();
            holder.textView.setText(name);
            holder.chk.setChecked(toBoolean(selectedHabit.getDone()));
            if (toBoolean(selectedHabit.getDone())) {
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        else if (this.currentFragment.equals("event")) {
//            String name = habitEventList.get(position).getEventHabit().getTitle();
//            holder.textView.setText(name);
//            holder.chk.setVisibility(View.GONE);
        }
        else if (this.currentFragment.equals("all")) {
            String name = selectedHabit.getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
        }

        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    collectionReference
                            .whereEqualTo("Title",selectedHabit.getTitle())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            collectionReference.document(document.getId()).update("Done","true");
                                            Log.d("Done Habit", "Data has been marked done successfully");

                                            // Function for creating new habit event.
                                            createHabitEvent(document);
                                        }
                                    } else {
                                        Log.d("Done Habit", "Data could not be marked done" );
                                    }
                                }
                            });
                }
                else {
                    collectionReference
                            .whereEqualTo("Title",selectedHabit.getTitle())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            collectionReference.document(document.getId()).update("Done","false");
                                            Log.d("Done Habit", "Data has been marked done successfully");

                                            // Remove the habit event.
                                            undoHabitEvent(document);
                                        }
                                    } else {
                                        Log.d("Done Habit", "Data could not be marked done" );
                                    }
                                }
                            });
                }
            }
        });

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

    private boolean toBoolean(String done) {
        if (done.equals("false")) {
            return false;
        }
        else{
            return true;
        }
    }

    // Add habit event when habit is checked/crossed off.
    private void createHabitEvent(DocumentSnapshot doc) {

        // Needs date of creation for undoing the last habit event.
        Date dateCreated = new Date();

        HashMap<String, Object> data = new HashMap<>();
        data.put("Title", doc.getString("Title"));
        data.put("Reason", doc.getString("Reason"));
        data.put("Date", doc.getDate("Date"));
        data.put("DateCreated", dateCreated);

        List<String> repeat = (List<String>)doc.get("Repeat");
        if (repeat != null) {
            data.put("Repeat", repeat);
        }

        // This part may change later.
        data.put("Comment", null);
        data.put("Photograph", null);
        data.put("Location", null);

        collectionReference
                .document(doc.getId())              // Current document
                .collection("Events")   // Create new sub-collection
                .add(data);                         // Add data to the sub-collection.
    }

    // Delete habit event when habit is unchecked.
    private void undoHabitEvent(DocumentSnapshot doc) {

        // Get the most recent document, and delete it.
        collectionReference
                .document(doc.getId())
                .collection("Events")
                .orderBy("DateCreated", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                // Get the first document on the collection, delete it.
                task.getResult().getDocuments().get(0).getReference().delete();
            }
        });
    }

    private void deleteItem(MyViewHolder holder){
        collectionReference
                .whereEqualTo("Title",habitList.get(holder.getAdapterPosition()).getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collectionReference.document(document.getId()).delete();
                        Log.d("Delete Habit", "Data has been deleted successfully");
                    }
                } else {
                    Log.d("Delete Habit", "Data could not be deleted" );
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }
}
