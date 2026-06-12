package com.example.transportbuddy;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class activity_Forgot_Password extends AppCompatActivity {

    TextInputLayout resetEmail;
    Button btn_sendResetMail;

    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_fp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sendResetEmail();
    }
    private void sendResetEmail(){
        resetEmail = findViewById(R.id.fp_email);
        btn_sendResetMail = findViewById(R.id.btn_sendResetMail);
        resetEmail.setOnClickListener(v -> {
            value = Objects.requireNonNull(resetEmail.getEditText()).getText().toString();
            if (value.isEmpty()) {
                resetEmail.setError("Field cannot be Empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                resetEmail.setError("Invalid Email Address");
            } else {
                sendMail();
            }
        });
    }
    private void sendMail(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(value).addOnCompleteListener(activity_Forgot_Password.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activity_Forgot_Password.this, "Password Reset Mail Sent.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity_Forgot_Password.this, "SomeThing went wrong TRY AGAIN!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}