package com.example.transportbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class fragment_Ticket_Selection extends Fragment {

    View view;
    TextView txtTimeDisplay, txtDateDisplay, txtBusNumber, txtTicketCount1, txtTicketCount2, txtTicketCost, txtTicketAmount, txtTotalAmount;
    tickets_sharedViewModel ticketsSharedViewModel;
    ImageButton btn_plusTicket, btn_minusTicket;
    Button btn_Proceed;
    LocalDateTime localDateTime;
    String str_currentTime, str_currentDate;
    int not, charges, ticketAmt, totalAmt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_selection, container, false);
        ticketsSharedViewModel = new ViewModelProvider(requireActivity()).get(tickets_sharedViewModel.class);
        localDateTime = LocalDateTime.now();
        str_currentTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        str_currentDate = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ticketsSharedViewModel.setTicketTime(str_currentTime);
        ticketsSharedViewModel.setTicketDate(str_currentDate);

        txtTimeDisplay = view.findViewById(R.id.txt_Time);
        txtDateDisplay = view.findViewById(R.id.txt_Date);
        txtBusNumber = view.findViewById(R.id.txt_BusNumber);
        txtTicketCount1 = view.findViewById(R.id.txt_TicketCount1);
        txtTicketCount2 = view.findViewById(R.id.txt_TicketCount2);
        txtTicketCost = view.findViewById(R.id.txt_TicketCost);
        txtTicketAmount = view.findViewById(R.id.txtTicketAmount);
        txtTotalAmount = view.findViewById(R.id.totalAmount);

        btn_plusTicket = view.findViewById(R.id.plusTicket);
        btn_minusTicket = view.findViewById(R.id.minusTicket);
        btn_Proceed = view.findViewById(R.id.btn_proceed);
        btn_Proceed.setOnClickListener(view -> {
            fragment_Ticket_Payment fragmentTicketPayment = new fragment_Ticket_Payment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTicketPayment).commit();
            ((activity_Ticket_Booking) requireActivity()).selectTab(1);
        });
        btn_plusTicket.setOnClickListener(v -> {
            not = Integer.parseInt(txtTicketCount1.getText().toString());
            charges = Integer.parseInt(txtTicketCost.getText().toString());
            if (not > 0 && not < 5) {
                not += 1;
                ticketAmt = charges * not;
                totalAmt = ticketAmt;
                txtTicketCount1.setText(String.valueOf(not));
                txtTicketCount2.setText(String.valueOf(not));
                ticketsSharedViewModel.setTicketCount(String.valueOf(not));
                ticketsSharedViewModel.setTicketAmount(String.valueOf(ticketAmt));
                ticketsSharedViewModel.setTotalAmount(String.valueOf(totalAmt));
            } else {
                Toast.makeText(getContext(), "Cannot exceed more than 5", Toast.LENGTH_SHORT).show();
            }
        });
        btn_minusTicket.setOnClickListener(v -> {
            not = Integer.parseInt(txtTicketCount1.getText().toString());
            charges = Integer.parseInt(txtTicketCost.getText().toString());
            if (not > 1) {
                not -= 1;
                ticketAmt = charges * not;
                totalAmt = ticketAmt;
                txtTicketCount1.setText(String.valueOf(not));
                txtTicketCount2.setText(String.valueOf(not));
                ticketsSharedViewModel.setTicketCount(String.valueOf(not));
                ticketsSharedViewModel.setTicketAmount(String.valueOf(ticketAmt));
                ticketsSharedViewModel.setTotalAmount(String.valueOf(totalAmt));
            } else {
                Toast.makeText(getContext(), "Cannot be less than 1", Toast.LENGTH_SHORT).show();
            }
        });
        ticketsSharedViewModel.setTicketCount(String.valueOf(1));
        fn_getTicketDetails();

        return view;
    }

    private void fn_getTicketDetails() {
        ticketsSharedViewModel.getTicketTime().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                txtTimeDisplay.setText(name);
            }
        });
        ticketsSharedViewModel.getTicketDate().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                txtDateDisplay.setText(name);
            }
        });
        ticketsSharedViewModel.getTicketBusNumber().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                txtBusNumber.setText(name);
            }
        });
        ticketsSharedViewModel.getTicketCost().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                txtTicketCost.setText(name);
            }
        });
        ticketsSharedViewModel.getTicketCount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                txtTicketCount1.setText(name);
                txtTicketCount2.setText(name);
            }
        });
        ticketsSharedViewModel.getTicketAmount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                String str = "Rs:"+name;
                txtTicketAmount.setText(str);
            }
        });
        ticketsSharedViewModel.getTotalAmount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                String str = "Rs:"+name;
                txtTotalAmount.setText(str);
            }
        });
    }
}