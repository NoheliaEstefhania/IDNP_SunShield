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
import com.example.idnp_sunshield.databinding.FragmentForecastBinding;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Alerts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Alerts extends Fragment {

    FragmentAlertsBinding binding;

    public Alerts() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        data();
        return binding.getRoot();
    }

    private void data(){
        List<Illness> listaEnfermedades;
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,"dbPruebas"
        ).allowMainThreadQueries().build();
        byte[] photos = {(byte) R.drawable.img_disease01};

        // Cargar la imagen en un Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_disease01);

// Convertir el Bitmap a un array de bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

// Ahora puedes guardar byteArray en tu base de datos
        dataBase.getIllnessDAO().addIllness(new Illness("hola", "hola", byteArray));
        listaEnfermedades = dataBase.getIllnessDAO().getAllIllnesses();

        /*
        dataBase.getIllnessDAO().addIllness(new Illness("hola", "hola", photos));
        listaEnfermedades = dataBase.getIllnessDAO().getAllIllnesses();
        binding.alertTextView.setText(listaEnfermedades.get(0).getTitle());
        Bitmap bitmap = BitmapFactory.decodeByteArray(listaEnfermedades.get(0).getImage(), 0, listaEnfermedades.get(0).getImage().length);
        /*/
        binding.alertTextView.setText(listaEnfermedades.get(0).getTitle());
        binding.imageView.setImageBitmap(bitmap);
    }


}