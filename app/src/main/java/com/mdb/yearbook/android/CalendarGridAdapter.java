package com.mdb.yearbook.android;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CalendarGridAdapter extends ArrayAdapter {
    private static final String TAG = CalendarGridAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentGroup;

    public CalendarGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, String currentGroup) {
        super(context, R.layout.grid_cell_layout);
        this.context = context;
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.currentGroup = currentGroup;
        mInflater = LayoutInflater.from(context);
    }
    @NonNull

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        final int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        final int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

//        final String date = ""+currentMonth+String.valueOf(dayValue)+currentYear;
        final Long date = mDate.getTime();
        View view = convertView;
        if(view == null){
            view = mInflater.inflate(R.layout.grid_cell_layout, parent, false);
        }

        final CircleImageView eventImage = (CircleImageView) view.findViewById(R.id.dayImage);

        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            final TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
            cellNumber.setText(String.valueOf(dayValue));
            cellNumber.setTextColor(Color.BLACK);

            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("groupIds").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        currentGroup = ((ArrayList<String>) dataSnapshot.getValue()).get(0);

                        myRef.child("Groups").child(currentGroup).child("photoIds").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> photos = (ArrayList<String>) dataSnapshot.getValue();

                                if (photos != null && photos.size()>0) {
                                    for (final String s : photos) {
                                        myRef.child("Photos").child(s).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Photo p = dataSnapshot.getValue(Photo.class);
                                                if (p != null && p.getDate() != 0) {
                                                    Calendar c = Calendar.getInstance();
                                                    c.setTimeInMillis(p.getDate());
                                                    if (c.get(Calendar.DAY_OF_MONTH) == dayValue && c.get(Calendar.MONTH) + 1 == displayMonth && c.get(Calendar.YEAR) == displayYear
                                                            && p.getGroupIds() != null && p.getGroupIds().contains(currentGroup)) {
                                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("photos/" + s + ".jpg");
                                                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                if (context != null) {
                                                                    Glide.with(YearbookActivity.context)
                                                                            .load(uri)
                                                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                                            .into(eventImage);
                                                                    cellNumber.setTextColor(Color.WHITE);

                                                                }
                                                            }

                                                        });
                                                    }
                                                }


//                                                HashMap<String, Object> photoMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                                                Calendar calendar = null;
//                                                if (photoMap != null) {
//                                                    calendar = Calendar.getInstance();
//                                                    calendar.setTimeInMillis((Long) photoMap.get("date"));
//                                                }
//
//                                                if (photoMap != null && calendar.get(Calendar.DAY_OF_MONTH) == dayValue && (int) calendar.get(Calendar.MONTH) + 1 == displayMonth && calendar.get(Calendar.YEAR) == displayYear
//                                                        && (photoMap.get("groupIds")) != null && !(photoMap.get("groupIds")).equals("") &&
//                                                        (((ArrayList<String>) photoMap.get("groupIds")).get(0)).equalsIgnoreCase(currentGroup)) {
//                                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("photos/" + s + ".jpg");
//                                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                        @Override
//                                                        public void onSuccess(Uri uri) {
//                                                            if (context != null) {
//                                                                Glide.with(YearbookActivity.context)
//                                                                        .load(uri)
//                                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                                                        .into(eventImage);
//                                                                cellNumber.setTextColor(Color.WHITE);
//
//                                                            }
//                                                        }
//
//                                                    });
//                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });



                    }catch (NullPointerException e)
                    {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });

        }else{
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            eventImage.setImageResource(R.drawable.blank);
            TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
            cellNumber.setText(String.valueOf(dayValue));
        }

        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
}
