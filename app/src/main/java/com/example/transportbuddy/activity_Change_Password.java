package com.example.transportbuddy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_Change_Password extends AppCompatActivity {

    String currentPwd, newPwd, confirmPwd,UId;
    TextInputLayout currentPassword, newPassword, confirmPassword;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_cp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        UId = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("UserDetails").child(UId);
        user = FirebaseAuth.getInstance().getCurrentUser();

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        Button changePwd = findViewById(R.id.changePwd);
        changePwd.setOnClickListener(v -> {
            fn_getUserPwd();
        });
    }

    private void fn_getUserPwd(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object o = snapshot.child("passWord").getValue();
                String pwd = String.valueOf(o);
                fn_updatePassword(pwd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_Change_Password.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fn_updatePassword(String userCurrentPwd) {
        currentPwd = Objects.requireNonNull(currentPassword.getEditText()).getText().toString();
        newPwd = Objects.requireNonNull(newPassword.getEditText()).getText().toString();
        confirmPwd = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString();
        if (user != null) {
            if (currentPwd.equals(userCurrentPwd))
            {
                user.updatePassword(newPwd).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("passWord", newPwd);
                        reference.updateChildren(updates);
                    } else {
                        Toast.makeText(this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}