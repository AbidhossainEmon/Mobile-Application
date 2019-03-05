package com.example.emon.tourmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emon.tourmate.Other_class.DatabaseRef;
import com.example.emon.tourmate.Other_class.EventClass;
import com.example.emon.tourmate.Other_class.EventListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.emon.tourmate.SignUp_Activity.EMAIL;
import static com.example.emon.tourmate.SignUp_Activity.USERNAME;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<EventClass> eventList;
    private EventListAdapter eventListAdapter;
    private String userId;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private RecyclerView myRecylerView;
    FloatingActionButton fab;
    EditText tournameTv, startPlaceTv, destinationTv, startDateTv,endDateTv,budgetTv;
    BottomSheetBehavior sheetBehavior;
    Button createEventbtn;
    public static Double LAT;
    public static Double LON;


    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private static int REQUEST_CODE_FOR_LOCATION = 1;

    String startDate,endDate;
    DatePickerDialog.OnDateSetListener startdate,enddate;
    int budget=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent=getIntent();

        List<EventClass> events= loadEventList();
       final Calendar myCalendar=Calendar.getInstance();
          startdate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDate =sdf.format(myCalendar.getTime());
                startDateTv.setText(startDate);
            }


        };
        enddate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                endDate =sdf.format(myCalendar.getTime());
                endDateTv.setText(endDate);
            }


        };



        startDateTv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, startdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, enddate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    LAT = location.getLatitude();
                    LON = location.getLongitude();
                   // Toast.makeText(MainActivity.this, String.valueOf(LAT), Toast.LENGTH_SHORT).show();

                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_FOR_LOCATION);
            return;
        }
        client.requestLocationUpdates(request, callback, null);

    }


//        private void updateLabel() {
//
//        }


    private List<EventClass> loadEventList() {
        initialization();
        eventList=new ArrayList<>();
        loadEventListFromDatabase();
        eventListAdapter=new EventListAdapter(this,eventList);

        myRecylerView=findViewById(R.id.myRecyclerViewid);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        myRecylerView.setLayoutManager(layoutManager);
        myRecylerView.setAdapter(eventListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        createEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TourName= tournameTv.getText().toString();
                String startPlace =startPlaceTv.getText().toString();
                String destination= destinationTv.getText().toString();
                String value =budgetTv.getText().toString();
                if(value.equals("")){
                    Toast.makeText(MainActivity.this, "Enter your Budget Amount", Toast.LENGTH_SHORT).show();
                }else {
                    budget= Integer.parseInt(value);
                }

                if(TourName.equals("")|| startPlace.equals("")||destination.equals("")|| startDate.equals("")||endDate.equals("")||value.equals("")){
                    Toast.makeText(MainActivity.this, "Please Fillup All the Empty Field", Toast.LENGTH_SHORT).show();
                }else {

                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    saveeventToDatabase(TourName,startPlace,destination, startDate,endDate,budget);
                    tournameTv.setText("");
                    startPlaceTv.setText("");
                    destinationTv.setText("");
                    startDateTv.setText("");
                    endDateTv.setText("");
                    budgetTv.setText("");
                    Toast.makeText(MainActivity.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(EventListActivity.this,EventListActivity.class));
                }
            }
        });

     return eventList;
    }

    private void loadEventListFromDatabase() {
        userId=firebaseAuth.getCurrentUser().getUid();
        DatabaseRef.userRef.child(userId).child("EventList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventClass event = dataSnapshot.getValue(EventClass.class);
                eventList.add(event);
                eventListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initialization() {


            tournameTv = findViewById(R.id.TourNameTvid);
            startPlaceTv=findViewById(R.id.startingPlaceTvid);
            destinationTv=findViewById(R.id.destinationTvid);
            startDateTv=findViewById(R.id.startDateTvid);
            endDateTv=findViewById(R.id.endDateTvid);
            budgetTv=findViewById(R.id.budgetTvid);

            createEventbtn=findViewById(R.id.createEventbtnid);

            fab = (FloatingActionButton) findViewById(R.id.fab);


            View bottomSheet = findViewById(R.id.bottom_sheet);
            sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_usernametvid);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_header_emailtvid);
        navUsername.setText(USERNAME);
        navEmail.setText(EMAIL);


        }


    private void saveeventToDatabase(String tourName, String startPlace, String destination,
                                     String starDate, String endDate, int budget) {

        userId= firebaseAuth.getCurrentUser().getUid();
        String id =DatabaseRef.userRef.child(userId).child("EventList").push().getKey();
        EventClass event = new EventClass(id,tourName,startPlace,destination,starDate,endDate,budget);
        DatabaseRef.userRef.child(userId).child("EventList").child(id).setValue(event);
    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }else {
            super.onBackPressed();
        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_event) {
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }else if (id == R.id.nav_all_photo) {
            startActivity(new Intent(MainActivity.this,PhotoMomentsActivity.class));
        }
        else if (id == R.id.nav_nearby) {
            startActivity(new Intent(MainActivity.this,NearbyPlaceActivity.class));
        } else if (id == R.id.nav_weather) {
            startActivity(new Intent(MainActivity.this,WeatherActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this,LogIn_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
