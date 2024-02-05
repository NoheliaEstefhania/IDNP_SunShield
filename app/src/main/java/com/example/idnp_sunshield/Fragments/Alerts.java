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
        //data();
        // Return the root view of the fragment
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("En el boton de alerts");
                // Crear una instancia del nuevo fragmento
                Locations locations_fragment = new Locations();

                // Obtener el FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Iniciar una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual con el nuevo fragmento
                fragmentTransaction.replace(R.id.fragment_alerts, locations_fragment);

                // Agregar la transacción a la pila para que pueda ser retrocedida
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
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
                    ", Longitud=" + location.getLongitude());
        }
        // Set up the adapter
        AlertsAdapter adapter = new AlertsAdapter(locationsList);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void data() {
        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();

        // Dummy data: byte array representing an image (replace with actual image data)
        byte[] photos = {(byte) R.drawable.img_disease01};

        // Load the image into a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_disease02);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Add a new illness with the image to the database
        Illness newIllness = new Illness("New Illness", "Description", byteArray);
        dataBase.getIllnessDAO().addIllness(newIllness);

        // Retrieve all illnesses from the database
        List<Illness> illnessList = dataBase.getIllnessDAO().getAllIllnesses();

        // Verifica si hay alguna enfermedad en la lista
        if (!illnessList.isEmpty()) {
            // Recupera la primera enfermedad (puedes ajustar esto según tus necesidades)
            Illness firstIllness = illnessList.get(0);

            // Convierte el array de bytes de la imagen en un Bitmap
            Bitmap firstIllnessBitmap = BitmapFactory.decodeByteArray(firstIllness.getImage(), 0, firstIllness.getImage().length);

            // Muestra el título de la primera enfermedad en el TextView
            binding.alertTextView.setText(firstIllness.getTitle());

            // Muestra la imagen de la primera enfermedad en el ImageView
            //binding.imageView.setImageBitmap(firstIllnessBitmap);
        } else {
            // Manejar el caso en el que no hay ninguna enfermedad en la base de datos
        }

        System.out.println("Eliminacion");
        for (int i = 0; i < illnessList.toArray().length; i++) {
            dataBase.getIllnessDAO().deleteIllness(illnessList.get(i));
        }
    }


    public class AlertsAdapter  extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {
        private List<Location> locationList;
        FragmentAlertsBinding binding;

        //private int selectedPosition = RecyclerView.NO_POSITION;


        public AlertsAdapter (List<Location> locationList) {
            this.locationList = locationList;
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
            });
        }

        @Override
        public int getItemCount() {
            return locationList.size();
        }

        private void uncheckOtherSwitches(int currentPosition) {
            try {
                for (int i = 0; i < locationList.size(); i++) {
                    if (i != currentPosition) {
                        locationList.get(i).setState(false);
                        notifyItemChanged(i);
                    }
                }
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
