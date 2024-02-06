package com.example.idnp_sunshield.Adapter_ViewHolder.Disease_Adapter_ViewHolder;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idnp_sunshield.Entity.Illness;
import com.example.idnp_sunshield.Fragments.UV_index;
import com.example.idnp_sunshield.R;

import java.util.List;

public class AdapterDisease extends RecyclerView.Adapter<AdapterDisease.AdapterDiseaseHolder> {

    List<Illness> illnessList;
    Fragment fragment;

    // Constructor que acepta una lista de enfermedades
    public AdapterDisease(List<Illness> illnessList,Fragment fragment ) {
        this.illnessList = illnessList;
        this.fragment = fragment;
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
    public class AdapterDiseaseHolder extends RecyclerView.ViewHolder {
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
                    FragmentTransaction transaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
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