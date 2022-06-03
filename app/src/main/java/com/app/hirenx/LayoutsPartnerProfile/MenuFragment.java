package com.app.hirenx.LayoutsPartnerProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.hirenx.Adapters.MenuAdapter;
import com.app.hirenx.Authentication.RegistrationTypeActivity;
import com.app.hirenx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuFragment extends Fragment {

    private RecyclerView rvProfile;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager linearlayoutmanager;
    public static Context context;
    private Button btnLogOut;
    private FirebaseAuth mAuth;
    private String[] name = {"Help & Support", "Contact Us", "About Us", "Terms and Privacy Policy"
            , "App info", "Refer to a friend", "Rate us on playstore"};
    private int[] images = {R.drawable.help, R.drawable.mail, R.drawable.aboutus, R.drawable.policieslock,
            R.drawable.appinfo, R.drawable.refer, R.drawable.star};


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_menu, container, false);
        btnLogOut=view.findViewById(R.id.btn_logout);
        mAuth=FirebaseAuth.getInstance();
        rvProfile = view.findViewById(R.id.rv_view);
        rvProfile.setHasFixedSize(true);
        linearlayoutmanager = new LinearLayoutManager(getContext());
        rvProfile.setLayoutManager(linearlayoutmanager);
        adapter = new MenuAdapter(getContext(), name, images);
        rvProfile.setAdapter(adapter);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                Intent moveToRegistrationType=new Intent(getContext(), RegistrationTypeActivity.class);
                startActivity(moveToRegistrationType);
            }
        });

        return view;
    }
}