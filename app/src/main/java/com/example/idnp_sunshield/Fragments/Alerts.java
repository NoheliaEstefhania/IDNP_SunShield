package com.example.idnp_sunshield.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Illness;
import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.SharePreferences.LocationPreferences;
import com.example.idnp_sunshield.databinding.FragmentAlertsBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Alerts extends Fragment {

    FragmentAlertsBinding binding;

    // Default constructor
    public Alerts() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        // Call the data method to load and display information
        // Return the root view of the fragment
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("En el boton de alerts");
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


        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();
        List<Location> locationsList;
        locationsList = dataBase.getLocationDAO().getAllLocations();
        for (Location location : locationsList) {
            System.out.println("Valores de locationsList: Country: "+  location.getTitle() + " Latitud=" + location.getLatitude() +
                    ", Longitud=" + location.getLongitude() + ", State: " + location.getState());
        }
        // Set up the adapter
        AlertsAdapter adapter = new AlertsAdapter(locationsList, binding);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    public Location getActiveLocationFromAdapter() {
        if (binding != null && binding.recyclerView2 != null && binding.recyclerView2.getAdapter() instanceof AlertsAdapter) {
            AlertsAdapter adapter = (AlertsAdapter) binding.recyclerView2.getAdapter();
            return adapter.getActiveLocation();
        }
        return null;
    }

    public class AlertsAdapter  extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {
        private List<Location> locationList;
        FragmentAlertsBinding binding;
        private int activePosition = -1;
        private int lastActivePosition = -1;

        //private int selectedPosition = RecyclerView.NO_POSITION;


        public AlertsAdapter(List<Location> locationList, FragmentAlertsBinding binding) {
            this.locationList = locationList;
            this.binding = binding;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_locations_item, parent, false);
            System.out.println("VAlor View "+ view);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            System.out.println("Longitud recivida: " + getItemCount());
            Location item = locationList.get(position);
            holder.textView.setText(item.getTitle());
            holder.switchView.setChecked(item.getState());
            System.out.println("Item at position " + position + ": " + item.getTitle() + ", SwitchState: " + item.getState());
            holder.switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Update the switch state in the model
                item.setState(isChecked);

                // Uncheck all other switches
                uncheckOtherSwitches(position);
                ensureOneActive();

                activePosition = isChecked ? position : -1;
                Location activeLocation = getActiveLocation();
                if (activeLocation != null) {
                    System.out.println("Valor activo después del cambio: " + activeLocation.getTitle());
                } else {
                    System.out.println("NO hay valor activo después del cambio");
                }
            });

            if(getActiveLocationFromAdapter() != null) {
                System.out.println("valor acitivo " + getActiveLocationFromAdapter().getTitle());
                LocationPreferences locationPreferences = new LocationPreferences(requireContext());
                // Establecer la información de ubicación
                double latitude = getActiveLocationFromAdapter().getLatitude();  // Reemplaza con la latitud real
                double longitude = getActiveLocationFromAdapter().getLongitude();  // Reemplaza con la longitud real
                String title = getActiveLocationFromAdapter().getTitle();  // Reemplaza con el título real

                locationPreferences.saveLocation(longitude, latitude , title);
            }else
                System.out.println("VALOR NULO");

        }

        @Override
        public int getItemCount() {
            return locationList.size();
        }

        private void ensureOneActive() {
            boolean atLeastOneActive = false;

            for (Location location : locationList) {
                if (location.getState()) {
                    atLeastOneActive = true;
                    break;
                }
            }

            // Si no hay ninguno activo, restaura el último activo
            if (!atLeastOneActive && lastActivePosition != -1 && lastActivePosition < locationList.size()) {
                locationList.get(lastActivePosition).setState(true);
            }
        }
        private void uncheckOtherSwitches(int currentPosition) {
            try {
                for (int i = 0; i < locationList.size(); i++) {
                    if (i != currentPosition) {
                        locationList.get(i).setState(false);
                        notifyItemChanged(i);
                    }
                }
                // Guarda la posición del elemento activo
                activePosition = currentPosition;

                binding.recyclerView2.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(currentPosition);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Location getActiveLocation() {
            for (Location location : locationList) {
                if (location.getState()) {
                    return location;
                }
            }
            return null;
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Switch switchView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                switchView = itemView.findViewById(R.id.switchView);
            }
        }
    }


}
