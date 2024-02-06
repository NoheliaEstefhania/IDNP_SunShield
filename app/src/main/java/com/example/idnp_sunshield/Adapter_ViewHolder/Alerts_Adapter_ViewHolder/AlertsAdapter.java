package com.example.idnp_sunshield.Adapter_ViewHolder.Alerts_Adapter_ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.SharePreferences.LocationPreferences;
import com.example.idnp_sunshield.databinding.FragmentAlertsBinding;

import java.util.List;

public class AlertsAdapter  extends RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder> {
    private List<Location> locationList;
    FragmentAlertsBinding binding;
    private int activePosition = -1;
    private int lastActivePosition = -1;
    Fragment fragment;
    private Context mContext;
    public AlertsAdapter(List<Location> locationList, FragmentAlertsBinding binding, Fragment fragment,Context mContext ) {
        this.locationList = locationList;
        this.binding = binding;
        this.fragment = fragment;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_locations_item, parent, false);
        System.out.println("VAlor View "+ view);
        return new AlertsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsViewHolder holder, int position) {
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

                DataBase dataBase = Room.databaseBuilder(
                        fragment.getActivity().getApplicationContext(),
                        DataBase.class,
                        "dbPruebas"
                ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();

                activeLocation.setState(true);
                dataBase.getLocationDAO().updateLocation(activeLocation);
            } else {
                System.out.println("NO hay valor activo después del cambio");
            }
        });

        if(getActiveLocationFromAdapter() != null) {
            System.out.println("valor acitivo " + getActiveLocationFromAdapter().getTitle());
            LocationPreferences locationPreferences = new LocationPreferences(mContext);
            // Establecer la información de ubicación
            double latitude = getActiveLocationFromAdapter().getLatitude();  // Reemplaza con la latitud real
            double longitude = getActiveLocationFromAdapter().getLongitude();  // Reemplaza con la longitud real
            String title = getActiveLocationFromAdapter().getTitle();  // Reemplaza con el título real

            locationPreferences.saveLocation(longitude, latitude , title);
        }else
            System.out.println("VALOR NULO");

    }
    public Location getActiveLocationFromAdapter() {
        if (binding != null && binding.recyclerView2 != null && binding.recyclerView2.getAdapter() instanceof AlertsAdapter) {
            AlertsAdapter adapter = (AlertsAdapter) binding.recyclerView2.getAdapter();
            return adapter.getActiveLocation();
        }
        return null;
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
                    DataBase dataBase = Room.databaseBuilder(
                            fragment.getActivity().getApplicationContext(),
                            DataBase.class,
                            "dbPruebas"
                    ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();

                    locationList.get(i).setState(false);
                    dataBase.getLocationDAO().updateLocation(locationList.get(i));
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
    public class AlertsViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Switch switchView;

        public AlertsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            switchView = itemView.findViewById(R.id.switchView);
        }
    }
}
