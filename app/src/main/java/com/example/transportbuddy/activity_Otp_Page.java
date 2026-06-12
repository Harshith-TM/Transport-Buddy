package com.example.transportbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_Otp_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_op), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        click_functions();
    }

    private void click_functions() {
        Button btn_verify = findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(view -> startActivity(new Intent(activity_Otp_Page.this, activity_Home.class)));
        //Button btn_resendOpt = findViewById(R.id.btn_resendOtp);
    }
}