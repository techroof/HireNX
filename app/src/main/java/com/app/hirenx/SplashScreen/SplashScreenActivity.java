package com.app.hirenx.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.app.hirenx.Authentication.RegistrationTypeActivity;
import com.app.hirenx.ConsumerProfile.ClientProfileSetup2Activity;
import com.app.hirenx.ConsumerProfile.ClientProfileSetupActivity;
import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.app.hirenx.IntroSlider.IntroSliderActivity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup2Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup3Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;
import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1500;
    private String uId,userType;
    private String checkOnBoarding = "null";
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Paper.init(this);

        checkOnBoarding = Paper.book().read("checkOnboarding");
        firestore=FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkOnBoarding == null) {

                    Intent moveToIntroSlider = new Intent(getApplicationContext(), IntroSliderActivity.class);
                    startActivity(moveToIntroSlider);

                } else {

                    if (user == null) {

                        //Intent login = new Intent(HomePageClientActivity.this, RegistrationTypeActivity.class);
                        Intent login = new Intent(SplashScreenActivity.this, RegistrationTypeActivity.class);

                        startActivity(login);
                        finish();

                    } else {

                        GetUserStatus();

                    }

                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void GetUserStatus() {

        uId = user.getPhoneNumber();

        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    userType = task.getResult().getString("userType");

                    if (userType.equals("partner")) {

                        Intent moveToPartner = new Intent(SplashScreenActivity.this, ProfilePagePartnerActivity.class);
                        startActivity(moveToPartner);

                    } else if (userType.equals("consumer")) {

                        Intent moveToConsumer = new Intent(SplashScreenActivity.this, HomePageClientActivity.class);
                        startActivity(moveToConsumer);

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
