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
    /*private void data(){
        // List to store illnesses retrieved from the database
        List<Illness> illnessList;

        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();


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
    }*/
    /*private void data() {
        // List to store illnesses retrieved from the database
        List<Illness> illnessList;

        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();

        // Obtén el ID del recurso de la imagen desde el directorio drawable
        int drawableResourceId = R.drawable.img_disease01;

        // Crea una Illness con la imagen del directorio drawable
        Illness newIllness = new Illness("In process", "In process", getDrawableAsByteArray(drawableResourceId));

        // Añade la Illness a la base de datos
        dataBase.getIllnessDAO().addIllness(newIllness);

        // Recupera todas las enfermedades de la base de datos
        illnessList = dataBase.getIllnessDAO().getAllIllnesses();

        // Muestra el título de la primera enfermedad en el TextView
        binding.alertTextView.setText(illnessList.get(0).getTitle());

        // Muestra la imagen en el ImageView
        binding.imageView.setImage(illnessList.get(0).getImage());
    }*/

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
            binding.imageView.setImageBitmap(firstIllnessBitmap);
        } else {
            // Manejar el caso en el que no hay ninguna enfermedad en la base de datos
        }

        System.out.println("Eliminacion");
        for (int i = 0; i < illnessList.toArray().length; i++) {
            dataBase.getIllnessDAO().deleteIllness(illnessList.get(i));
        }
    }


    // Método para convertir un recurso drawable en un array de bytes
    private byte[] getDrawableAsByteArray(int drawableResourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
