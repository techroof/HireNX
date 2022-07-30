package com.app.hirenx.LayoutsConsumerProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.hirenx.Adapters.MenuAdapter;
import com.app.hirenx.Authentication.RegistrationTypeActivity;
import com.app.hirenx.R;
import com.google.firebase.auth.FirebaseAuth;


public class MenuClientFragment extends Fragment {

    private RecyclerView rvProfile;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager linearlayoutmanager;
    private FirebaseAuth mAuth;
    private Button btnLogout;
    public static Context context;
    private String[] name = {"Help & Support", "Contact Us", "About Us", "Terms and Privacy Policy"
            , "App info", "Refer to a friend", "Rate us on playstore"};
    private int[] images = {R.drawable.help, R.drawable.mail, R.drawable.aboutus, R.drawable.policieslock,
            R.drawable.appinfo, R.drawable.refer, R.drawable.star};


    public MenuClientFragment() {
        // Required empty public constructor
    }

    public static MenuClientFragment newInstance(String param1, String param2) {
        MenuClientFragment fragment = new MenuClientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu_client, container, false);
        mAuth=FirebaseAuth.getInstance();
        btnLogout=view.findViewById(R.id.btn_logout_client);
        rvProfile = view.findViewById(R.id.rv_client_menu_view);
        rvProfile.setHasFixedSize(true);
        linearlayoutmanager = new LinearLayoutManager(getContext());
        rvProfile.setLayoutManager(linearlayoutmanager);
        adapter = new MenuAdapter(getContext(), name, images);
        rvProfile.setAdapter(adapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                Intent moveToRegisterclient =new Intent(getContext(), RegistrationTypeActivity.class);
                moveToRegisterclient.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(moveToRegisterclient);
            }
        });
        return view;
    }
}