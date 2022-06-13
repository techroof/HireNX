package com.app.hirenx.ConsumerProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.hirenx.PartnerProfile.PartnerProfileSetup2Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup3Activity;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientProfileSetup2Activity extends AppCompatActivity {

    private CardView crdtakePhoto, crdFileFront, crdFileBack;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button btnProceedstep3;
    private String uid;
    private ProgressDialog pd;
    private static final int GALLERY = 1, CAMERA = 2;
    private StorageReference mStorage;
    String Storage_Path = "All_User_Images_Uploads/";
    String Storage_PDFPath = "All_User_PDF_Uploads/";
    private String imgUrl;
    String mCurrentPhotoPath;
    private Uri photoURI, pdfUri;
    private ImageView imgCameraSelfieClient,imgPdfFrontClient,imgPdfBackClient;
    private String checkImageupload, checkPdfUploadFront, checkPdfUploadBack, backPdf,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_setup2);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            phoneNumber=extras.getString("phoneNumber");

        }else{

            phoneNumber=user.getPhoneNumber();
        }


        pd = new ProgressDialog(ClientProfileSetup2Activity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Uploading image...");


        uid = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance().getReference();

        crdtakePhoto = findViewById(R.id.crdView_take_client_a_selfie);
        crdFileFront = findViewById(R.id.crdview_upload_client_document_front);
        crdFileBack = findViewById(R.id.crdview_upload_client_document_back);

        imgCameraSelfieClient=findViewById(R.id.img_take_selfie_client);
        imgPdfBackClient=findViewById(R.id.img_document_back_client);
        imgPdfFrontClient=findViewById(R.id.img_document_front_client);

        btnProceedstep3 = findViewById(R.id.btn_partner_proceed_step2);

        btnProceedstep3 = findViewById(R.id.btn_client_proceed_step2);

        crdtakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();

            }
        });

        crdFileFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backPdf = "FrontFile";
                Toast.makeText(getApplicationContext(), "Please select front side pdf of id", Toast.LENGTH_LONG).show();

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, GALLERY);
            }


        });

        crdFileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backPdf = "BackFile";
                Toast.makeText(getApplicationContext(), "Please select back side pdf of your id", Toast.LENGTH_LONG).show();

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        btnProceedstep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkImageupload.equals("checked")
                        && checkPdfUploadFront.equals("checked")
                        && checkPdfUploadBack.equals("checked")) {

                    Intent movetoPartnerSetup2 = new Intent(ClientProfileSetup2Activity.this, ClientProfileCompletionActivity.class);
                    startActivity(movetoPartnerSetup2);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    Toast.makeText(getApplicationContext(), "Step 2 completed", Toast.LENGTH_LONG).show();

                }else if (checkImageupload == null) {

                    Toast.makeText(getApplicationContext(), "Please upload your image", Toast.LENGTH_LONG).show();


                }
                if (checkPdfUploadFront == null) {

                    Toast.makeText(getApplicationContext(), "Please upload your front pdf of id", Toast.LENGTH_LONG).show();


                }
                if (checkPdfUploadBack == null) {

                    Toast.makeText(getApplicationContext(), "Please upload your back pdf of id", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK) {

            File f = new File(mCurrentPhotoPath);
            //imgView.setImageURI(Uri.fromFile(f));
            pd.show();

            Intent mediaintent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaintent.setData(contentUri);
            this.sendBroadcast(mediaintent);

            UploadImageTofirebase(f.getName(), contentUri);


        } else if (requestCode == GALLERY && resultCode == RESULT_OK) {


            UploadPdfFile(data.getData());

        }

    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void UploadImageTofirebase(String name, Uri contentUri) {


        final StorageReference filepath = mStorage.child(Storage_Path + name);
        filepath.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        AddImgUrl(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UploadPdfFile(Uri data) {

        pd = new ProgressDialog(ClientProfileSetup2Activity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Uploading pdf file...");
        pd.show();

        final StorageReference filepath = mStorage.child(Storage_PDFPath + +System.currentTimeMillis() + "." +
                GetFileExtension(data));
        filepath.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        pd.dismiss();
                        if (backPdf.equals("BackFile")) {

                            AddPdfUrlBack(uri.toString());

                        } else if(backPdf.equals("FrontFile")){


                            AddPdfUrl(uri.toString());


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AddImgUrl(String downloadUri) {

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("userImage", downloadUri);
        userProfileMap.put("stepStatus","2");

        firestore.collection("users")
                .document(phoneNumber)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            checkImageupload = "checked";
                            Drawable imgDrawable = getResources().getDrawable(R.drawable.done_verify);
                            imgCameraSelfieClient.setImageDrawable(imgDrawable);
                            imgCameraSelfieClient.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                                    R.color.donecolor), android.graphics.PorterDuff.Mode.SRC_IN);


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


    private void AddPdfUrl(String downloadUri) {

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("pdfIdFront", downloadUri);
        userProfileMap.put("stepStatus","2");

        firestore.collection("users")
                .document(phoneNumber)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            checkPdfUploadFront = "checked";

                            Drawable imgDrawable = getResources().getDrawable(R.drawable.done_verify);
                            imgPdfFrontClient.setImageDrawable(imgDrawable);
                            imgPdfFrontClient.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                                    R.color.donecolor), android.graphics.PorterDuff.Mode.SRC_IN);
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


    private void AddPdfUrlBack(String downloadUri) {

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("pdfIdBack", downloadUri);
        userProfileMap.put("stepStatus","2");


        firestore.collection("users")
                .document(phoneNumber)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            checkPdfUploadBack = "checked";
                            //backPdf=null;

                            Drawable imgDrawable = getResources().getDrawable(R.drawable.done_verify);
                            imgPdfBackClient.setImageDrawable(imgDrawable);
                            imgPdfBackClient.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                                    R.color.donecolor), android.graphics.PorterDuff.Mode.SRC_IN);

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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

// Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.app.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }


    private File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}