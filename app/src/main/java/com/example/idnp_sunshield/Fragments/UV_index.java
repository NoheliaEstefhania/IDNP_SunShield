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
import com.example.idnp_sunshield.Models.UVData;
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
        // Initialize the binding object for the UVIndex fragment
        binding = FragmentUVIndexBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the binding object
        binding = FragmentUVIndexBinding.inflate(inflater, container, false);
        // Fetch weather data and update UI components
        fetchWeather();
        return binding.getRoot();
    }

    // Fetches weather data using Retrofit and OpenWeatherMap API
    public void fetchWeather() {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the InterfaceApi using the Retrofit instance
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        // Make an asynchronous call to get UV data from OpenWeatherMap API
        Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    // Retrieve UVData from the response
                    UVData uvData = response.body();
                    // Extract main and current data from UVData
                    main to = uvData.getMain();
                    current tc = uvData.getCurrent();

                    // Update UI components with UV index value
                    binding.uvIndexNumber.setText(String.valueOf(tc.getUvi()));

                    // UV index recommendations based on the UV index value
                    HashMap<String, String> recommendations = new HashMap<>();
                    recommendations.put("Low", "On sunny days, it's recommended to wear sunglasses. If your skin is sensitive, don't forget the SPF 30+ sunscreen. Pay attention to surfaces that reflect UV rays.");
                    recommendations.put("Moderate", "It's better to seek shade around noon. Don't forget your protective clothing, hat, and sunglasses. Remember to reapply the SPF 30+ sunscreen every two hours.");
                    recommendations.put("High", "Try to limit your sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Don't forget to reapply sunscreen every two hours.");
                    recommendations.put("Very High", "It's advisable to minimize sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Remember to reapply sunscreen every two hours.");
                    recommendations.put("Extreme", "Avoid sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Remember to reapply sunscreen every two hours.");

                    // Determine UV index grade
                    String grade;
                    if (tc.getUvi() <= 2) grade = "Low";
                    else if (3 <= tc.getUvi() && tc.getUvi() <= 5) grade = "Moderate";
                    else if (6 <= tc.getUvi() && tc.getUvi() <= 7) grade = "High";
                    else if (8 <= tc.getUvi() && tc.getUvi() <= 10) grade = "Very High";
                    else grade = "Extreme";

                    // Set UV index grade, recommendation, and date
                    binding.uvGrade.setText(grade);
                    binding.uvRecomendation.setText(recommendations.get(grade));
                    binding.uvDate.setText(date(tc.getDt()));
                }
            }

            @Override
            public void onFailure(Call<UVData> call, Throwable t) {
                // Handle the failure scenario
                binding.uvIndexNumber.setText("no answer");
            }
        });
    }
    // Format timestamp to a readable date
    private String date(long timestamp) {
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        return sdf.format(time);
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

        // TODO: Rename and change types and number of parameters
        /*public static DetailFragment newInstance(String param1, String param2) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }*/

        // En la clase DetailFragment de UV_index
        public static DetailFragment newInstance(String name, String description, byte[] image) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, name);
            args.putString(ARG_PARAM2, description);
            args.putByteArray(ARG_IMAGE_RESOURCE, image);  // Utiliza putByteArray para pasar un array de bytes
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Retrieve arguments passed to the fragment
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_detail, container, false);

            // Retrieve data from the Bundle
            String name = getArguments().getString(String.valueOf(ARG_NAME));
            String description = getArguments().getString(ARG_DESCRIPTION);
            int imageResource = getArguments().getInt(ARG_IMAGE_RESOURCE);

            // Find views in the layout
            TextView textViewTitle = view.findViewById(R.id.textView_item_title);
            TextView textViewDescription = view.findViewById(R.id.textView_item_details);
            ImageView imageView = view.findViewById(R.id.imageView_item);

            // Set data to the views
            textViewTitle.setText(name);
            textViewDescription.setText(description);
            imageView.setImageResource(imageResource);

            return view;
        }

        // Create a new instance of DetailFragment with parameters
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