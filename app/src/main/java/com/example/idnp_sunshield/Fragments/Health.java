package com.example.idnp_sunshield.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idnp_sunshield.Adapter_ViewHolder.Disease_Adapter_ViewHolder.AdapterDisease;
import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Illness;
import com.example.idnp_sunshield.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Health extends Fragment {

    RecyclerView rv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        // Initialize RecyclerView
        rv1 = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv1.setLayoutManager(linearLayoutManager);

        // Verify and add information to the database if necessary
        checkAndAddDataToDatabase();
        List<Illness> illnessList = getIllnessesFromDatabase();
        System.out.println("BEFORE onCreateView");
        for (Illness ill: illnessList) {
            System.out.println("Contenido ILLNESS: " + ill.getTitle());
        }
        rv1.setAdapter(new AdapterDisease(illnessList, getCurrentActivity()));
        return view;
    }

    // Method to retrieve illnesses from the database
    private List<Illness> getIllnessesFromDatabase() {
        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();

        // Retrieve all illnesses from the database
        return dataBase.getIllnessDAO().getAllIllnesses();
    }

    private void checkAndAddDataToDatabase() {
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();

        // Retrieve existing illnesses from the database
        List<Illness> existingIllnesses = dataBase.getIllnessDAO().getAllIllnesses();

        for (Illness ill: existingIllnesses) {
            System.out.println("Contenido ILLNESS: " + ill.getTitle());
        }
        System.out.println("valor de existingIllnesses.isEmpty() " + existingIllnesses.isEmpty());
        // Add data to the database if it's empty or new data is available
        addDataToDatabase(dataBase);
    }

    // Method to add sample data to the database
    private void addDataToDatabase(DataBase dataBase) {
        // Sample data for diseases
        String[] names = {"Skin Cancer", "Sunburn", "Cataracts", "Pterygium","Immunosuppression", "Age-Related Macular Degeneration (ARMD)"};
        String[] descriptions = {"UV radiation is a risk factor for several types of skin cancer.",
                "Sunburns are an acute response to excessive exposure to UV rays.",
                "It is estimated that 10% of cataract cases, an eye disease that can cause blindness.",
                "It is a fleshy growth that can cover part of the cornea.",
                "This is an eye disease that can worsen with exposure to UV radiation",
                "Overexposure to UV radiation can suppress the functioning of the body’s immune system."
        };
        int[] photos = {R.drawable.img_disease01, R.drawable.img_disease03, R.drawable.img_disease06, R.drawable.img_disease05, R.drawable.img_disease02, R.drawable.img_disease04};

        // Add information to the database for each illness if it's not already present
        for (int i = 0; i < names.length; i++) {
            // Verificar si la enfermedad ya existe en la base de datos
            List<Illness> existingIllnesses = dataBase.getIllnessDAO().getAllIllnesses();

            boolean illnessExists = false;
            for (Illness existingIllness : existingIllnesses) {
                System.out.println("HealthFragment "+ "Existing illness title: " + existingIllness.getTitle());
                System.out.println("HealthFragment "+ "New illness title: " + names[i]);
                if (existingIllness.getTitle().equals(names[i])) {
                    illnessExists = true;
                    break;
                }
            }

            if (!illnessExists) {
                // Convert the image resource to byte array
                byte[] imageByteArray = getByteArrayFromDrawableResource(photos[i]);

                // Add the illness to the database
                dataBase.getIllnessDAO().addIllness(new Illness(names[i], descriptions[i], imageByteArray));
            }
        }
    }

    // Method to convert drawable resource to byte array
    private byte[] getByteArrayFromDrawableResource(int drawableResourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Method to get the current activity fragment
    public Fragment getCurrentActivity() {
        return this;
    }
}


