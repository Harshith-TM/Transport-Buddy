package com.example.transportbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_Bus_Ticket extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth auth;
    ProgressBar BT_progressBar;
    ConstraintLayout BT_detailsContainer;
    TextView busTime, busDate, busNumber, busTicketId, busTicketCount, busFrom, busTo, busTicketAmount;
    String str_busTime, str_busDate, str_busNumber, str_busTicketId, str_busTicketCount, str_busFrom, str_busTo, str_busTicketAmount, TId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ticket), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        busTicketId = findViewById(R.id.txtView_ticketId);
        busTime = findViewById(R.id.txtView_booking_time1);
        busDate = findViewById(R.id.txtView_booking_date1);
        busNumber = findViewById(R.id.txtView_bus_number1);
        busTicketCount = findViewById(R.id.txtView_ticket1);
        busFrom = findViewById(R.id.txtView_place1);
        busTo = findViewById(R.id.txtView_place2);
        busTicketAmount = findViewById(R.id.txtView_amount1);
        BT_progressBar = findViewById(R.id.BT_progressBar);
        BT_progressBar.setVisibility(View.VISIBLE);
        BT_detailsContainer = findViewById(R.id.BT_detailsContainer);
        BT_detailsContainer.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            BT_detailsContainer.setVisibility(View.VISIBLE);
            BT_progressBar.setVisibility(View.GONE);
            String firstView = getIntent().getStringExtra("FirstView");
            if (firstView != null && firstView.equals("1")) {
                fn_TicketDetails();
            } else {
                fn_showTicketDetails();
            }
        }, 2000);

    }

    private void fn_TicketDetails() {
        auth = FirebaseAuth.getInstance();
        String UId = auth.getUid();
        TId = getIntent().getStringExtra("Ticket");
        reference = FirebaseDatabase.getInstance().getReference("UserDetails").child(UId).child("BookedTickets").child(TId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                str_busTicketId = snapshot.child("bt_Id").getValue(String.class);
                busTicketId.setText(str_busTicketId);
                str_busTime = snapshot.child("bt_time").getValue(String.class);
                busTime.setText(str_busTime);
                str_busDate = snapshot.child("bt_date").getValue(String.class);
                busDate.setText(str_busDate);
                str_busNumber = snapshot.child("bt_busNum").getValue(String.class);
                busNumber.setText(str_busNumber);
                str_busFrom = snapshot.child("bt_fromPlace").getValue(String.class);
                String capsFrom = capitalizeWord(str_busFrom);
                busFrom.setText(capsFrom);
                str_busTo = snapshot.child("bt_toPlace").getValue(String.class);
                String capsTo = capitalizeWord(str_busTo);
                busTo.setText(capsTo);
                str_busTicketCount = snapshot.child("bt_count").getValue(String.class);
                busTicketCount.setText(str_busTicketCount);
                str_busTicketAmount = snapshot.child("bt_amount").getValue(String.class);
                busTicketAmount.setText("Rs:" + str_busTicketAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Error: " + error);
                Toast.makeText(activity_Bus_Ticket.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fn_showTicketDetails(){
        Intent indent = getIntent();
        str_busTicketId = indent.getStringExtra("BTId");
        busTicketId.setText(str_busTicketId);
        str_busTime = indent.getStringExtra("BTTime");
        busTime.setText(str_busTime);
        str_busDate = indent.getStringExtra("BTDate");
        busDate.setText(str_busDate);
        str_busNumber = indent.getStringExtra("BTNumber");
        busNumber.setText(str_busNumber);
        str_busFrom = indent.getStringExtra("BTFrom");
        String capsFrom = capitalizeWord(str_busFrom);
        busFrom.setText(capsFrom);
        str_busTo = indent.getStringExtra("BTTo");
        String capsTo = capitalizeWord(str_busTo);
        busTo.setText(capsTo);
        str_busTicketCount = indent.getStringExtra("BTCount");
        busTicketCount.setText(str_busTicketCount);
        str_busTicketAmount = "Rs:"+indent.getStringExtra("BTFare");
        busTicketAmount.setText(str_busTicketAmount);

    }

    private String capitalizeWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return capitalized.toString().trim();
    }
}