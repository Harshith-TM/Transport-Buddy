package com.example.transportbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;


public class fragment_Ticket_Payment extends Fragment {

    View view;
    TextView paymentAmount;
    tickets_sharedViewModel ticketsSharedViewModel;
    MaterialRadioButton radio_btn_google_pay, radio_btn_phone_pe, radio_btn_pay_tm, radio_btn_other_upi, radio_btn_debit_card, radio_btn_credit_card;
    Button btn_confirm;
    String time, date, busNumber, fromPlace, toPlace, TCount, TAmount;//BTicketId;
    DatabaseReference reference;
    FirebaseAuth auth;
    busTicketDetails ticketDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_payment, container, false);

        radio_btn_google_pay = view.findViewById(R.id.radio_btn_google_pay);
        radio_btn_phone_pe = view.findViewById(R.id.radio_btn_phone_pe);
        radio_btn_pay_tm = view.findViewById(R.id.radio_btn_paytm);
        radio_btn_other_upi = view.findViewById(R.id.radio_btn_other_upi);
        radio_btn_debit_card = view.findViewById(R.id.radio_btn_debit_card);
        radio_btn_credit_card = view.findViewById(R.id.radio_btn_credit_card);

        View.OnClickListener radio_btn_listener = radio_btn_v -> {
            radio_btn_google_pay.setChecked(false);
            radio_btn_phone_pe.setChecked(false);
            radio_btn_pay_tm.setChecked(false);
            radio_btn_other_upi.setChecked(false);
            radio_btn_debit_card.setChecked(false);
            radio_btn_credit_card.setChecked(false);
            ((MaterialRadioButton) radio_btn_v).setChecked(true);
        };

        radio_btn_google_pay.setOnClickListener(radio_btn_listener);
        radio_btn_phone_pe.setOnClickListener(radio_btn_listener);
        radio_btn_pay_tm.setOnClickListener(radio_btn_listener);
        radio_btn_other_upi.setOnClickListener(radio_btn_listener);
        radio_btn_debit_card.setOnClickListener(radio_btn_listener);
        radio_btn_credit_card.setOnClickListener(radio_btn_listener);

        paymentAmount = view.findViewById(R.id.paymentAmount);
        ticketsSharedViewModel = new ViewModelProvider(requireActivity()).get(tickets_sharedViewModel.class);
        ticketsSharedViewModel.getTotalAmount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                String str = "Rs:" + name;
                paymentAmount.setText(str);
            }
        });
        fn_getTicketDetails();

        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(view1 -> {
            String str = paymentAmount.getText().toString();

            if (!str.equals("Rs:000")) {
                if (fn_isChecked_rb()) {
                    fn_putTicketDetails();
                }
            } else {
                Toast.makeText(getContext(), "Enter Stops Name", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean fn_isChecked_rb() {
        if (radio_btn_google_pay.isChecked()) {
            Toast.makeText(getContext(), "Google Pay", Toast.LENGTH_SHORT).show();
            return true;
        } else if (radio_btn_phone_pe.isChecked()) {
            Toast.makeText(getContext(), "Phone Pe", Toast.LENGTH_SHORT).show();
            return true;
        } else if (radio_btn_pay_tm.isChecked()) {
            Toast.makeText(getContext(), "Paytm", Toast.LENGTH_SHORT).show();
            return true;
        } else if (radio_btn_other_upi.isChecked()) {
            Toast.makeText(getContext(), "Other UPI Apps", Toast.LENGTH_SHORT).show();
            return true;
        } else if (radio_btn_debit_card.isChecked()) {
            Toast.makeText(getContext(), "Debit Card", Toast.LENGTH_SHORT).show();
            return true;
        } else if (radio_btn_credit_card.isChecked()) {
            Toast.makeText(getContext(), "Credit Card", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getContext(), "Select Any One", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void fn_getTicketDetails() {
        ticketsSharedViewModel.getTicketTime().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                time = name;
            }
        });
        ticketsSharedViewModel.getTicketDate().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                date = name;
            }
        });
        ticketsSharedViewModel.getTicketBusNumber().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                busNumber = name;
            }
        });
        ticketsSharedViewModel.getTicketFrom().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                fromPlace = name;
            }
        });
        ticketsSharedViewModel.getTicketTo().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                toPlace = name;
            }
        });
        ticketsSharedViewModel.getTicketCount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                TCount = name;
            }
        });
        ticketsSharedViewModel.getTotalAmount().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                TAmount = name;
            }
        });
    }

    private void fn_putTicketDetails() {
        auth = FirebaseAuth.getInstance();
        String uId = auth.getUid();
        if (uId != null) {
            reference = FirebaseDatabase.getInstance().getReference().child("UserDetails").child(uId);
        }
        String BTicketId = fn_generateTicketId();
        String mode = "bus";
        ticketDetails = new busTicketDetails(BTicketId, time, date, busNumber, fromPlace, toPlace, TCount, TAmount,mode);
        reference.child("BookedTickets").child(BTicketId).setValue(ticketDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Uploaded");
                Intent intent = new Intent(getActivity(), activity_Timer.class);
                intent.putExtra("Booking", "1");
                intent.putExtra("Tick", BTicketId);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Log.d("Firebase", "Not Uploaded");
            }
        }).addOnFailureListener(task -> Log.d("FireBase", "Unknown Error"));
    }

    private String fn_generateTicketId() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int TId_LENGTH = 8;
        SecureRandom random = new SecureRandom();
        StringBuilder TId = new StringBuilder(TId_LENGTH);
        for (int i = 0; i < TId_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            TId.append(CHARACTERS.charAt(index));
        }
        return TId.toString();
    }

    /*
    private ActivityResultLauncher<Intent> upiPaymentLauncher;

    upiPaymentLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
    Intent data = result.getData();
    if (data != null) {
    String response = data.getStringExtra("response");
    Map<String, String> parsed = parseUpiResponse(response);
    handleUpiResponse(parsed);
    } else {
    Toast.makeText(requireContext(), "Payment canceled or failed", Toast.LENGTH_SHORT).show();
    }});

    googlePay.setOnClickListener(v -> payWithUpiApp("com.google.android.apps.nbu.paisa.user"));
    phonePe.setOnClickListener(v -> payWithUpiApp("com.phonepe.app"));
    payTm.setOnClickListener(v -> payWithUpiApp("net.one97.paytm"));

    private void payWithUpiApp(String packageName) {
        String upiId = "user@okbank";
        String name = "Transport Buddy";
        String note = "Test";
        String amount = "100.00";

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (packageName != null && !packageName.isEmpty()) {
            intent.setPackage(packageName);
        }

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            try {
                upiPaymentLauncher.launch(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "No UPI app found to handle this request", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to open UPI app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, String> parseUpiResponse(@Nullable String response) {
        Map<String, String> map = new HashMap<>();

        if (response == null || response.trim().isEmpty()) return map;

        String[] pairs = response.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length >= 2) {
                map.put(keyValue[0].toLowerCase(), keyValue[1]);
            } else if (keyValue.length == 1) {
                map.put(keyValue[0].toLowerCase(), "");
            }
        }
        return map;
    }

    private void handleUpiResponse(Map<String, String> data) {
        String status = data.get("status");
        String txnId = data.get("txnid");
        String txnRef = data.get("txnref");
        String approvalRef = data.get("approvalrefno");

        if ("success".equalsIgnoreCase(status)) {
            Toast.makeText(getContext(), "Payment Successful\nTxn ID: " + txnId, Toast.LENGTH_LONG).show();
        } else if ("failure".equalsIgnoreCase(status)) {
            Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Payment Cancelled or Unknown", Toast.LENGTH_SHORT).show();
        }
    }*/
}