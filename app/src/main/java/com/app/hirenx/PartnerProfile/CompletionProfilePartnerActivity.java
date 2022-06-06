package com.app.hirenx.PartnerProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.hirenx.R;

public class CompletionProfilePartnerActivity extends AppCompatActivity {

    private Button btnProfile;
    private ImageView imgClosePartnerCompletion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion_profile_partner);
        btnProfile=findViewById(R.id.btn_profile);
        imgClosePartnerCompletion=findViewById(R.id.img_close_profile_setup);

        imgClosePartnerCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoProfilePagePartner=new Intent(getApplicationContext(),ProfilePagePartnerActivity.class);

                startActivity(movetoProfilePagePartner);

                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoProfilePagePartner=new Intent(getApplicationContext(),ProfilePagePartnerActivity.class);

                startActivity(movetoProfilePagePartner);
            }
        });
    }
}