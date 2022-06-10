package com.app.hirenx.LayoutsPartnerProfile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.LayoutsConsumerProfile.ProfileClientFragment;
import com.app.hirenx.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView tvPartnerName, tvPartnerDateofBirth, tvPartnerGender, tvPartnerMaritalStatus, tvPartnerEmail, tvPartnerPhoneNumber, tvPartnerAddress1, tvPartnerAddress2, tvPartnerLandMark, tvPartnerVillage, tvPartnerState,tvPinCode;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uId,strUserimg;
    private CircleImageView partnerImage;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog,pd;
    private FirebaseUser user;
    private String Storage_Path = "All_User_Images_Uploads/";


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        uId=user.getPhoneNumber();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);

        pd=new ProgressDialog(getContext());
        pd.setMessage("Please Wait...");
        pd.setCanceledOnTouchOutside(false);

        tvPartnerName = view.findViewById(R.id.tv_partner_name);
        tvPartnerDateofBirth = view.findViewById(R.id.tv_partner_dob);
        tvPartnerGender = view.findViewById(R.id.tv_gender);
        tvPartnerMaritalStatus = view.findViewById(R.id.tv_marital_status);
        tvPartnerEmail = view.findViewById(R.id.tv_email_address);
        tvPartnerPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvPartnerAddress1 = view.findViewById(R.id.tv_address1);
        tvPartnerAddress2 = view.findViewById(R.id.tv_address2);
        tvPartnerLandMark = view.findViewById(R.id.tv_landmark);
        tvPartnerVillage = view.findViewById(R.id.tv_city_village);
        tvPartnerState = view.findViewById(R.id.tv_state);
        tvPinCode=view.findViewById(R.id.tv_pincode);
        partnerImage = view.findViewById(R.id.img_partner_profile);


        partnerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getimagefromgallery();
            }
        });
        GetPartnerProfile();


        return view;
    }

    private void GetPartnerProfile() {

        pd.show();
        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){

                    String partnerName = task.getResult().getString("fullName");
                    String partnerDateOfBirth = task.getResult().getString("dateofbirth");
                    String partnerGender = task.getResult().getString("gender");
                    String partnerMaritalStatus = task.getResult().getString("maritalStatus");
                    String partnerEmail = task.getResult().getString("email");
                    String partnerPhoneNumber = task.getResult().getString("phoneNumber");
                    String partnerAddress1 = task.getResult().getString("address");
                    String partnerAddress2 = task.getResult().getString("address2");
                    String partnerLandMark = task.getResult().getString("landMark");
                    String partnerVillage = task.getResult().getString("city");
                    String partnerState = task.getResult().getString("state");
                    String pincode=task.getResult().getString("pinCode");
                    String imgUrl=task.getResult().getString("userImage");

                    tvPartnerName.setText(partnerName);
                    tvPartnerDateofBirth.setText(partnerDateOfBirth);
                    tvPartnerGender.setText(partnerGender);
                    tvPartnerMaritalStatus.setText(partnerMaritalStatus);
                    tvPartnerEmail.setText(partnerEmail);
                    tvPartnerPhoneNumber.setText(partnerPhoneNumber);
                    tvPartnerAddress1.setText(partnerAddress1);
                    tvPartnerAddress2.setText(partnerAddress2);
                    tvPartnerLandMark.setText(partnerLandMark);
                    tvPartnerVillage.setText(partnerVillage);
                    tvPartnerState.setText(partnerState);
                    tvPinCode.setText(pincode);


                    Glide.with(ProfileFragment.this)

                            .load(imgUrl)

                            .placeholder(R.drawable.add)

                            .into(partnerImage);

                    pd.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    public void getimagefromgallery() {

        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageUri != null) {
            imageUri = data.getData();
            partnerImage.setImageURI(imageUri);

            UploadImageFileToFirebaseStorage();
        }


    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void UploadImageFileToFirebaseStorage() {
        if (imageUri != null) {

            progressDialog.show();

            StorageReference storageReference2nd =
                    storageReference
                            .child(Storage_Path + System.currentTimeMillis() + "." +
                                    GetFileExtension(imageUri));

            storageReference2nd.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            strUserimg = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            progressDialog.dismiss();

                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    strUserimg = uri.toString();
                                    //Toast.makeText(getContext(), "URI" + strUserimg, Toast.LENGTH_SHORT).show();

                                    addImg(strUserimg);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();

                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("http", "onFailure: " + exception.getMessage());
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            //progressDialog.setTitle("Image is Uploading...");

                        }
                    });


        } else {

            Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }


    }


    private void addImg(String strUserimg) {

        Map<String, Object> userimgMap = new HashMap<>();
        userimgMap.put("img", strUserimg);

        firestore.collection("users")
                .document(uId)
                .update(userimgMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image Submitted Successfully", Toast.LENGTH_SHORT).show();


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();


            }
        });

    }
}