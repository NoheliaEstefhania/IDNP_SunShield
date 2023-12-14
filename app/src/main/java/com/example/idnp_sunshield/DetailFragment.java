package com.example.idnp_sunshield;

import static android.icu.text.MessagePattern.Part.Type.ARG_NAME;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

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