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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogIn_Activity extends AppCompatActivity {
 private EditText emailET, passwordET;
 private TextView signUpTV;
 private Button logInBtn;
 private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        onclick();
        if(firebaseAuth.getCurrentUser()!=null){
            gotoHome();
        }

    }
    private void gotoHome() {
        Intent intent= new Intent(LogIn_Activity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        }


    private void onclick() {
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if (email.equals("")){
                    Toast.makeText(LogIn_Activity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(LogIn_Activity.this, "Give your Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginWithEmailandPassword(email,password);
                }

            }
        });
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn_Activity.this,SignUp_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginWithEmailandPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(LogIn_Activity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                   gotoHome();
               }else{
                   Toast.makeText(LogIn_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private void initialization() {
        emailET=findViewById(R.id.loginemailETid);
        passwordET=findViewById(R.id.loginpasswordETid);
        signUpTV=findViewById(R.id.signupTVid);
        logInBtn=findViewById(R.id.loginbtnid);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();


    }
}
