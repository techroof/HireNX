package com.app.hirenx.PartnerProfile;

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

import com.app.hirenx.Adapters.LayoutPagerAdapter;
import com.app.hirenx.ConsumerProfile.ClientProfileSetup2Activity;
import com.app.hirenx.ConsumerProfile.ClientProfileSetupActivity;
import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.app.hirenx.R;
import com.caverock.androidsvg.SVGImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePagePartnerActivity extends AppCompatActivity {

    private FragmentManager fm;
    private LayoutPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ImageView imgProfile, imgMenu;
    private TextView tvProfile, tvMenu, tvHeading;
    private String uId,userType,status;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_partner);

        imgProfile = findViewById(R.id.img_profile);
        imgMenu = findViewById(R.id.img_menu);
        tvProfile = findViewById(R.id.tv_profile);
        tvMenu = findViewById(R.id.tv_menu);
        tvHeading = findViewById(R.id.label_profile);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager_profile);

        pager.beginFakeDrag();
        /** Getting fragment manager */
        fm = getSupportFragmentManager();

        /** Instantiating FragmentPagerAdapter */
        pagerAdapter = new LayoutPagerAdapter(fm);

        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);

        tvHeading.setText("Profile");
        imgProfile.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
        tvProfile.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
        imgMenu.setColorFilter(getResources().getColor(R.color.black));
        tvMenu.setTextColor(getResources().getColor(R.color.black));
        pager.setCurrentItem(0);

        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        GetUserStatus();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvHeading.setText("Profile");
                imgProfile.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
                tvProfile.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
                imgMenu.setColorFilter(getResources().getColor(R.color.black));
                tvMenu.setTextColor(getResources().getColor(R.color.black));
                pager.setCurrentItem(0);
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvHeading.setText("Menu");
                imgMenu.setColorFilter(getResources().getColor(R.color.headingtextviewcolor));
                tvMenu.setTextColor(getResources().getColor(R.color.headingtextviewcolor));
                imgProfile.setColorFilter(getResources().getColor(R.color.black));
                tvProfile.setTextColor(getResources().getColor(R.color.black));
                pager.setCurrentItem(1);
            }
        });
        //pager.setPageTransformer(true, new ZoomOutPageTransformer());

    }

    private void GetUserStatus() {

        user=firebaseAuth.getCurrentUser();
        uId = user.getPhoneNumber();

        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    userType = task.getResult().getString("userType");
                    status=task.getResult().getString("stepStatus");

                    if (userType.equals("partner") && status == null) {

                        Intent moveToPartner1 = new Intent(ProfilePagePartnerActivity.this, PartnerProfileSetupActivity.class);
                        startActivity(moveToPartner1);
                        Toast.makeText(getApplicationContext(), "Please Complete Your Profile First", Toast.LENGTH_LONG).show();

                    } else if (userType.equals("partner") && status.equals("1")) {

                        Intent moveToPartner2 = new Intent(ProfilePagePartnerActivity.this, PartnerProfileSetup2Activity.class);
                        startActivity(moveToPartner2);
                        Toast.makeText(getApplicationContext(), "Please Complete Your Profile First", Toast.LENGTH_LONG).show();


                    } else if (userType.equals("partner") && status.equals("2")) {

                        Intent moveToPartner3 = new Intent(ProfilePagePartnerActivity.this, PartnerProfileSetup3Activity.class);
                        startActivity(moveToPartner3);
                        Toast.makeText(getApplicationContext(), "Please Complete Your Profile First", Toast.LENGTH_LONG).show();

                    }

                }else{

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}