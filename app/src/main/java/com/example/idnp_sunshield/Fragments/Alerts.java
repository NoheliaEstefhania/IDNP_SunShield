package com.example.idnp_sunshield.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Illness;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.FragmentAlertsBinding;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Alerts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Alerts extends Fragment {

    FragmentAlertsBinding binding;

    // Default constructor
    public Alerts() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the Alerts fragment
    public static Alerts newInstance(String param1, String param2) {
        Alerts fragment = new Alerts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        data();
        // Return the root view of the fragment
        return binding.getRoot();
    }

    // Method to load and display data in the fragment
    private void data(){
        // List to store illnesses retrieved from the database
        List<Illness> illnessList;

        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).allowMainThreadQueries().build();

        // Dummy data: byte array representing an image (replace with actual image data)
        byte[] photos = {(byte) R.drawable.img_disease01};

        // Load the image into a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_disease01);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Add an illness with the image to the database
        dataBase.getIllnessDAO().addIllness(new Illness("In process", "In process", byteArray));

        // Retrieve all illnesses from the database
        illnessList = dataBase.getIllnessDAO().getAllIllnesses();

        // Display the title of the first illness in the TextView
        binding.alertTextView.setText(illnessList.get(0).getTitle());

        // Display the image in the ImageView
        binding.imageView.setImageBitmap(bitmap);
    }
}
