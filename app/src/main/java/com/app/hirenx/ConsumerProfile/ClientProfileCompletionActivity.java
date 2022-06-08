package com.app.hirenx.ConsumerProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;

public class ClientProfileCompletionActivity extends AppCompatActivity {

    private Button btnHire;
    private ImageView imgClosePartnerSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_completion);

        imgClosePartnerSetup=findViewById(R.id.img_close_profile_setup);
        btnHire=findViewById(R.id.btn_hire);

        imgClosePartnerSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoHirePagePartner=new Intent(getApplicationContext(), HomePageClientActivity.class);

                startActivity(movetoHirePagePartner);

                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoHirePagePartner=new Intent(getApplicationContext(), HomePageClientActivity.class);

                startActivity(movetoHirePagePartner);
            }
        });
    }
}