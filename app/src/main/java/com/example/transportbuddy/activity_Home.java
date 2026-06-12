package com.example.transportbuddy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavigationView main_menu;
    ActionBarDrawerToggle toggle;
    int itemId;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View view;
    Button btn_logoutConfirm, btn_logoutCancel;
    FirebaseAuth auth;
    Intent intent;
    String userId, displayName;
    TextView textView_displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNavigation();
        drawerNavigation();
    }

    private void bottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home), (view, insets) -> {
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            if (bottomNavigationView != null) {
                bottomNavigationView.setPadding(0, 0, 0, bottom);
            }
            return insets;
        });
        fragment_Home fragmentHome = new fragment_Home();
        fragment_Tickets fragmentTickets = new fragment_Tickets();
        fragment_Places fragmentPlaces = new fragment_Places();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentHome).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentHome).commit();
            } else if (itemId == R.id.nav_ticket) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentTickets).commit();
            } else if (itemId == R.id.nav_places) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentPlaces).commit();
            }
            return true;
        });
    }

    private void drawerNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        main_menu = findViewById(R.id.drawer_navigation);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        main_menu.setNavigationItemSelectedListener(item -> {
            itemId = item.getItemId();
            if (itemId == R.id.profile) {
                startActivity(new Intent(activity_Home.this, activity_Profile.class));
            } else if (itemId == R.id.contactUs) {
                fn_contactUs();
            } else if (itemId == R.id.faq) {
                startActivity(new Intent(activity_Home.this, activity_Faq.class));
            } else if (itemId == R.id.shareApp) {
                fn_share();
            } else if (itemId == R.id.logout) {
                fn_logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        fn_displayName();
    }

    private void fn_displayName() {
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        Query getUserName = FirebaseDatabase.getInstance().getReference("UserDetails").orderByChild(userId);
        getUserName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                displayName = snapshot.child(userId).child("userName").getValue(String.class);
                View headerView = main_menu.getHeaderView(0);
                textView_displayName = headerView.findViewById(R.id.displayName);
                textView_displayName.setText(displayName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_Home.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fn_contactUs() {
        builder = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.layout_contact_us, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void fn_logout() {
        builder = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.layout_logout, null);
        builder.setView(view);
        dialog = builder.create();
        btn_logoutCancel = view.findViewById(R.id.btn_logoutCancel);
        btn_logoutCancel.setOnClickListener(v -> dialog.dismiss());
        btn_logoutConfirm = view.findViewById(R.id.btn_logoutConfirm);
        btn_logoutConfirm.setOnClickListener(v -> {
            auth = FirebaseAuth.getInstance();
            intent = new Intent(activity_Home.this, activity_Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            auth.signOut();
            startActivity(intent);
            Toast.makeText(activity_Home.this, "Logged Out SuccessFul", Toast.LENGTH_LONG).show();
            finish();
        });
        dialog.show();
    }

    private void fn_share() {
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Check Out This New Ticket Booking App\nTransport Buddy");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

}