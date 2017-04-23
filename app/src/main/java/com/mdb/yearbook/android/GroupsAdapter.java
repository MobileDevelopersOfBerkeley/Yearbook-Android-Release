package com.mdb.yearbook.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by MEEEE on 4/4/17.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.CustomViewHolder> {

    private Context context;
    ArrayList<Group> groupsList;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    android.app.AlertDialog alertDialog;

    public GroupsAdapter(Context context, ArrayList<Group> groupsList, DatabaseReference mDatabase, FirebaseAuth mAuth, android.app.AlertDialog alertDialog) {
        this.context = context;
        this.groupsList = groupsList;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
        this.alertDialog = alertDialog;
    }

    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Group g = groupsList.get(position);
        holder.title.setText(g.getTitle());
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public CustomViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.groupTitleText);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("groupIds").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> groupIds = (ArrayList<String>)dataSnapshot.getValue();
                            String selectedGroup = groupIds.get(getAdapterPosition());
                            String current = groupIds.get(0);
                            groupIds.set(0, selectedGroup);
                            groupIds.set(getAdapterPosition(), current);
                            mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("groupIds").setValue(groupIds);
                            Toast.makeText(context, "Group Selected!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }
    }

    public void updateList(ArrayList<Group> newList)
    {
        this.groupsList = newList;
    }

}