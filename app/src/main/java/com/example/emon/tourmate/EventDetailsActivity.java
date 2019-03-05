package com.example.emon.tourmate;


import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.emon.tourmate.Other_class.DatabaseRef;
import com.example.emon.tourmate.Other_class.Expense;
import com.example.emon.tourmate.Other_class.ExpenseListAdapter;
import com.example.emon.tourmate.Other_class.PhotoAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934

public class EventDetailsActivity extends AppCompatActivity {
    TextView TourTitle,BUDGET,myBudgetTv, FromToTV;
    FloatingActionButton fab_expense , fab_moments;
    private String eventID;
    private List<Expense> expenses;
    private ExpenseListAdapter adapter;

    private RecyclerView myrecyclerView,recyclerView;
    private  String Uid,id;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private int Allamount,newBudget,value, amount;
    private int Budget;
    private Expense expense=new Expense();

    DatabaseReference databaseReference;



    public  List<Uri> uriList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        //imageView=findViewById(R.id.myimageid);

        Intent intent=getIntent();
        eventID= intent.getStringExtra("id");
        String Tourname= intent.getStringExtra("tourname");
        String startDate=intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        String startingPlace = intent.getStringExtra("startingLocation");
        String destination = intent.getStringExtra("destination");
        Budget= intent.getIntExtra("budget",0);

        expenses=new ArrayList<>();
        LoadexpenseFromDatabase();


        adapter=new ExpenseListAdapter(expenses,this);



      //  myrecyclerView=findViewById(R.id.myrcyclrID);
        recyclerView=findViewById(R.id.myrcyclrID);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);





        FromToTV=findViewById(R.id.FromToTvid);
        TourTitle=findViewById(R.id.TourNameTVId);
        BUDGET=findViewById(R.id.BudgetTvid);
        myBudgetTv=findViewById(R.id.MYBudgetTvid);
        myBudgetTv.setText(String.valueOf(Budget));
        TourTitle.setText(Tourname);
        FromToTV.setText("From "+startingPlace+", To "+destination);
        Allamount+=expense.getAmount();
        value=amount+Allamount;
        newBudget=Budget-Allamount;
        //Toast.makeText(this, String.valueOf(newBudget), Toast.LENGTH_SHORT).show();
        BUDGET.setText(String.valueOf(newBudget));

        fab_expense=findViewById(R.id.addExpensemenuid);
        fab_moments=findViewById(R.id.addmomentsmenuid);

        fab_expense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog myDialog= new Dialog(EventDetailsActivity.this);
                myDialog.setContentView(R.layout.addexpense_dialog);
                final EditText amounttv=myDialog.findViewById(R.id.amountEtId);
                final EditText descriptiontv=myDialog.findViewById(R.id.descriptionEtid);
                Button cancelbtn,addbtn;
                cancelbtn=myDialog.findViewById(R.id.cancelbtnid);
                addbtn=myDialog.findViewById(R.id.addbtnid);
                myDialog.show();
                myDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT);
                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amount= Integer.parseInt(amounttv.getText().toString());
                        String description=descriptiontv.getText().toString();
                        value=amount+Allamount;
                        if (value>Budget){
                            Toast.makeText(EventDetailsActivity.this, "Isufficient Budget", Toast.LENGTH_SHORT).show();

                        }else {
                            myDialog.dismiss();
                            saveexpenseToDatabase(amount,description);
                            newBudget=Budget-value;
                            BUDGET.setText(String.valueOf(newBudget));

                            Toast.makeText(EventDetailsActivity.this, "New Expense Added", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });



            }
        });
        fab_moments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsActivity.this,PhotoMomentsActivity.class));

            }
        });

//     String budeget= (String) BUDGET.getText();
//     int value= Integer.parseInt(String.valueOf(budeget));
//     int newvalue=value-800;
//     Toast.makeText(this, String.valueOf(newvalue), Toast.LENGTH_SHORT).show();



    }

    private void saveexpenseToDatabase(int amount, String description) {
        Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        id=DatabaseRef.userRef.child(Uid).child("EventList").child(eventID).child("expenses").push().getKey();
        Expense expense=new Expense(description,id,amount);
        DatabaseRef.userRef.child(Uid).child("EventList").child(eventID).child("expenses").child(id).setValue(expense);
    }



    private void LoadexpenseFromDatabase() {
        String userId=firebaseAuth.getCurrentUser().getUid();
        DatabaseRef.userRef.child(userId).child("EventList").child(eventID).child("expenses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                expense=dataSnapshot.getValue(Expense.class);
                Allamount+=expense.getAmount();
                expenses.add(expense);
                newBudget=Budget-Allamount;
               // adapter.notifyDataSetChanged();

                BUDGET.setText(String.valueOf(newBudget));

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


}
