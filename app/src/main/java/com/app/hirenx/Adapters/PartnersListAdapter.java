package com.app.hirenx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hirenx.Interfaces.OpenBottomSheetClickListener;
import com.app.hirenx.Models.PartnersList;
import com.app.hirenx.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartnersListAdapter extends RecyclerView.Adapter<PartnersListAdapter.ViewAdapter>  {
    public PartnersListAdapter(ArrayList<PartnersList> arraylistPartnersLists, Context context, OpenBottomSheetClickListener bottomSheetClickListener) {
        this.arraylistPartnersLists = arraylistPartnersLists;
        this.context = context;
        this.bottomSheetClickListener = bottomSheetClickListener;

        firestore=FirebaseFirestore.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser= firebaseAuth.getCurrentUser();

        id=firebaseUser.getPhoneNumber();
    }

    private ArrayList<PartnersList> arraylistPartnersLists;

    private Context context;

    private OpenBottomSheetClickListener bottomSheetClickListener;


    private String status,expireDates,getExpireDates,todaysDate;

    private String id;

    private FirebaseFirestore firestore;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser firebaseUser;









    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_search_client, parent, false);
        return new ViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter holder, int position) {

        //id=firebaseUser.getPhoneNumber();


        PartnersList partnersList= arraylistPartnersLists.get(position);


        holder.tvName.setText(partnersList.getFullName());
        holder.tvAddress.setText(partnersList.getAddress());
        Glide.with(context)
                .load(partnersList.getUserImage())
                .placeholder(R.drawable.user_avatar) // image url
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.circleImageView);
        holder.tvContactNumber.setText(partnersList.getPhoneNumber());





            holder.imgVisibleStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.eye_off));
            holder.tvContactNumber.setText("+91-XXXXXXXXX");
            holder.imgVisibleStatus.setTag(R.drawable.eye_off);



        holder.imgVisibleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firestore.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        if(task.isSuccessful()){

                            status = task.getResult().getString("activatedStatus");

                            if(status.equals("activated")){


                                holder.imgVisibleStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.eye));
                                holder.tvContactNumber.setText(partnersList.getPhoneNumber());
                                holder.imgVisibleStatus.setTag(R.drawable.eye);

                                bottomSheetClickListener.onItemClicks();


                            }else if(status.equals("normal")){

                                bottomSheetClickListener.onItemClicks();


                            }

                            //PartnersSkillList();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context.getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });





                //if ((int)holder.imgVisibleStatus.getTag()==R.drawable.eye_off){

                    /*AppCompatActivity appCompatActivity = new AppCompatActivity();
                    FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();

                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                    bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), bottomSheetDialog.getTag());

*/
                //}
                    //Toast.makeText(context.getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                    /*BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                    bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "ModalBottomSheet");
                    */
            }
        });




    }

    @Override
    public int getItemCount() {
        return arraylistPartnersLists.size();
    }

    public class ViewAdapter extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress,tvContactNumber;
        CircleImageView circleImageView;
        ImageView imgVisibleStatus;
        public ViewAdapter(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_partner_name);
            tvAddress=itemView.findViewById(R.id.tv_details);
            tvContactNumber=itemView.findViewById(R.id.tv_contact_number);
            circleImageView=itemView.findViewById(R.id.partners_img);
            imgVisibleStatus=itemView.findViewById(R.id.img_number_visible);

        }
    }
}
