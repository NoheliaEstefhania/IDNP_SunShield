package com.example.idnp_sunshield.Fragments;

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
        dataBase.getIllnessDAO().addIllness(new Illness("hola", "hola", photos));
        listaEnfermedades = dataBase.getIllnessDAO().getAllIllnesses();
        binding.alertTextView.setText(listaEnfermedades.get(0).getTitle());
    }
}