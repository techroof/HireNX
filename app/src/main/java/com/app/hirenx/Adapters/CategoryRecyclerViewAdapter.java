package com.app.hirenx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.hirenx.Interfaces.InsertSkillsClickListener;
import com.app.hirenx.Models.CategoryText;
import com.app.hirenx.Models.Skills;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup3Activity;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    FirebaseFirestore firestore;
    ArrayList<Skills> skillsArrayList;


    public CategoryRecyclerViewAdapter(ArrayList<CategoryText> categoryListData, Context context) {
        this.categoryListData = categoryListData;
        this.context = context;
    }

    private ArrayList<CategoryText> categoryListData;
    private Context context;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        firestore=FirebaseFirestore.getInstance();
        skillsArrayList=new ArrayList<>();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_category_text, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InsertSkillsClickListener clickListener;

        CategoryText ld = categoryListData.get(position);

        holder.tvCategory.setText(ld.getCategoryName());

        firestore.collection("skills").whereEqualTo("categoryId",ld.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    skillsArrayList=new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        Skills listData = documentSnapshot.toObject(Skills.class);

                        skillsArrayList.add(listData);


                    }

                    SkillsRecyclerViewAdapter sKillsViewHolder=
                            new SkillsRecyclerViewAdapter(context.getApplicationContext(), skillsArrayList);
                    //holder.rvView.setLayoutManager(new GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false));
                    holder.rvView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
                    //holder.rvView.setNestedScrollingEnabled(true);
                    holder.rvView.setAdapter(sKillsViewHolder);


                    //sKillsViewHolder.notifyDataSetChanged();
                }else{


                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory;
        RecyclerView rvView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_categories);
            rvView = itemView.findViewById(R.id.rv_child_view);
        }
    }
}
