package com.app.hirenx.PartnerProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.Adapters.CategoryRecyclerViewAdapter;
import com.app.hirenx.Adapters.SkillsRecyclerViewAdapter;
import com.app.hirenx.Interfaces.InsertSkillsClickListener;
import com.app.hirenx.Models.CategoryText;
import com.app.hirenx.Models.Skills;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PartnerProfileSetup3Activity extends AppCompatActivity implements InsertSkillsClickListener {

    private ArrayList<String> addSkillsArrayList;
    private ArrayList<CategoryText> categoryTextsArrayList;
    private SkillsRecyclerViewAdapter skillsRecyclerViewAdapter;
    private CategoryRecyclerViewAdapter recyclerViewAdapters;
    private RecyclerView skillsRv;
    private FirebaseFirestore firestore;
    private LinearLayoutManager linearLayoutManager;
    private RadioGroup rdButton;
    private String driveStatus, phoneNumber;
    private Button submitBtn;
    private CheckBox chkbxtwoWheeler, chkbxCar;
    private ArrayList<String> arraylistCurrentVehicle = new ArrayList<>();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile_setup3);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            phoneNumber = extras.getString("phoneNumber");

        } else {

            phoneNumber = user.getPhoneNumber();
        }
        pd = new ProgressDialog(PartnerProfileSetup3Activity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Submitting...");

        addSkillsArrayList = new ArrayList<>();
        categoryTextsArrayList = new ArrayList<>();

        rdButton = findViewById(R.id.radio_group);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-arraylist"));

        firestore = FirebaseFirestore.getInstance();
        skillsRv = findViewById(R.id.recyclerView_categories);
        submitBtn = findViewById(R.id.btn_partner_submit);
        chkbxtwoWheeler = findViewById(R.id.checkbox_2_wheeler);
        chkbxCar = findViewById(R.id.checkbox_2_car);

        skillsRecyclerViewAdapter = new SkillsRecyclerViewAdapter();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        skillsRv.setLayoutManager(linearLayoutManager);

        //skillsRv.setNestedScrollingEnabled(true);

        getCategories();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chkbxtwoWheeler.isChecked()) {

                    arraylistCurrentVehicle.add("Two Wheeler");


                }
                if (chkbxCar.isChecked()) {

                    arraylistCurrentVehicle.add("Car");

                }

                if (!TextUtils.isEmpty(driveStatus)
                        && !arraylistCurrentVehicle.isEmpty()
                        && !skillsRecyclerViewAdapter.skillsNamesArrayList.isEmpty()) {


                    SubmitData(driveStatus, arraylistCurrentVehicle, skillsRecyclerViewAdapter.skillsNamesArrayList);

                } else {

                    Toast.makeText(getApplicationContext(), "Select all above values", Toast.LENGTH_SHORT).show();

                }


                //Toast.makeText(getApplicationContext(), ""+skillsRecyclerViewAdapter.skillsNamesArrayList, Toast.LENGTH_SHORT).show();
            }
        });
        //getData();

        /*crdViewTileMistri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crdViewTileMistri.setCardBackgroundColor(getResources().getColor(R.color.buttoncolor));
                tvTileMistri.setTextColor(getResources().getColor(R.color.white));
                imgTileMistri.setImageDrawable(getResources().getDrawable(R.drawable.close_white));

            }
        });*/

        rdButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {

                    case R.id.radiobutton_yes:
                        driveStatus = "yes";
                        break;

                    case R.id.radiobutton_no:
                        driveStatus = "no";

                        break;
                }


            }
        });


    }


    private void getCategories() {


        firestore.collection("categories")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    //Skills listData = documentSnapshot.toObject(Skills.class);
                    CategoryText listDataa = documentSnapshot.toObject(CategoryText.class);

                    //skillsArrayList.add(listData);
                    categoryTextsArrayList.add(listDataa);

                }


                recyclerViewAdapters = new CategoryRecyclerViewAdapter(categoryTextsArrayList,
                        getApplicationContext());
                skillsRv.setAdapter(recyclerViewAdapters);


            }
        });
    }

    @Override
    public void onItemclick(ArrayList<String> skillsNames) {

        addSkillsArrayList = new ArrayList<>();
        addSkillsArrayList = skillsNames;

        for (int i = 0; i < addSkillsArrayList.size(); i++) {


            Toast.makeText(getApplicationContext(), "" + addSkillsArrayList.get(i), Toast.LENGTH_SHORT).show();
        }
    }

    BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addSkillsArrayList = new ArrayList<String>();
            addSkillsArrayList = intent.getStringArrayListExtra("arraylist");
            //Toast.makeText(PartnerProfileSetup3Activity.this,""+addSkillsArrayList ,Toast.LENGTH_SHORT).show();

        }
    };

    private void SubmitData(String driveStatus, ArrayList<String> currentVehicles, ArrayList<String> skills) {

        pd.show();

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("drivingStatus", driveStatus);
        userProfileMap.put("currentVehicles", currentVehicles);
        userProfileMap.put("skills", skills);
        userProfileMap.put("stepStatus", "3");
        //userProfileMap.put("skills", Arrays.asList("Hamburger", "Vegetables"));

        firestore.collection("users")
                .document(phoneNumber)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            Intent movetoPartnerCompletionSetup = new Intent(PartnerProfileSetup3Activity.this, CompletionProfilePartnerActivity.class);
                            startActivity(movetoPartnerCompletionSetup);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            Toast.makeText(getApplicationContext(), "Profile Setup Is Completed", Toast.LENGTH_SHORT).show();

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
}
