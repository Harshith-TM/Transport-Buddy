package com.example.transportbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_Profile extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    String userId, str_userName, str_userMail, str_old_userMail, str_userPhone, str_userDob, str_userGender;
    boolean isEditing;
    TextView textUserName, textUserEmail, textUserPhone, textUserGender, textUserDob;
    TextInputLayout editUserName, editUserEmail, editUserPhone, editUserGender, editUserDob;
    Button btn_editDetails, btn_changePassword, btn_cancelChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textUserName = findViewById(R.id.profile_text_userName);
        textUserEmail = findViewById(R.id.profile_text_userMail);
        textUserPhone = findViewById(R.id.profile_text_userPhone);
        textUserGender = findViewById(R.id.profile_text_userGender);
        textUserDob = findViewById(R.id.profile_text_userDob);
        editUserName = findViewById(R.id.profile_edit_userName);
        editUserEmail = findViewById(R.id.profile_edit_userMail);
        editUserPhone = findViewById(R.id.profile_edit_userPhone);
        editUserGender = findViewById(R.id.profile_edit_userGender);
        editUserDob = findViewById(R.id.profile_edit_userDob);

        isEditing = false;
        btn_editDetails = findViewById(R.id.btn_editDetails);
        btn_editDetails.setOnClickListener(v -> fn_changeVisibility());
        btn_changePassword = findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(v -> startActivity(new Intent(activity_Profile.this, activity_Change_Password.class)));
        btn_cancelChanges = findViewById(R.id.btn_cancelChanges);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fn_getUserDetails();
    }

    private void fn_getUserDetails() {
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        Query getUserDetails = FirebaseDatabase.getInstance().getReference("UserDetails").orderByChild(userId);
        getUserDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                str_userName = snapshot.child(userId).child("userName").getValue(String.class);
                textUserName.setText(str_userName);
                str_userMail = snapshot.child(userId).child("userMail").getValue(String.class);
                str_old_userMail = str_userMail;
                textUserEmail.setText(str_userMail);
                str_userPhone = snapshot.child(userId).child("userPhone").getValue(String.class);
                textUserPhone.setText(str_userPhone);
                str_userDob = snapshot.child(userId).child("userDob").getValue(String.class);
                textUserDob.setText(str_userDob);
                str_userGender = snapshot.child(userId).child("userGender").getValue(String.class);
                textUserGender.setText(str_userGender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fn_changeVisibility() {
        isEditing = !isEditing;

        if (isEditing) {
            btn_editDetails.setText(R.string.txt_saveChanges);

            toggleVisibility(View.GONE, textUserName, textUserPhone, textUserEmail, textUserGender, textUserDob, btn_changePassword);
            toggleVisibility(View.VISIBLE, editUserName, editUserPhone, editUserEmail, editUserGender, editUserDob, btn_cancelChanges);

            Objects.requireNonNull(editUserName.getEditText()).setText(textUserName.getText());
            Objects.requireNonNull(editUserPhone.getEditText()).setText(textUserPhone.getText());
            Objects.requireNonNull(editUserEmail.getEditText()).setText(textUserEmail.getText());
            Objects.requireNonNull(editUserDob.getEditText()).setText(textUserDob.getText());
            Objects.requireNonNull(editUserGender.getEditText()).setText(textUserGender.getText());

        } else {
            btn_editDetails.setText(R.string.txt_editDetails);

            toggleVisibility(View.VISIBLE, textUserName, textUserPhone, textUserEmail, textUserGender, textUserDob, btn_changePassword);
            toggleVisibility(View.GONE, editUserName, editUserPhone, editUserEmail, editUserGender, editUserDob, btn_cancelChanges);

            textUserName.setText(Objects.requireNonNull(editUserName.getEditText()).getText());
            textUserEmail.setText(Objects.requireNonNull(editUserEmail.getEditText()).getText());
            textUserPhone.setText(Objects.requireNonNull(editUserPhone.getEditText()).getText());
            textUserDob.setText(Objects.requireNonNull(editUserDob.getEditText()).getText());
            textUserGender.setText(Objects.requireNonNull(editUserGender.getEditText()).getText());

            str_userName = (Objects.requireNonNull(editUserName.getEditText()).getText()).toString();
            str_userMail = (Objects.requireNonNull(editUserEmail.getEditText()).getText()).toString();
            str_userPhone = (Objects.requireNonNull(editUserPhone.getEditText()).getText()).toString();
            str_userDob = (Objects.requireNonNull(editUserDob.getEditText()).getText()).toString();
            str_userGender = (Objects.requireNonNull(editUserGender.getEditText()).getText()).toString();
            fn_updateUserDetails();
        }
        btn_cancelChanges.setOnClickListener(v -> {
            btn_editDetails.setText(R.string.txt_editDetails);
            isEditing = false;
            toggleVisibility(View.VISIBLE, textUserName, textUserPhone, textUserEmail, textUserGender, textUserDob, btn_changePassword);
            toggleVisibility(View.GONE, editUserName, editUserPhone, editUserEmail, editUserGender, editUserDob, btn_cancelChanges);
        });
    }

    private void toggleVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    private void fn_updateUserDetails() {
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("UserDetails");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!str_old_userMail.equals(str_userMail)) {
            if (user != null) {
                try {
                    user.verifyBeforeUpdateEmail(str_userMail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity_Profile.this, "Verify New Email Address", Toast.LENGTH_LONG).show();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("userMail", str_userMail);
                            reference.child(userId).updateChildren(updates);
                            /*.addOnCompleteListener(task1 -> {if (task1.isSuccessful()) {Toast.makeText(activity_Profile.this, "Email Updated Successfully", Toast.LENGTH_SHORT).show();} else {Toast.makeText(activity_Profile.this, "Profile Update was Unsuccessful", Toast.LENGTH_SHORT).show();}})*/
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthRecentLoginRequiredException e) {
                                Toast.makeText(activity_Profile.this, "Please re-authenticate to update email", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(activity_Profile.this, "This email is already in use by another account", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(activity_Profile.this, "Invalid email format", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(activity_Profile.this, "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(activity_Profile.this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity_Profile.this, "User Not Logged In", Toast.LENGTH_SHORT).show();
            }
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("userName", str_userName);
        updates.put("userPhone", str_userPhone);
        updates.put("userDob", str_userDob);
        updates.put("userGender", str_userGender);
        reference.child(userId).updateChildren(updates).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Toast.makeText(activity_Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity_Profile.this, "Profile update was unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}