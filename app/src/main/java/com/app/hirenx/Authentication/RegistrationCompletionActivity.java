package com.app.hirenx.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.hirenx.ConsumerProfile.ClientProfileSetupActivity;
import com.app.hirenx.R;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;

public class RegistrationCompletionActivity extends AppCompatActivity {

    private Button btnProfileCompletion;
    private String userType,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_completion);

        btnProfileCompletion=findViewById(R.id.btn_profile_completion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userType= extras.getString("userType");
            phoneNumber=extras.getString("phoneNumber");
        }

        btnProfileCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userType.equals("consumer")){

                    Intent movetoClientProfileSetup =new Intent(getApplicationContext(), ClientProfileSetupActivity.class);
                    movetoClientProfileSetup.putExtra("registerType",userType);
                    movetoClientProfileSetup.putExtra("phoneNumber",phoneNumber);
                    startActivity(movetoClientProfileSetup);
                    finish();

                }else if(userType.equals("partner")){

                    Intent movetoPartnerProfileSetup =new Intent(getApplicationContext(), PartnerProfileSetupActivity.class);
                    movetoPartnerProfileSetup.putExtra("registerType",userType);
                    movetoPartnerProfileSetup.putExtra("phoneNumber",phoneNumber);
                    startActivity(movetoPartnerProfileSetup);
                    finish();

                }
            }
        });


    }
}