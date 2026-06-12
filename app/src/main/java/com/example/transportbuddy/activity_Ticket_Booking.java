package com.example.transportbuddy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class activity_Ticket_Booking extends AppCompatActivity {
    TabLayout tabLayout;
    fragment_Ticket_Selection fragmentTicketSelection;
    fragment_Ticket_Payment fragmentTicketPayment;
    EditText ticketFrom, ticketTo;
    String str_ticketFrom, str_ticketTo;
    String str_busNumbers;
    int cost;
    tickets_sharedViewModel ticketsSharedViewModel;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ticket_booking), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();

        fn_ticketProcess();
    }

    public void fn_ticketProcess() {
        tabLayout = findViewById(R.id.nav_ticket_booking);
        fragmentTicketSelection = new fragment_Ticket_Selection();
        fragmentTicketPayment = new fragment_Ticket_Payment();
        ticketFrom = findViewById(R.id.placeFrom);
        ticketTo = findViewById(R.id.placeTo);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTicketSelection).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ticketFrom.setEnabled(true);
                        ticketTo.setEnabled(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTicketSelection).commit();
                        break;
                    case 1:
                        ticketFrom.setEnabled(false);
                        ticketTo.setEnabled(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTicketPayment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ticketTo.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                }
                textView.clearFocus();

                str_ticketFrom = ticketFrom.getText().toString();
                str_ticketTo = ticketTo.getText().toString();
                if (!str_ticketFrom.isEmpty() && !str_ticketTo.isEmpty()) {
                    fn_fetchTicketData(str_ticketFrom, str_ticketTo);
                } else {
                    Toast.makeText(activity_Ticket_Booking.this, "Enter Stop Name's", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            return false;
        });
    }

    public void fn_fetchTicketData(String fromStop, String toStop) {
        ticketsSharedViewModel = new ViewModelProvider(this).get(tickets_sharedViewModel.class);

        firestore.collection("bmtc_routes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> stops = (Map<String, Object>) document.get("stops");

                            if (stops != null && stops.containsKey(fromStop) && stops.containsKey(toStop)) {
                                str_busNumbers = document.getString("bus_number");
                                Double distance_fromPlace = (Double) stops.get(fromStop);
                                Double distance_toPlace = (Double) stops.get(toStop);
                                Log.d("Stops", "busNum: " + str_busNumbers);

                                if (distance_fromPlace != null && distance_toPlace != null && distance_fromPlace < distance_toPlace) {
                                    double distance = distance_toPlace - distance_fromPlace;
                                    if (distance <= 2) {
                                        cost = 6;
                                    } else if (distance <= 4) {
                                        cost = 12;
                                    } else if (distance <= 6) {
                                        cost = 18;
                                    } else if (distance <= 10) {
                                        cost = 23;
                                    } else if (distance <= 14) {
                                        cost = 24;
                                    } else if (distance <= 20) {
                                        cost = 28;
                                    } else if (distance <= 40) {
                                        cost = 30;
                                    } else if (distance > 42) {
                                        cost = 32;
                                    }
                                }
                                ticketsSharedViewModel.setTicketFrom(str_ticketFrom);
                                ticketsSharedViewModel.setTicketTo(str_ticketTo);
                                ticketsSharedViewModel.setTicketBusNumber(str_busNumbers);
                                ticketsSharedViewModel.setTicketCost(String.valueOf(cost));
                                ticketsSharedViewModel.setTicketAmount(String.valueOf(cost));
                                ticketsSharedViewModel.setTotalAmount(String.valueOf(cost));
                            } else {
                                Toast.makeText(this, "Invalid Stops", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.d("FireStore", "Unknown Error");
                        Toast.makeText(activity_Ticket_Booking.this, "Error Fetching Details", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(activity_Ticket_Booking.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void selectTab(int index) {
        if (index == 1) {
            ticketFrom.setEnabled(false);
            ticketTo.setEnabled(false);
        } else {
            ticketFrom.setEnabled(true);
            ticketTo.setEnabled(true);
        }
        tabLayout.selectTab(tabLayout.getTabAt(index));
    }
}