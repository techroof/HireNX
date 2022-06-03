package com.app.hirenx.IntroSlider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hirenx.Authentication.RegistrationTypeActivity;
import com.app.hirenx.ConsumerProfile.ClientHireSearchListActivity;
import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.app.hirenx.HomeActivity;
import com.app.hirenx.R;
import com.app.hirenx.ShowingIntroSlider.ShowingFirstIntroSlider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class IntroSliderActivity extends AppCompatActivity {

    private ViewPager mPager;
    public int[] layouts = {R.layout.firstslide, R.layout.secondslide, R.layout.thirdslide};
    private PagerAdapter pagerAdapter;
    private LinearLayout dots_Layout;
    private ImageView[] dots;
    private FloatingActionButton nextButton;
    private ShowingFirstIntroSlider sliderManager;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        /*sliderManager = new ShowingFirstIntroSlider(this);
        if (sliderManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }*/
        setContentView(R.layout.activity_intro_slider);

        nextButton=findViewById(R.id.next_floating_button);

        tvSkip=findViewById(R.id.tv_skip);

        Paper.init(this);

        Paper.book().write("checkOnboarding","yes");

        if (Build.VERSION.SDK_INT >= 19) {


            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {

                    Intent intent = new Intent(getApplicationContext(), RegistrationTypeActivity.class);
                    startActivity(intent);

                }
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegistrationTypeActivity.class);
                startActivity(intent);
            }
        });

        mPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new com.app.hirenx.Adapters.PagerAdapter(layouts, this);
        mPager.setAdapter(pagerAdapter);

        dots_Layout = (LinearLayout) findViewById(R.id.dotslayout);
        createdots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                createdots(position);
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createdots(int poss) {

        if (dots_Layout != null) {

            dots_Layout.removeAllViews();
            dots = new ImageView[layouts.length];
            for (int i = 0; i < layouts.length; i++) {
                dots[i] = new ImageView(this);
                if (i == poss) {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.introsliderline));
                } else {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.introsliderdot));

                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                dots_Layout.addView(dots[i], params);
            }
        }
    }

    private void launchHomeScreen() {
        sliderManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroSliderActivity.this, HomePageClientActivity.class));
        finish();
    }

    private int getItem(int i) { return mPager.getCurrentItem() + i;}

}