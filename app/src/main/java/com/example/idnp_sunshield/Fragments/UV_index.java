package com.example.idnp_sunshield.Fragments;

import static android.icu.text.MessagePattern.Part.Type.ARG_NAME;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.MausanData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.FragmentUVIndexBinding;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UV_index extends Fragment {
    FragmentUVIndexBinding binding;
    public UV_index() {
        // Required empty public constructor
    }
    public static UV_index newInstance(String param1, String param2) {
        UV_index fragment = new UV_index();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentUVIndexBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        fetchWeather();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUVIndexBinding.inflate(inflater, container, false);
        fetchWeather();
        return binding.getRoot();
    }

    public void fetchWeather (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("retrofit: " + retrofit);
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        Call<MausanData> call = interfaceApi.getData(-16.39889,-71.535,"hourly,daily","c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<MausanData>() {
            @Override
            public void onResponse(Call<MausanData> call, Response<MausanData> response) {
                if (response.isSuccessful()){
                    MausanData mausanData = response.body();
                    main to = mausanData.getMain();
                    current tc = mausanData.getCurrent();
                    System.out.println("Estoy en la respuesta");
                    binding.uvIndexNumber.setText(String.valueOf(tc.getUvi()));
                    HashMap<String, String> recomendaciones = new HashMap<>();
                    recomendaciones.put("Bajo", "En días de sol, es recomendable usar gafas de sol. Si tu piel es sensible, no olvides el protector solar SPF 30+. Presta atención a las superficies que reflejan los rayos UV.");
                    recomendaciones.put("Moderado", "Es mejor buscar sombra alrededor del mediodía. No olvides tu ropa protectora, sombrero y gafas de sol. Recuerda reaplicar el protector solar SPF 30+ cada dos horas.");
                    recomendaciones.put("Alto", "Intenta limitar tu exposición al sol entre las 10 a. m y las 4 p. m. Busca sombra, viste ropa protectora, usa sombrero y gafas de sol. No olvides reaplicar el protector solar cada dos horas.");
                    recomendaciones.put("Muy Alto", "Es aconsejable minimizar la exposición al sol entre las 10 a. m. y las 4 p. m. Busca sombra, viste ropa protectora, usa sombrero y gafas de sol. Recuerda reaplicar el protector solar cada dos horas.");
                    recomendaciones.put("Extremo", "Evita la exposición al sol entre las 10 a. m. y las 4 p. m. Busca sombra, viste ropa protectora, usa sombrero y gafas de sol. Recuerda reaplicar el protector solar cada dos horas.");

                    String grado;
                    if(tc.getUvi() <= 2) grado = "Bajo";
                    else if(3 <= tc.getUvi() && tc.getUvi() <= 5) grado = "Moderado";
                    else if(6 <= tc.getUvi() && tc.getUvi() <= 7) grado = "Alto";
                    else if(8 <= tc.getUvi() && tc.getUvi() <= 10) grado = "Muy Alto";
                    else grado = "Extremo";

                    binding.uvGrade.setText(grado);
                    binding.uvRecomendation.setText(recomendaciones.get(grado));
                    binding.uvDate.setText(date(tc.getDt()));
                }
            }

            @Override
            public void onFailure(Call<MausanData> call, Throwable t) {
                binding.uvIndexNumber.setText("no rpta");
            }
        });
    }
    private String date(long timestamp){
        //long timestamp = 1684929490L;
        java.util.Date time=new java.util.Date((long)timestamp*1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        String formattedDate = sdf.format(time);
        System.out.println("supuesta fecha: " + formattedDate);
        return formattedDate;
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link DetailFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class DetailFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";
        private static final String ARG_DESCRIPTION = "aaaaa";
        private static final String ARG_IMAGE_RESOURCE = "";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public DetailFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static DetailFragment newInstance(String param1, String param2) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Infla el layout para este fragmento
            View view = inflater.inflate(R.layout.fragment_detail, container, false);

            // Recupera los datos del Bundle
            String name = getArguments().getString(String.valueOf(ARG_NAME));
            String description = getArguments().getString(ARG_DESCRIPTION);
            int imageResource = getArguments().getInt(ARG_IMAGE_RESOURCE);

            // Encuentra las vistas en tu layout
            TextView textViewTitle = view.findViewById(R.id.textView_item_title);
            TextView textViewDescription = view.findViewById(R.id.textView_item);
            ImageView imageView = view.findViewById(R.id.imageView_item);

            // Establece los datos en tus vistas
            textViewTitle.setText(name);
            textViewDescription.setText(description);
            imageView.setImageResource(imageResource);

            return view;
        }


        public static DetailFragment newInstance(String name, String description, int imageResource) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(String.valueOf(ARG_NAME), name);
            args.putString(ARG_DESCRIPTION, description);
            args.putInt(ARG_IMAGE_RESOURCE, imageResource);
            fragment.setArguments(args);
            return fragment;
        }

    }
}