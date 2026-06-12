package com.example.transportbuddy;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_Signup extends AppCompatActivity {

    String value,Name,Mail,Pwd,Phone, Dob, Gender,mobileRegex;
    TextInputLayout details_userName, details_userMail, details_userPhone, details_userPwd;
    Button btn_signUp;
    DatabaseReference reference;
    FirebaseAuth auth;
    userDetails user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fn_signUp_User();
    }

    private void fn_signUp_User() {
        details_userName = findViewById(R.id.details_userName);
        details_userMail = findViewById(R.id.details_userMail);
        details_userPhone = findViewById(R.id.details_userPhone);
        details_userPwd = findViewById(R.id.details_userPwd);
        btn_signUp = findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(v -> {
            if (!validateUsername() | !validatePassword() | !validateEmail() | !validatePhone()) {
                Toast.makeText(activity_Signup.this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                fn_addUserDetails();
            }
        });
    }

    private boolean validateUsername() {
        value = Objects.requireNonNull(details_userName.getEditText()).getText().toString();
        if (value.isEmpty()) {
            details_userName.setError("Field cannot be empty");
            return false;
        } else if (value.length() >= 15) {
            details_userName.setError("Length is to long");
            return false;
        } else {
            details_userName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        value = Objects.requireNonNull(details_userMail.getEditText()).getText().toString();
        if (value.isEmpty()) {
            details_userMail.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            details_userMail.setError("Invalid E-mail address");
            return false;
        } else {
            details_userMail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        value = Objects.requireNonNull(details_userPwd.getEditText()).getText().toString();
        if (value.isEmpty()) {
            details_userPwd.setError("Field cannot be empty");
            return false;
        } else if (value.length() <= 8) {
            details_userPwd.setError("Length Should more than 6 characters");
            return false;
        } else {
            details_userPwd.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        value = Objects.requireNonNull(details_userPhone.getEditText()).getText().toString();
        mobileRegex = "[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobliePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobliePattern.matcher(value);
        if (value.isEmpty()) {
            details_userPhone.setError("Field cannot be empty");
            return false;
        } else if (!mobileMatcher.find()) {
            details_userPhone.setError("Invalid Phone Number");
            return false;
        } else if (value.length() != 10) {
            details_userPhone.setError("Phone Number Should Be 10 Digits Only");
            return false;
        } else if (!Patterns.PHONE.matcher(value).matches()) {
            details_userPhone.setError("Invalid Phone Number");
            return false;
        } else {
            details_userPhone.setError(null);
            return true;
        }

    }

    private void fn_addUserDetails() {
        Name = Objects.requireNonNull(details_userName.getEditText()).getText().toString();
        Mail = Objects.requireNonNull(details_userMail.getEditText()).getText().toString();
        Pwd = Objects.requireNonNull(details_userPwd.getEditText()).getText().toString();
        Phone = Objects.requireNonNull(details_userPhone.getEditText()).getText().toString();
        Dob = "DD/MM/YYYY";
        Gender = "Gender";
        user_details = new userDetails(Name, Mail, Pwd, Phone,Dob,Gender);
        reference = FirebaseDatabase.getInstance().getReference().child("UserDetails");
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Mail, Pwd).addOnCompleteListener(activity_Signup.this, task -> {
            if (task.isSuccessful()) {
                String uId = auth.getUid();
                reference.child(uId).setValue(user_details).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        startActivity(new Intent(activity_Signup.this, activity_Home.class));
                        Toast.makeText(activity_Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(activity_Signup.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity_Signup.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}