package com.example.emon.tourmate.Other_class;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PublicKey;

public class DatabaseRef {

    public static DatabaseReference mainRef =FirebaseDatabase.getInstance().getReference();

    public static final DatabaseReference userRef = mainRef.child("Users");


}
