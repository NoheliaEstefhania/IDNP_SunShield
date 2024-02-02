package com.example.idnp_sunshield.Fragments;

import static android.icu.text.MessagePattern.Part.Type.ARG_NAME;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.Singleton.LocationSingleton;
import com.example.idnp_sunshield.databinding.FragmentUVIndexBinding;

import java.io.ByteArrayOutputStream;
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
        LocationSingleton locationSingleton = LocationSingleton.getInstance();
        Location ubicacion = locationSingleton.getLocation();

        System.out.println("LOCALIZACION ubicacion: " + ubicacion.getTitle());
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



    public static class DetailFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_NAME = "param_name";
        private static final String ARG_DESCRIPTION = "param_description";
        private static final String ARG_IMAGE_RESOURCE = "param_image";

        private String name;
        private String description;
        private byte[] image;
        public DetailFragment() {
            // Required empty public constructor
        }

        // TODO: Rename and change types and number of parameters
        // En la clase DetailFragment de UV_index
        public static DetailFragment newInstance(String name, String description, byte[] image) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_NAME, name);
            args.putString(ARG_DESCRIPTION, description);
            args.putByteArray(ARG_IMAGE_RESOURCE, image);  // Utiliza putByteArray para pasar un array de bytes
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Retrieve arguments passed to the fragment
            if (getArguments() != null) {
                name = getArguments().getString(ARG_NAME);
                description = getArguments().getString(ARG_DESCRIPTION);
                image = getArguments().getByteArray(ARG_IMAGE_RESOURCE);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_detail, container, false);

            TextView textViewTitle = view.findViewById(R.id.textView_item_title);
            TextView textViewDescription = view.findViewById(R.id.textView_item_details);
            ImageView imageView = view.findViewById(R.id.imageView_item);

            textViewTitle.setText(name);
            textViewDescription.setText(description);

            // Convertir el array de bytes a un Bitmap y establecerlo en el ImageView
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);

            return view;
        }

        // Create a new instance of DetailFragment with parameters
        public static DetailFragment newInstance(String name, String description, int imageResource) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_NAME, name);
            args.putString(ARG_DESCRIPTION, description);
            // Puedes convertir el recurso de imagen a bytes si lo necesitas
            args.putByteArray(ARG_IMAGE_RESOURCE, convertImageResourceToByteArray(imageResource));
            fragment.setArguments(args);
            return fragment;
        }

        private static byte[] convertImageResourceToByteArray(int imageResource) {
            // Aquí puedes implementar la conversión de un recurso de imagen a un array de bytes
            // Por ejemplo, puedes usar BitmapFactory para decodificar el recurso a un Bitmap
            // y luego convertir el Bitmap a un array de bytes
            // Este código es solo un ejemplo y puedes ajustarlo según tus necesidades
            Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), imageResource);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

    }
}