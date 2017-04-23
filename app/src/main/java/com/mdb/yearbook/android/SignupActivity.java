package com.mdb.yearbook.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signupButton;
    private EditText signupEmailText, signupPasswordText, signupNameText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupButton = (Button)findViewById(R.id.signupButtonID);
        signupButton.setOnClickListener(this);
        signupButton.setEnabled(true);


        signupNameText = (EditText)findViewById(R.id.signupNameTextID);
        signupEmailText = (EditText)findViewById(R.id.signupEmailTextID);
        signupPasswordText = (EditText)findViewById(R.id.signupPasswordTextID);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(SignupActivity.this, YearbookActivity.class));
                    finish();
                }
            }
        };
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signupButtonID:
                signUp();
                break;
        }
    }

    public void signUp() {
        final String userName, userPassword, userEmail;
        userName = signupNameText.getText().toString().trim();
        userEmail = signupEmailText.getText().toString().trim();
        userPassword = signupPasswordText.getText().toString();

        if (userPassword == null || userEmail == null || userEmail.length() == 0 || userPassword.length() == 0 || userName.length()==0) {
            Toast.makeText(getApplicationContext(), "Fill All Fields Correctly", Toast.LENGTH_SHORT).show();
        } else {
            signupButton.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String key = mAuth.getCurrentUser().getUid();
                                String groupKey = mDatabase.child("Groups").push().getKey();

                                ArrayList<String> userGroups = new ArrayList<String>();
                                userGroups.add(groupKey);
                                ArrayList<String> newGroupMembers = new ArrayList<String>();
                                newGroupMembers.add(key);
                                long firstDate = System.currentTimeMillis();

                                Group newGroup = new Group(firstDate,userName+"'s YearBook",key,new ArrayList<String>(),0,newGroupMembers,
                                        new ArrayList<String>(),0,"","Personal Album",new HashMap<String, Long>(),-1,-1);
                                User newUser = new User(userName, userEmail, "", userGroups);

                                mDatabase.child("Users").child(key).setValue(newUser);
                                mDatabase.child("Groups").child(groupKey).setValue(newGroup);
                            } else if (!(task.isSuccessful())) {
                                Toast.makeText(SignupActivity.this, "Sign Up Failed.",
                                        Toast.LENGTH_LONG).show();
                                signupButton.setEnabled(true);
                            }
                        }
                    });
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}