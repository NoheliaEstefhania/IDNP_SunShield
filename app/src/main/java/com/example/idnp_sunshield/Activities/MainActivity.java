package com.example.idnp_sunshield.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.example.idnp_sunshield.Fragments.Alerts;
import com.example.idnp_sunshield.Fragments.Forecast;
import com.example.idnp_sunshield.Fragments.Health;
import com.example.idnp_sunshield.Fragments.UV_index;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new UV_index());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.uv_index) {
                replaceFragment(new UV_index());
            } else if (item.getItemId() == R.id.forecast) {
                replaceFragment(new Forecast());
            } else if (item.getItemId() == R.id.health) {
                replaceFragment(new Health());
            } else if (item.getItemId() == R.id.alerts) {
                replaceFragment(new Alerts());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}