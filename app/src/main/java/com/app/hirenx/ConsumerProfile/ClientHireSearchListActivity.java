package com.app.hirenx.ConsumerProfile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.Adapters.PartnersListAdapter;
import com.app.hirenx.BottomSheetDialog.BottomSheetDialog;
import com.app.hirenx.Interfaces.OpenBottomSheetClickListener;
import com.app.hirenx.Models.PartnersList;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.PaymentResultListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClientHireSearchListActivity extends AppCompatActivity implements OpenBottomSheetClickListener, PaymentResultListener {

    private OpenBottomSheetClickListener bottomSheetClickListener;
    private FirebaseFirestore firestore;
    private PartnersListAdapter partnersListAdapter;
    private LinearLayoutManager layoutManagerdashboard;
    private ArrayList<PartnersList> partnersListArrayList;
    private TextView tvSkillsandCity;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String uId,city,skill,status,expireDates,getExpireDates,todaysDate;
    private ProgressDialog pd;
    private RecyclerView rvPartnerList;
    private Boolean isBefore;
    private ImageView imgBackToSearch;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_hire_search_list);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uId = user.getPhoneNumber();

        rvPartnerList = findViewById(R.id.rv_partners_list);
        tvSkillsandCity = findViewById(R.id.label_skill_and_city);
        imgBackToSearch = findViewById(R.id.img_move_towards_search);
        partnersListArrayList = new ArrayList<>();
        pd = new ProgressDialog(ClientHireSearchListActivity.this);
        pd.setMessage("Loading Please Wait...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            city = extras.getString("city");
            skill = extras.getString("skill");

        }

        /*Intent intent = getIntent();
        city= intent.getStringExtra("city");
        skill=intent.getStringExtra("skill");*/
        tvSkillsandCity.setText(skill + " " + "from" + " " + city + " ");

        PartnersSkillList();

        partnersListAdapter = new PartnersListAdapter(partnersListArrayList,
                getApplicationContext(), this);

        imgBackToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


    }



    private void PartnersSkillList(){

        if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
        {
            pd.show();
        }

        firestore.collection("users")
                .whereEqualTo("city",city)
                .whereArrayContainsAny("skills", Arrays.asList(skill))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    //GetPlanExpireDate();

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        PartnersList listData = documentSnapshot.toObject(PartnersList.class);
                        partnersListArrayList.add(listData);

                    }


                    layoutManagerdashboard = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    rvPartnerList.setLayoutManager(layoutManagerdashboard);
                    rvPartnerList.setAdapter(partnersListAdapter);

                    if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
                    {
                        pd.dismiss();
                    }



                }

            }
        });

    }

    void AddExpiryDate(String expireDates) {


        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("planExpireDate", expireDates);

        firestore.collection("users")
                .document(uId)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            ChangeActivationStatus();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    void ChangeActivationStatus() {



        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("activationStatus", "activated");

        firestore.collection("users")
                .document(uId)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {



                            if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
                            {
                                pd.dismiss();

                                /*BottomSheetDialog bottomSheet = new BottomSheetDialog();
                                bottomSheet.dismiss();*/


                            }


                            //Payment();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
                {
                    pd.dismiss();
                }

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //activation status normal

    void ChangeActivationStatusNonActivated() {

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("activationStatus", "normal");

        firestore.collection("users")
                .document(uId)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            BottomSheetDialog bottomSheet = new BottomSheetDialog();
                            bottomSheet.show(getSupportFragmentManager(),
                                    "ModalBottomSheet");

                            partnersListAdapter.notifyDataSetChanged();

                            if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
                            {
                                pd.dismiss();
                            }


                            //Payment();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void GetPlanExpireDate() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {

                    getExpireDates = task.getResult().getString("planExpireDate");

                    if (getExpireDates != null) {


                        Calendar c = Calendar.getInstance();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime expireDate = LocalDateTime.now().plusHours(24);


                        todaysDate = dtf.format(now).toString();

                        expireDates = dtf.format(expireDate).toString();

                        //Toast.makeText(getContext(), ""+LocalDateTime.parse(now.format(dtf)), Toast.LENGTH_SHORT).show();
                        isBefore = now.isBefore(LocalDateTime.parse(getExpireDates, dtf));

                        if (isBefore == true) {


                        } else if (isBefore == false) {

                            //Payment();
                            ChangeActivationStatusNonActivated();

                        }


                    } else {

                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        bottomSheet.show(getSupportFragmentManager(),
                                "ModalBottomSheet");
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }





    @Override
    public void onItemClicks() {

        GetPlanExpireDate();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPaymentSuccess(String s) {

        if(!((Activity) ClientHireSearchListActivity.this).isFinishing())
        {
            pd.show();
        }

        Calendar c = Calendar.getInstance();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = LocalDateTime.now().plusHours(24);

        expireDates = dtf.format(expireDate).toString();

        AddExpiryDate(expireDates);

        Toast.makeText(getApplicationContext(), "Order SuccessFull, Transaction Number "+s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getApplicationContext(), "Error"+s, Toast.LENGTH_SHORT).show();

    }



}