package com.app.hirenx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    private int[] layout;
    private LayoutInflater layoutInflater;
    private Context context;

    public PagerAdapter(int[] layout, Context context){

        this.layout=layout;
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=layoutInflater.inflate(layout[position],container,false);
        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        View view=(View)object;
        container.removeView(view);

    }
}
