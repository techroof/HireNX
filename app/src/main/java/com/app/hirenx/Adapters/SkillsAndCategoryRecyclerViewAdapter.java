package com.app.hirenx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hirenx.Models.CategoryText;
import com.app.hirenx.Models.Skills;
import com.app.hirenx.R;

import java.util.ArrayList;

public class SkillsAndCategoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int categoryModel=0;
    private final int skillsModel=1;

    ArrayList<Object> objectArrayList;
    Context context;


    public SkillsAndCategoryRecyclerViewAdapter(ArrayList<Object> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {

        if(objectArrayList.get(position) instanceof CategoryText){

            return categoryModel;

        }
        else if( objectArrayList.get(position)instanceof Skills){

            return skillsModel;
        }
        else{

            return -1;
        }

  }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View modelView;

        RecyclerView.ViewHolder viewHolder=null;
        if(viewType==categoryModel){

            modelView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_category_text, parent, false);

            viewHolder=new CategoryTextViewHolder(modelView);


        }else if(viewType==skillsModel) {

            modelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_skill_categories, parent, false);

            viewHolder = new SKillsViewHolder(modelView);

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder.getItemViewType()==categoryModel){


            CategoryText ldCategoryText = (CategoryText) objectArrayList.get(position);
            CategoryTextViewHolder tvcategoryObjects=(CategoryTextViewHolder) holder;

            //tvcategoryObjects.tvCategory.setText(ldCategoryText.getCatetoryText());

        }else if(holder.getItemViewType()==skillsModel){

            Skills ldSkills = (Skills) objectArrayList.get(position);
            SKillsViewHolder tvSkillsObjects=(SKillsViewHolder) holder;

            tvSkillsObjects.tvSkill.setText(ldSkills.getSkill());

        }
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    public class CategoryTextViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory;

        public CategoryTextViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory=itemView.findViewById(R.id.tv_categories);
        }
}

    public class SKillsViewHolder extends  RecyclerView.ViewHolder{

        ImageView imgSkill;
        TextView tvSkill;
        CardView crdViewSkills;

        public SKillsViewHolder(@NonNull View itemView) {
            super(itemView);


            imgSkill = itemView.findViewById(R.id.img_add_skill);
            tvSkill = itemView.findViewById(R.id.tv_skill);
            crdViewSkills = itemView.findViewById(R.id.parentlayoutskills);
        }
    }

}
