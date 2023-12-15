package com.example.idnp_sunshield.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idnp_sunshield.R;

public class Health extends Fragment {

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
        rv1.setAdapter(new AdapterDisease());

        return view;
    }

    // Adapter for the RecyclerView
    private class AdapterDisease extends RecyclerView.Adapter<AdapterDisease.AdapterDiseaseHolder> {

        @NonNull
        @Override
        public AdapterDiseaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item in the RecyclerView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new AdapterDiseaseHolder(view);
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterDiseaseHolder holder, int position) {
            // Bind data to each item in the RecyclerView
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
                        UV_index.DetailFragment detailFragment = UV_index.DetailFragment.newInstance(names[getBindingAdapterPosition()], descriptions[getBindingAdapterPosition()], photos[getBindingAdapterPosition()]);
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
                iv1.setImageResource(photos[position]);
                tv1.setText(names[position]);
                tv2.setText(descriptions[position]);
            }
        }
    }
}
