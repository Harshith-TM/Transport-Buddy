package com.example.transportbuddy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class fragment_Places extends Fragment {

    View view;
    RecyclerView rcViews_places;
    FirebaseFirestore firestore;
    List<places_elements> placesElements;
    places_Adapter placesAdapter;
    ProgressBar progressBar;
    SearchView searchView_places;
    EditText searchView_edittext;
    View searchView_Plate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);

        rcViews_places = view.findViewById(R.id.rcView_places);
        rcViews_places.setLayoutManager(new LinearLayoutManager(getContext()));
        placesElements = new ArrayList<>();
        placesAdapter = new places_Adapter(getContext(), placesElements);
        rcViews_places.setAdapter(placesAdapter);
        firestore = FirebaseFirestore.getInstance();

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        searchView_places = view.findViewById(R.id.searchBar_places);
        searchView_edittext = searchView_places.findViewById(androidx.appcompat.R.id.search_src_text);
        searchView_edittext.setBackground(null);
        searchView_Plate = searchView_places.findViewById(androidx.appcompat.R.id.search_plate);
        searchView_Plate.setBackground(null);

        fn_getPlacesInfo();
        fn_searchPlaces();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fn_getPlacesInfo() {
        firestore.collection("places").orderBy("place_name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        places_elements place = doc.toObject(places_elements.class);
                        placesElements.add(place);
                    }
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    placesAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void fn_searchPlaces() {
        searchView_places.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPlaces(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPlaces(newText);
                return true;
            }
        });
    }

    private void filterPlaces(String text) {
        List<places_elements> filteredList = new ArrayList<>();
        for (places_elements place_ele : placesElements) {
            if (place_ele.getPlace_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(place_ele);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            placesAdapter.updateList(filteredList);
        }
    }
}