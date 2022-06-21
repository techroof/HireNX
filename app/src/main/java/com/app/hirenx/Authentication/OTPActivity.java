package com.app.hirenx.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.app.hirenx.HomeActivity;
import com.app.hirenx.OtpReceiver.MessageReceiver;
import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;
import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.OTP;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPActivity extends AppCompatActivity {

    private ImageView imgMoveRegistration;
    private PinView pinView;
    private String getcod, verificationId, enterCode, resendCodeVerificationId, uId, phoneNumber, authenticationType, userType, resentPhoneNumber, userTypeLogin, otps;
    private Button btnVerify;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private TextView tvPhoneNumber;
    private TextView tvResendCode;
    private static final int REQ_USER_CONSENT = 200;
    public MessageReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        imgMoveRegistration = findViewById(R.id.img_move_towards_registration);
        pinView = findViewById(R.id.pin_view);
        btnVerify = findViewById(R.id.btn_verify);
        tvPhoneNumber = findViewById(R.id.label_phone_number);
        tvResendCode = findViewById(R.id.tv_code_desc);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();

        getcod = getIntent().getStringExtra("backendotp");

        if (extras != null) {

            phoneNumber = extras.getString("phoneNumber");
            verificationId = extras.getString("verificationId");
            authenticationType = extras.getString("authenticationType");
            userType = extras.getString("userType");
            tvPhoneNumber.setText(phoneNumber);
            //Toast.makeText(getApplicationContext(), "" + registrarType + authentication, Toast.LENGTH_SHORT).show();
            // and get whatever type user account id is
        }

        imgMoveRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent moveRegistration = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(moveRegistration);*/

                onBackPressed();

            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resentPhoneNumber = tvPhoneNumber.getText().toString();

                sendVerificationCode(resentPhoneNumber);

                Toast.makeText(getApplicationContext(), "Resending code please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterCode = pinView.getText().toString();

                verifyCode(enterCode);
            }
        });

        autoOTPReceiver();
    }

    private void autoOTPReceiver() {

        SmsRetrieverClient client = SmsRetriever.getClient(OTPActivity.this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT) {

            if ((resultCode == RESULT_OK) && (data != null)) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);

            }
        }
    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {

            pinView.setText(matcher.group(0));

        }
    }


    private void registerBroadcastReceiver() {



        messageReceiver = new MessageReceiver();

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(messageReceiver, intentFilter);

        messageReceiver.smsBroadcastReceiverListener = new MessageReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent, REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(messageReceiver);
    }

    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            AddStatus(userType);
                            //finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //otp generator
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
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
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void AddStatus(String userType) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uId = user.getPhoneNumber();
        Map<String, Object> userTypeMap = new HashMap<>();
        userTypeMap.put("userType", userType);

        if (authenticationType.equals("login")) {

            firestore.collection("users")
                    .document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                userTypeLogin = task.getResult().get("userType").toString();

                                if (userTypeLogin.equals("consumer")) {

                                    Intent home = new Intent(OTPActivity.this, HomePageClientActivity.class);
                                    startActivity(home);

                                } else if (userTypeLogin.equals("partner")) {

                                    Intent home = new Intent(OTPActivity.this, ProfilePagePartnerActivity.class);
                                    startActivity(home);

                                }

                            } else {

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        } else if (authenticationType.equals("register")) {

            firestore.collection("users")
                    .document(uId)
                    .set(userTypeMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getApplicationContext(), RegistrationCompletionActivity.class);
                                intent.putExtra("userType", userType);
                                startActivity(intent);
                                Toast.makeText(OTPActivity.this, "Your Account Has Been Created.", Toast.LENGTH_LONG).show();
                                finish();

                            } else {

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Failed To Create Account", Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }


}

