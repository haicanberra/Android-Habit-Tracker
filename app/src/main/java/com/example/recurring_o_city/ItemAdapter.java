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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Generates a view that reuses views instead of creating/destroyer them when the user scrolls by
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private OnItemClickListener myListener;
    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;
    private String currentFragment;
    private CollectionReference collectionReference, collectionReference2;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ItemAdapter(ArrayList<?> list, String type) {
        this.currentFragment = type;
        if (type.equals("event")) {
            this.habitEventList = (ArrayList<HabitEvent>) list;
        } else if (type.equals("today") || type.equals("all") || type.equals("fyhf")) {
            this.habitList = (ArrayList<Habit>) list;
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton button;
        CheckBox chk;
        TextView date;

        /**
         * @param itemView
         * @param listener
         */
        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_view);
            button = itemView.findViewById(R.id.imageButton);
            chk = itemView.findViewById(R.id.checkBox2);
            date = itemView.findViewById(R.id.dateCreated);


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
        collectionReference2 = db.collection("Habit Events");
        mAuth = FirebaseAuth.getInstance();
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false), myListener);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int pos) {
        int position = pos;
        if (this.currentFragment.equals("today")) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setChecked(toBoolean(habitList.get(position).getDone()));
            if (toBoolean(habitList.get(position).getDone())) {
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            holder.date.setVisibility(View.GONE);
            holder.button.setVisibility(View.INVISIBLE);
        }
        else if (this.currentFragment.equals("all")) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
            holder.date.setVisibility((View.GONE));
        }
        else if (this.currentFragment.equals("fyhf")) {
            String name = habitList.get(position).getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);
            holder.date.setVisibility((View.GONE));
            holder.button.setVisibility(View.INVISIBLE);
        }
        else if (this.currentFragment.equals("event")) {
            String name = habitEventList.get(position).getEventHabit().getTitle();
            holder.textView.setText(name);
            holder.chk.setVisibility(View.GONE);

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd 'at' h:mm a");
            Date date = habitEventList.get(position).getDateCreated();
            String date_string = format2.format(date);
            holder.date.setText(date_string);

        }

        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    collectionReference
                            .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                            .whereEqualTo("Title",habitList.get(position).getTitle())
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
                            .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                            .whereEqualTo("Title",habitList.get(position).getTitle())
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
                if ((currentFragment.equals("today") || currentFragment.equals("all"))) {
                    deleteHabit(holder);
                }
                else if ((currentFragment.equals("event"))) {
                    deleteEvent(holder);
                }
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

        data.put("User Id", mAuth.getCurrentUser().getUid());
        data.put("Title", doc.getString("Title"));
        data.put("Reason", doc.getString("Reason"));
        data.put("Date", doc.getDate("Date"));
        data.put("DateCreated", dateCreated);
        data.put("Privacy", Integer.valueOf(doc.getLong("Privacy").toString()));

        List<String> repeat = (List<String>)doc.get("Repeat");
        if (repeat != null) {
            data.put("Repeat", repeat);
        }

        // This part may change later.
        data.put("Comment", null);
        data.put("Photograph", null);
        data.put("Location", null);

        // Add to collection habit event
        collectionReference2
                .document()
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("New Habit Event", "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("New Habit Event", "Data could not be added" + e.toString());
                    }
                });
    }

    // Delete habit event when habit is unchecked.
    private void undoHabitEvent(DocumentSnapshot doc) {
        // Get most recent document from collection habit event
        collectionReference2
                .whereEqualTo("User Id", doc.get("User Id"))
                .whereEqualTo("Title", doc.get("Title"))
                .orderBy("DateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().getDocuments().size() > 0) {
                            task.getResult().getDocuments().get(0).getReference().delete();
                        }
                    }
                });

    }

    private void deleteHabit(MyViewHolder holder){
        // Delete the habit from habit list
        collectionReference
                .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
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

        // Delete all corresponding habit events
        collectionReference2
                .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                .whereEqualTo("Title",habitList.get(holder.getAdapterPosition()).getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                collectionReference2.document(document.getId()).delete();
                                Log.d("Delete Habit", "Data has been deleted successfully");
                            }
                        } else {
                            Log.d("Delete Habit", "Data could not be deleted" );
                        }
                    }
                });
        if (habitEventList != null ) {
            habitEventList.remove(holder.getAdapterPosition());
        }
        habitList.remove(holder.getAdapterPosition());
        notifyDataSetChanged();
    }

    private void deleteEvent(MyViewHolder holder) {
        collectionReference2
                .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                .whereEqualTo("Title",habitEventList.get(holder.getAdapterPosition()).getEventHabit().getTitle())
                .whereEqualTo("DateCreated",habitEventList.get(holder.getAdapterPosition()).getDateCreated())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                collectionReference2.document(document.getId()).delete();
                                Log.d("Delete Habit", "Data has been deleted successfully");
                            }
                        } else {
                            Log.d("Delete Habit", "Data could not be deleted" );
                        }
                    }
                });

        habitEventList.remove(holder.getAdapterPosition());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if ((this.currentFragment.equals("today") || this.currentFragment.equals("all") || this.currentFragment.equals("fyhf")) && this.habitList != null ) {
            return this.habitList.size();
        }
        else if (this.currentFragment.equals("event") && this.habitEventList != null) {
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
