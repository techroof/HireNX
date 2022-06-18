package com.app.hirenx.ConsumerProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.Adapters.ClientLayoutPagerAdapter;
import com.app.hirenx.Adapters.LayoutPagerAdapter;
import com.app.hirenx.Authentication.RegistrationTypeActivity;
import com.app.hirenx.HomeActivity;
import com.app.hirenx.IntroSlider.IntroSliderActivity;
import com.app.hirenx.LayoutsConsumerProfile.ProfileClientFragment;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup2Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup3Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;
import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;
import com.app.hirenx.ShowingIntroSlider.ShowingFirstIntroSlider;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class HomePageClientActivity extends AppCompatActivity {

    private FragmentManager clientfm;
    private ClientLayoutPagerAdapter clientPagerAdapter;
    private ViewPager viewPager;
    private ImageView imgHome, imgProfile, imgMenu;
    private TextView tvHome, tvProfile, tvMenu, tvHeading;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uId, checkOnBoarding;
    private FirebaseUser user;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_client);

        ShowingFirstIntroSlider prefManager = new ShowingFirstIntroSlider(getApplicationContext());


       /* if(prefManager.isFirstTimeLaunch()){
            prefManager.setFirstTimeLaunch(false);
           startActivity(new Intent(HomePageClientActivity.this, IntroSliderActivity.class));
            finish();
        }*/

        imgProfile = findViewById(R.id.img_client_profile);
        imgMenu = findViewById(R.id.img_client_menu);
        imgHome = findViewById(R.id.img_client_home);
        tvProfile = findViewById(R.id.tv_client_profile);
        tvMenu = findViewById(R.id.tv_client_menu);
        tvHome = findViewById(R.id.tv_client_home);
        tvHeading = findViewById(R.id.label_home);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //uId= firebaseAuth.getUid();
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager_client);

        /* checking if user is already login or not*/

        user = firebaseAuth.getCurrentUser();

        GetUserStatus();

        pager.beginFakeDrag();
        /** Getting fragment manager */
        clientfm = getSupportFragmentManager();

        /** Instantiating FragmentPagerAdapter */
        clientPagerAdapter = new ClientLayoutPagerAdapter(clientfm);

        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(clientPagerAdapter);

        //
        tvHeading.setText("Home");
        imgHome.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
        tvHome.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
        imgProfile.setColorFilter(getResources().getColor(R.color.profilecardview));
        tvProfile.setTextColor(getResources().getColor(R.color.profilecardview));
        imgMenu.setColorFilter(getResources().getColor(R.color.profilecardview));
        tvMenu.setTextColor(getResources().getColor(R.color.profilecardview));


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvHeading.setText("Home");
                imgHome.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
                tvHome.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
                imgProfile.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvProfile.setTextColor(getResources().getColor(R.color.profilecardview));
                imgMenu.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvMenu.setTextColor(getResources().getColor(R.color.profilecardview));
                pager.setCurrentItem(0);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvHeading.setText("Profile");
                imgProfile.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
                tvProfile.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
                imgHome.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvHome.setTextColor(getResources().getColor(R.color.profilecardview));
                imgMenu.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvMenu.setTextColor(getResources().getColor(R.color.profilecardview));
                pager.setCurrentItem(1);
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvHeading.setText("Menu");
                imgMenu.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
                tvMenu.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
                imgProfile.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvProfile.setTextColor(getResources().getColor(R.color.profilecardview));
                imgHome.setColorFilter(getResources().getColor(R.color.profilecardview));
                tvHome.setTextColor(getResources().getColor(R.color.profilecardview));
                pager.setCurrentItem(2);
            }
        });

    }


    private void GetUserStatus() {

        uId = user.getPhoneNumber();

        firestore.collection("users").document(uId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    status = task.getResult().getString("stepStatus");

                    if (status == null) {

                        Intent moveToConsumer1 = new Intent(HomePageClientActivity.this, ClientProfileSetupActivity.class);
                        startActivity(moveToConsumer1);
                        Toast.makeText(getApplicationContext(), "Please Complete Your Profile First", Toast.LENGTH_LONG).show();

                    } else if ( status.equals("1")) {

                        Intent moveToConsumer2 = new Intent(HomePageClientActivity.this, ClientProfileSetup2Activity.class);
                        startActivity(moveToConsumer2);
                        Toast.makeText(getApplicationContext(), "Please Complete Your Profile First", Toast.LENGTH_LONG).show();

                    }

                } else {

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}