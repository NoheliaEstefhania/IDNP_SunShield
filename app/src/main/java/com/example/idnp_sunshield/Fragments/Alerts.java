package com.example.idnp_sunshield.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Adapter_ViewHolder.Alerts_Adapter_ViewHolder.AlertsAdapter;
import com.example.idnp_sunshield.Entity.DataBase;

import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.FragmentAlertsBinding;

import java.util.List;

public class Alerts extends Fragment {

    FragmentAlertsBinding binding;

    // Default constructor
    // This constructor initializes the Alerts fragment.
    public Alerts() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Method to create and return the view of the fragment
    // This method inflates the layout for the Alerts fragment,
    // sets up UI elements, and fetches data to display.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        // Call the data method to load and display information
        // Return the root view of the fragment

        // Set up onClickListener for a button to navigate to another fragment
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("En el boton de alerts");
                // Navigate to Locations fragment when the button is clicked
                Locations locations_fragment = new Locations();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_alerts, locations_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        // Set up RecyclerView
        RecyclerView recyclerView = binding.recyclerView2;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        // Initialize database and fetch list of locations
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();
        List<Location> locationsList = dataBase.getLocationDAO().getAllLocations();
        // Print values of locationsList for debugging purposes
        for (Location location : locationsList) {
            System.out.println("Valores de locationsList: Country: "+  location.getTitle() + " Latitud=" + location.getLatitude() +
                    ", Longitud=" + location.getLongitude() + ", State: " + location.getState());
        }
        // Set up the adapter
        Context context = getContext();
        AlertsAdapter adapter = new AlertsAdapter(locationsList, binding, getCurrentActivity(),getContext());
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    // Method to get the current activity fragment
    // This method returns the current activity fragment.
    public Fragment getCurrentActivity() {
        return this;
    }


}
