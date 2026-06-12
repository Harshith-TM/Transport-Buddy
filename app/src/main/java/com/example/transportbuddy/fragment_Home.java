package com.example.transportbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;



public class fragment_Home extends Fragment {

    View view;
    TextView ticket_bmtc,ticket_metro,ticket_train;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ticket_bmtc = view.findViewById(R.id.ticket_bmtc);
        ticket_bmtc.setOnClickListener(v -> startActivity(new Intent(getActivity(), activity_Ticket_Booking.class)));
        ticket_metro = view.findViewById(R.id.ticket_metro);
        ticket_metro.setOnClickListener(v -> Toast.makeText(getContext(), "Available Soon", Toast.LENGTH_SHORT).show());
        ticket_train = view.findViewById(R.id.ticket_train);
        ticket_train.setOnClickListener(v -> Toast.makeText(getContext(), "Available Soon", Toast.LENGTH_SHORT).show());
        return view;
    }
}