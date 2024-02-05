package com.example.idnp_sunshield.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
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

        // Verificar y agregar información a la base de datos si es necesario
        checkAndAddDataToDatabase();
        List<Illness> illnessList = getIllnessesFromDatabase();
        System.out.println("BEFORE onCreateView");
        for (Illness ill: illnessList) {
            System.out.println("Contenido ILLNESS: " + ill.getTitle());
        }
        rv1.setAdapter(new AdapterDisease(illnessList));
        return view;
    }

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

        // Verificar si ya hay información en la base de datos
        List<Illness> existingIllnesses = dataBase.getIllnessDAO().getAllIllnesses();

        for (Illness ill: existingIllnesses) {
            System.out.println("Contenido ILLNESS: " + ill.getTitle());
        }
        System.out.println("valor de existingIllnesses.isEmpty() " + existingIllnesses.isEmpty());
        /*if (existingIllnesses.isEmpty()) {
            // La base de datos está vacía, así que agregamos la información
            addDataToDatabase(dataBase);
        }*/
        addDataToDatabase(dataBase);
    }

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

        // Agregar información a la base de datos
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
                // Convertir el recurso de imagen a byte array
                byte[] imageByteArray = getByteArrayFromDrawableResource(photos[i]);

                // Agregar la enfermedad a la base de datos
                dataBase.getIllnessDAO().addIllness(new Illness(names[i], descriptions[i], imageByteArray));
            }
        }
    }

    private byte[] getByteArrayFromDrawableResource(int drawableResourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Adapter for the RecyclerView
    private class AdapterDisease extends RecyclerView.Adapter<AdapterDisease.AdapterDiseaseHolder> {

        List<Illness> illnessList;

        // Constructor que acepta una lista de enfermedades
        public AdapterDisease(List<Illness> illnessList) {
            this.illnessList = illnessList;
        }

        @NonNull
        @Override
        public AdapterDiseaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item in the RecyclerView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new AdapterDiseaseHolder(view);
        }

        @Override
        public int getItemCount() {
            return illnessList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterDiseaseHolder holder, int position) {
            // Bind data to each item in the RecyclerView
            System.out.println("AdapterDisease onBindViewHolder called for position: " + position);
            holder.bindData(position);
        }

        // ViewHolder for each item in the RecyclerView
        private class AdapterDiseaseHolder extends RecyclerView.ViewHolder {
            TextView tv1, tv2;
            ImageView iv1;

            public AdapterDiseaseHolder(@NonNull View itemView) {
                super(itemView);
                // Initialize views
                iv1 = itemView.findViewById(R.id.imageView_disease);
                tv1 = itemView.findViewById(R.id.textView_name);
                tv2 = itemView.findViewById(R.id.textView_description);

                // Add an OnClickListener to the ImageView
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a new instance of your detail fragment
                        UV_index.DetailFragment detailFragment = UV_index.DetailFragment.newInstance(
                                illnessList.get(getBindingAdapterPosition()).getTitle(),
                                illnessList.get(getBindingAdapterPosition()).getDescription(),
                                illnessList.get(getBindingAdapterPosition()).getImage()
                        );
                        // Replace the current fragment with the new detail fragment
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, detailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

            // Bind data to the views
            public void bindData(int position) {
                Illness illness = illnessList.get(position);
                System.out.println("BINDDATA ILLNESS: " + illness.getTitle());
                iv1.setImageBitmap(BitmapFactory.decodeByteArray(illnessList.get(position).getImage(), 0, illnessList.get(position).getImage().length));
                tv1.setText(illnessList.get(position).getTitle());
                tv2.setText(illnessList.get(position).getDescription());
            }
        }
    }
}


