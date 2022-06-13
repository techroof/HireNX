package com.app.hirenx.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.ConsumerProfile.ClientProfileSetupActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.app.hirenx.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private String selectedCountryCode;
    private TextView tvPhoneDesc;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String phNumber,verificationId,authentication,userType;
    private TextInputLayout edtNumber;
    private Button btnOTP,btnMovetoLogin;
    private ImageView imgMoveToRegistrationType;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        countryCodePicker=findViewById(R.id.country_code_spinner);
        tvPhoneDesc=findViewById(R.id.label_desc);
        imgMoveToRegistrationType=findViewById(R.id.img_move_towards_registration_type);
        //firebase initialization

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        edtNumber=findViewById(R.id.edt_phone_number);
        btnOTP=findViewById(R.id.btn_request_otp_registration);

        btnMovetoLogin=findViewById(R.id.btn_move_towards_login);

        pd = new ProgressDialog(RegisterActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Sending OTP...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userType=extras.getString("userType");

            // and get whatever type user account id is
        }

        selectedCountryCode=countryCodePicker.getSelectedCountryCodeWithPlus();
        tvPhoneDesc.setText("Enter phone number without adding "+ selectedCountryCode);

        btnMovetoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToLogin=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(moveToLogin);
            }
        });


        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phNumber=selectedCountryCode+edtNumber.getEditText().getText().toString();

                if (TextUtils.isEmpty(edtNumber.getEditText().getText())){

                    edtNumber.setError("Enter Phone Number");

                }else{

                    pd.show();

                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = db.collection("users").document(phNumber);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(final DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                //redirect to home page

                                pd.dismiss();

                                Toast.makeText(getApplicationContext(), "Your Account Has Already Been Created, Please Login!", Toast.LENGTH_SHORT).show();


                            } else {

                                //redirect to sign up page

                                sendVerificationCode(phNumber);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        }
                    });

                }


            }
        });

        imgMoveToRegistrationType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             onBackPressed();

            }
        });

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                selectedCountryCode=countryCodePicker.getSelectedCountryCodeWithPlus();

                tvPhoneDesc.setText("Enter phone number without adding "+ selectedCountryCode);

            }
        });
    }
    //otp generator
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.MILLISECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            pd.dismiss();
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
            verificationId = s;
            Intent intent=new Intent(RegisterActivity.this, OTPActivity.class);
            intent.putExtra("phoneNumber",phNumber);
            intent.putExtra("userType",userType);
            intent.putExtra("authenticationType","register");
            intent.putExtra("verificationId",verificationId);
            startActivity(intent);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    };
}