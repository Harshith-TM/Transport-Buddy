package com.example.transportbuddy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_Tickets extends Fragment {

    View view;
    FirebaseAuth auth;
    DatabaseReference reference;
    RecyclerView rcViews_tickets;
    ProgressBar progressBar;
    List<busTicketDetails> ticketElements;
    tickets_Adapter ticketsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets, container, false);

        rcViews_tickets = view.findViewById(R.id.rcView_tickets);
        rcViews_tickets.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketElements = new ArrayList<>();
        ticketsAdapter = new tickets_Adapter(getContext(), ticketElements);
        rcViews_tickets.setAdapter(ticketsAdapter);

        progressBar = view.findViewById(R.id.ticketView_PBar);
        progressBar.setVisibility(View.VISIBLE);

        fn_getTicketDetails();

        return view;
    }

    private void fn_getTicketDetails() {
        auth = FirebaseAuth.getInstance();
        String UId = auth.getUid();
        if (UId != null) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails").child(UId).child("BookedTickets");
        }
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    busTicketDetails ticket = dataSnapshot.getValue(busTicketDetails.class);
                    ticketElements.add(ticket);
                }
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                ticketsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}