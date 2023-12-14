package com.example.idnp_sunshield;

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

public class Health extends Fragment {
    String[] names = {"disease01", "disease02", "disease03", "disease04", "disease05"};
    String[] descriptions = {"disease01", "disease02", "disease03", "disease04", "disease05"};
    int[] photos = {R.drawable.img_disease01, R.drawable.img_disease03, R.drawable.img_disease01, R.drawable.img_disease03,R.drawable.img_disease01};

    RecyclerView rv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        rv1 = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv1.setLayoutManager(linearLayoutManager);
        rv1.setAdapter(new AdapterDisease());

        return view;
    }

    private class AdapterDisease extends RecyclerView.Adapter<AdapterDisease.AdapterDiseaseHolder> {
        @NonNull
        @Override
        public AdapterDiseaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new AdapterDiseaseHolder(view);
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterDiseaseHolder holder, int position) {
            holder.print(position);
        }

        private class AdapterDiseaseHolder extends RecyclerView.ViewHolder {
            TextView tv1, tv2;
            ImageView iv1;

            public AdapterDiseaseHolder(@NonNull View itemView) {
                super(itemView);
                iv1 = itemView.findViewById(R.id.imageView_disease);
                tv1 = itemView.findViewById(R.id.textView_name);
                tv2 = itemView.findViewById(R.id.textView_description);

                // Agrega un OnClickListener a tu ImageView
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crea una nueva instancia de tu fragmento de detalles
                        DetailFragment detailFragment = DetailFragment.newInstance(names[getBindingAdapterPosition()], descriptions[getBindingAdapterPosition()], photos[getBindingAdapterPosition()]);
                        // Reemplaza el fragmento actual con el nuevo fragmento de detalles
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, detailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                });
            }

            public void print(int position) {
                iv1.setImageResource(photos[position]);
                tv1.setText(names[position]);
                tv2.setText(descriptions[position]);
            }
        }

    }
}
