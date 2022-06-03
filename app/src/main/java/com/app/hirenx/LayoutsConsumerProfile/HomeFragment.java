package com.app.hirenx.LayoutsConsumerProfile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.hirenx.Adapters.SliderAdapter;
import com.app.hirenx.ConsumerProfile.ClientSearchHireActivity;
import com.app.hirenx.Models.SliderImages;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private String imgurl1,imgurl2,imgurl3;
    private FirebaseFirestore firestore;
    ArrayList<SliderImages> sliderDataArrayList;
    SliderView sliderView;
    SliderAdapter adapter;
    ImageView arrow;
    ConstraintLayout hiddenView;
    CardView cardViewUse;
    Button btnHire;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        firestore=FirebaseFirestore.getInstance();
        
        sliderView = view.findViewById(R.id.slider);

        sliderDataArrayList = new ArrayList<SliderImages>();

        cardViewUse=view.findViewById(R.id.crdview_use);

        hiddenView=view.findViewById(R.id.cl2_crdview_use);

        arrow=view.findViewById(R.id.img_expand_view);

        btnHire=view.findViewById(R.id.btn_hire_home);

        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveSearchClient=new Intent(getContext(), ClientSearchHireActivity.class);
                startActivity(moveSearchClient);
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hiddenView.getVisibility() == View.VISIBLE) {

                    Drawable imgDrawable = getResources().getDrawable(R.drawable.downarrow);
                    arrow.setImageDrawable(imgDrawable);

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(cardViewUse,
                            new AutoTransition());
                    hiddenView.setVisibility(View.GONE);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {
                    Drawable imgDrawable = getResources().getDrawable(R.drawable.arrow_up);
                    arrow.setImageDrawable(imgDrawable);
                    arrow.setColorFilter(R.color.black);

                    TransitionManager.beginDelayedTransition(cardViewUse,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);

                }
            }
        });

        GetImages();

        return view;
    }



    private void GetImages() {

        firestore.collection("Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String img1=documentSnapshot.getString("imgUrl1");
                    imgurl1=img1;
                    String img2=documentSnapshot.getString("imgUrl2");
                    imgurl2=img2;
                    String img3=documentSnapshot.getString("imgUrl3");
                    imgurl3=img3;
                }
                //Toast.makeText(getApplicationContext(), ""+imgurl1, Toast.LENGTH_LONG).show();
                SlidingImages();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private  void SlidingImages() {


        sliderDataArrayList.add(new SliderImages(imgurl1));
        sliderDataArrayList.add(new SliderImages(imgurl2));
        sliderDataArrayList.add(new SliderImages(imgurl3));

        //Toast.makeText(getApplicationContext(),"bikes"+sliderDataArrayList.get(1),Toast.LENGTH_LONG).show();

        adapter = new SliderAdapter(getContext(),sliderDataArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);

        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();

    }

}
