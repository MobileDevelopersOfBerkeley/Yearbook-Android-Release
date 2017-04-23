package com.mdb.yearbook.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button resetPassword;
    EditText emailAddressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPassword = (Button)findViewById(R.id.resetPassword);
        emailAddressField = (EditText) findViewById(R.id.emailResetField);
        resetPassword.setOnClickListener(this);




    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.resetPassword:
                sendResetPassword();
                break;
        }
    }

    public void sendResetPassword() {
        if (resetPassword.getText().toString().equals("")) {
            return;

        } else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = emailAddressField.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Success:", "Email sent.");
                                Toast.makeText(getApplicationContext(), "Email Sent!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("Failure:", "Email not sent due to bad email.");
                                Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
