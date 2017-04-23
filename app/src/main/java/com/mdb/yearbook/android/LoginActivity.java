package com.mdb.yearbook.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private TextView signupTextView, forgotPassword;
    private EditText loginEmailText, loginPasswordText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButtonID);
        loginButton.setOnClickListener(this);
        signupTextView = (TextView)findViewById(R.id.loginSignupButtonID);
        signupTextView.setOnClickListener(this);
        loginEmailText = (EditText)findViewById(R.id.loginEmailTextID);
        loginPasswordText = (EditText)findViewById(R.id.loginPasswordTextID);
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordTextView);
        forgotPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(LoginActivity.this, YearbookActivity.class));
                    finish();
                } else {
                    loginButton.setEnabled(true);
                }
            }
        };
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.loginButtonID:
                signIn();
                break;
            case R.id.loginSignupButtonID:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.forgotPasswordTextView:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
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

    public void signIn()
    {
        String email = loginEmailText.getText().toString();
        String password = loginPasswordText.getText().toString();
        if (password == null || email == null || email.length() == 0 || password.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("wellthisfailed", "signInWithEmail", task.getException());
                                Toast.makeText(getApplicationContext(), "Sign in Failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                loginButton.setEnabled(false);
                            }
                        }
                    });
        }
    }
}
