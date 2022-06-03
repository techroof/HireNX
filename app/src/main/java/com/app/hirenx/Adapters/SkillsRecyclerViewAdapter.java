package com.app.hirenx.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hirenx.Interfaces.InsertSkillsClickListener;
import com.app.hirenx.Models.Skills;
import com.app.hirenx.R;

import java.util.ArrayList;


public class SkillsRecyclerViewAdapter extends RecyclerView.Adapter<SkillsRecyclerViewAdapter.ViewHolder> {

    public static ArrayList<String> skillsNamesArrayList;
    private InsertSkillsClickListener mlistener;
    private boolean checked=false;


    public SkillsRecyclerViewAdapter() {
    }

    public SkillsRecyclerViewAdapter(Context context, ArrayList<Skills> skillsListData) {
        this.context = context;
        this.skillsListData = skillsListData;
    }





    private Context context;
    private ArrayList<Skills> skillsListData;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        skillsNamesArrayList=new ArrayList<>();

        //skillsNamesArrayList=new ArrayList<>();


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_skill_categories, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Skills ld = skillsListData.get(position);

        holder.tvSkill.setText(ld.getSkill());


        holder.crdViewSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //holder.crdViewSkills.getCardBackgroundColor().getDefaultColor()==context.getResources().getColor(R.color.buttoncolor
                //holder.crdViewSkills.getCardBackgroundColor().getDefaultColor()==context.getResources().getColor(R.color.white)
                //skillsNamesArrayList=new ArrayList<>();

                if(ld.isChecked()==true){

                    holder.crdViewSkills.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.tvSkill.setTextColor(context.getResources().getColor(R.color.black));
                    holder.imgSkill.setImageDrawable(context.getResources().getDrawable(R.drawable.add));
                    holder.imgSkill.setColorFilter(context.getResources().getColor(
                            R.color.black));

                    ld.setChecked(false);
                    skillsNamesArrayList.remove(ld.getSkill());

                   /* for(int i=0;i<skillsNamesArrayList.size();i++){

                        Toast.makeText(context.getApplicationContext(), ""+skillsNamesArrayList.get(i), Toast.LENGTH_SHORT).show();
                    }*/

                    /*Intent intent = new Intent("custom-arraylist");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putStringArrayListExtra("arraylist",skillsNamesArrayList);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/


                }else if(ld.isChecked()==false) {

                    holder.crdViewSkills.setCardBackgroundColor(context.getResources().getColor(R.color.buttoncolor));
                    holder.tvSkill.setTextColor(context.getResources().getColor(R.color.white));
                    holder.imgSkill.setImageDrawable(context.getResources().getDrawable(R.drawable.close_white));
                    holder.imgSkill.setColorFilter(context.getResources().getColor(
                            R.color.white));

                    checked=true;
                    ld.setChecked(true);
                    skillsNamesArrayList.add(ld.getSkill());

                   /* for(int i=0;i<skillsNamesArrayList.size();i++){

                        Toast.makeText(context.getApplicationContext(), ""+skillsNamesArrayList.get(i), Toast.LENGTH_SHORT).show();
                    }*/

                    /*Intent intent = new Intent("custom-arraylist");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putStringArrayListExtra("arraylist",skillsNamesArrayList);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/

                }


            }
        });




    }

    @Override
    public int getItemCount() {
        return skillsListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSkill;
        TextView tvSkill;
        CardView crdViewSkills;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSkill = itemView.findViewById(R.id.img_add_skill);
            tvSkill = itemView.findViewById(R.id.tv_skill);
            crdViewSkills = itemView.findViewById(R.id.parentlayoutskills);
        }
    }
}

