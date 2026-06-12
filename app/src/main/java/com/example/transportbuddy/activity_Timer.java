package com.example.transportbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class activity_Timer extends AppCompatActivity {
    TextView timerText;
    ProgressBar circleProgress;
    CountDownTimer countDownTimer;
    LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_timer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fn_timerCount();
    }

    private void fn_timerCount() {
        animationView = findViewById(R.id.animationView);
        timerText = findViewById(R.id.timerText);
        circleProgress = findViewById(R.id.circleProgress);
//        firstView = getIntent().getStringExtra("Booking");
//        TicketId = getIntent().getStringExtra("Tick");

        int totalTime = 30;
        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                int progressPercent = 100 - ((secondsRemaining * 100) / totalTime);

                int minutes = secondsRemaining / 60;
                int seconds = secondsRemaining % 60;
                if (seconds == 10) {
                    countDownTimer.cancel();
                    timerText.setVisibility(View.GONE);
                    circleProgress.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        String firstView = getIntent().getStringExtra("Booking");
                        String TicketId = getIntent().getStringExtra("Tick");
                        Intent intent = new Intent(activity_Timer.this, activity_Bus_Ticket.class);
                        intent.putExtra("FirstView", firstView);
                        intent.putExtra("Ticket", TicketId);
                        startActivity(intent);
                        finish();
                    }, 4000);
                }
                timerText.setText(String.format("%02d:%02d", minutes, seconds));
                circleProgress.setProgress(progressPercent);
            }

            @Override
            public void onFinish() {
                timerText.setText(R.string.txt_time1);
                circleProgress.setProgress(100);
                Toast.makeText(activity_Timer.this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}