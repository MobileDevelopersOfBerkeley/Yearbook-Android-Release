package com.mdb.yearbook.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout{
    private static final String TAG = CustomCalendarView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarGridAdapter mAdapter;
    String currentGroup;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public CustomCalendarView(Context context) {
        super(context);
    }
    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_view_layout, this);
        previousButton = (ImageView) view.findViewById(R.id.previous_month);
        nextButton = (ImageView) view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
    }
    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setGridCellClickEvents(){

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date)mAdapter.getItem(position));
                final int year = cal.get(Calendar.YEAR);
                final int month = cal.get(Calendar.MONTH)+1;
                final int day = cal.get(Calendar.DAY_OF_MONTH);
                final String date = ""+month+day+year;

                myRef.child("Photos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Photo> photos = new ArrayList<Photo>();
                        for (DataSnapshot d: dataSnapshot.getChildren())
                        {
//                            HashMap<String, Object> photoMap = (HashMap<String, Object>) d.getValue();
                            Photo p = d.getValue(Photo.class);
                            if (p != null && p.getDate() != 0) {
                                Calendar c = Calendar.getInstance();
                                c.setTimeInMillis(p.getDate());
                                if (c.get(Calendar.DAY_OF_MONTH) == day && c.get(Calendar.MONTH) + 1 == month && c.get(Calendar.YEAR) == year
                                        && p.getGroupIds() != null && p.getGroupIds().contains(currentGroup)) {
                                    photos.add(p);

                                }
                            }
//                            Calendar calendar = null;
//                            if (photoMap != null) {
//                                calendar = Calendar.getInstance();
//                                calendar.setTimeInMillis((Long) photoMap.get("date"));
//                            }
//                            && calendar.get(Calendar.DAY_OF_MONTH) == dayValue && (int) calendar.get(Calendar.MONTH) + 1 == displayMonth && calendar.get(Calendar.YEAR) == displayYear
//                            if (photoMap != null && calendar.get(Calendar.DAY_OF_MONTH) == day && (int) calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.YEAR) == year
//                            && (photoMap.get("groupIds")) != null && !((String)photoMap.get("groupIds")).equals("") && ((ArrayList<String>)photoMap.get("groupIds")).contains(currentGroup))
//                            {
//                                Photo p = new Photo(
//                                        (String) photoMap.get("caption"), (String) photoMap.get("imageUrl"), (String) photoMap.get("posterId"),
//                                        (ArrayList<String>) photoMap.get("groupIds"), (Long) photoMap.get("date"),
//                                        (String) photoMap.get("location")
//                                );
//                                photos.add(p);
//                            }
                        }
                        if (photos.size()>0)
                        {
                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.popup_day, null);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            alertDialogBuilder.setView(promptsView);

                            final RecyclerView dayRecycler = (RecyclerView) promptsView.findViewById(R.id.dayPhotosRecyclerID);
                            dayRecycler.setLayoutManager(new LinearLayoutManager(context));

                            FeedAdapter adapter = new FeedAdapter(context, photos);
                            dayRecycler.setAdapter(adapter);

                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("Close",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

                                                }});

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

            }
        });

    }
    private void setUpCalendarAdapter(){
        final List<Date> dayValueInCells = new ArrayList<Date>();
        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);

        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("groupIds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((ArrayList<String>) dataSnapshot.getValue() != null) {
                    currentGroup = ((ArrayList<String>) dataSnapshot.getValue()).get(0);
                    mAdapter = new CalendarGridAdapter(context, dayValueInCells, cal, currentGroup);
                    calendarGridView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    public GridView getGridView()
    {
        return calendarGridView;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
}