package com.example.idnp_sunshield.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.idnp_sunshield.R;

import java.io.ByteArrayOutputStream;

public class DetailFragment extends Fragment {

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
    // Factory method to create a new instance of DetailFragment with parameters
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

    // Factory method to create a new instance of DetailFragment with parameters
    public static DetailFragment newInstance(String name, String description, int imageResource) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        // Convert image resource to byte array
        args.putByteArray(ARG_IMAGE_RESOURCE, convertImageResourceToByteArray(imageResource));
        fragment.setArguments(args);
        return fragment;
    }

    // Method to convert image resource to byte array
    private static byte[] convertImageResourceToByteArray(int imageResource) {
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), imageResource);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
