package com.example.transportbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_Login extends AppCompatActivity {

    TextInputLayout login_userMail, login_userPwd;
    Button btn_signIn, btn_createNow, btn_forgotPassword;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ConnectivityManager connectivityManager;
    ConnectivityManager.NetworkCallback networkCallback;
    AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fn_signIn_User();
        registerNetworkCallback();
    }

    private void fn_signIn_User() {
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signIn.setOnClickListener(view -> {
            if (!validateEmail() | !validatePassword()) {
                Toast.makeText(activity_Login.this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                validateUser();
            }
        });
        btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
        btn_forgotPassword.setOnClickListener(view -> startActivity(new Intent(activity_Login.this, activity_Forgot_Password.class)));
        btn_createNow = findViewById(R.id.btn_createNow);
        btn_createNow.setOnClickListener(view -> startActivity(new Intent(activity_Login.this, activity_Signup.class)));
        login_userMail = findViewById(R.id.login_userMail);
        login_userPwd = findViewById(R.id.login_userPwd);
    }

    private boolean validateEmail() {
        String value = Objects.requireNonNull(login_userMail.getEditText()).getText().toString();
        if (value.isEmpty()) {
            login_userMail.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            login_userMail.setError("Invalid E-mail address");
            return false;
        } else {
            login_userMail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String value = Objects.requireNonNull(login_userPwd.getEditText()).getText().toString();
        if (value.isEmpty()) {
            login_userPwd.setError("Field cannot be empty");
            return false;
        } else if (value.length() <= 8) {
            login_userPwd.setError("Length Should more than 8 characters");
            return false;
        } else {
            login_userPwd.setError(null);
            return true;
        }
    }

    private void validateUser() {
        String Mail = Objects.requireNonNull(login_userMail.getEditText()).getText().toString();
        String Pwd = Objects.requireNonNull(login_userPwd.getEditText()).getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(Mail, Pwd).addOnCompleteListener(activity_Login.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activity_Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity_Login.this, activity_Home.class));
                finish();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    login_userMail.setError("Invalid Mail Id");
                    login_userPwd.setError("Invalid Password");
                    Toast.makeText(activity_Login.this, "Invalid Mail Id Or Password", Toast.LENGTH_LONG).show();
                } catch (FirebaseAuthInvalidUserException e) {
                    login_userMail.setError("Mail Id Does Not Exists");
                    Toast.makeText(activity_Login.this, "Mail Id Does Not Exists Or No Longer Valid. Please Register", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(activity_Login.this, "No Internet Available! Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (isNetworkConnected()) {
            if (auth.getCurrentUser() != null) {
                startActivity(new Intent(activity_Login.this, activity_Home.class));
                Toast.makeText(activity_Login.this, "Welcome Back", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            fn_showNetworkError();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkCallback();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities =
                connectivityManager.getNetworkCapabilities(network);

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public void fn_showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.layout_no_internet, null);
        builder.setView(view);
        noInternetDialog = builder.create();
        builder.setCancelable(false);
        noInternetDialog.setCanceledOnTouchOutside(false);
        noInternetDialog.setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        noInternetDialog.show();
    }

    private void registerNetworkCallback() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                runOnUiThread(() -> {
                    Log.d("Internet", "Internet Connected");
                    if (noInternetDialog != null && noInternetDialog.isShowing()) {
                        noInternetDialog.dismiss();
                    }
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Internet Lost", Toast.LENGTH_SHORT).show();
                    fn_showNetworkError();
                });
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void unregisterNetworkCallback() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}