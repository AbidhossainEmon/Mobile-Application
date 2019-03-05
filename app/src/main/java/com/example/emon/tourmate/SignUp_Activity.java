package com.example.emon.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emon.tourmate.Other_class.DatabaseRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class SignUp_Activity extends AppCompatActivity {
    private EditText emailtv, passtv,confirmpassTv,usernametv;
    private TextView gotologinTV;
    private Button signUpbtn;
    private String email;
    public String username;
    private FirebaseAuth FA;
    public static String EMAIL,USERNAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        initialization();
        onclick();

    }


    private void onclick() {
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailtv.getText().toString();
                String password=passtv.getText().toString();
                String confirmpass=confirmpassTv.getText().toString();
                username=usernametv.getText().toString();

                if (email.equals("")){
                    Toast.makeText(SignUp_Activity.this, "Email cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(SignUp_Activity.this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
                }
               else if (confirmpass.equals("")){
                    Toast.makeText(SignUp_Activity.this, "Give Confirm Password", Toast.LENGTH_SHORT).show();
                }
              else  if (username.equals("")){
                    Toast.makeText(SignUp_Activity.this, "UserName cannot be Empty", Toast.LENGTH_SHORT).show();
                }
              else  if (!password.matches(confirmpass)){
                    Toast.makeText(SignUp_Activity.this, "Passwords not Matching", Toast.LENGTH_SHORT).show();
                }else {
                    signUpwithEmailandPassword(email,password);
               }

            }
        });
        gotologinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp_Activity.this,LogIn_Activity.class));
                finish();
            }
        });
    }

    private void signUpwithEmailandPassword(String email,String password) {
        FA.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp_Activity.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                    gotoHome();

                }else {
                    Toast.makeText(SignUp_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void gotoHome() {
        Intent intent= new Intent(SignUp_Activity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        String userid=FA.getCurrentUser().getUid();
        DatabaseRef.userRef.child(userid).child("email").setValue(email);
        DatabaseRef.userRef.child(userid).child("Username").setValue(username);
        DatabaseRef.userRef.child(userid).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EMAIL= (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseRef.userRef.child(userid).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                USERNAME = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        startActivity(intent);
    }

    private void initialization() {
        emailtv=findViewById(R.id.signupemailETid);
        passtv=findViewById(R.id.signuppasswordETid);
        confirmpassTv=findViewById(R.id.signupconpassETid);
        usernametv=findViewById(R.id.signupUsernameETid);
        signUpbtn=findViewById(R.id.Signupbtnid);

        gotologinTV=findViewById(R.id.gotologinTVid);

       FA=FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
}
