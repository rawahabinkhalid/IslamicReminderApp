package com.example.habitreminder.OnboardingPackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.habitreminder.LoginSignup.LoginSignupActivity;
import com.example.habitreminder.R;

public class SliderAdapter extends PagerAdapter implements View.OnClickListener {

    private Context context, sliderContext;
    private OnboardPreferenceManager prefManager;

    SliderAdapter(Context context, OnboardingSlider sliderContext) {
        this.context = context;
        this.sliderContext = sliderContext;
        prefManager = new OnboardPreferenceManager(sliderContext);
    }

    private int[] slides_heading = {
            R.string.onboarding_reach_your_goals,
            R.string.onboarding_improve_collectively_heading,
            R.string.onboarding_grow_mind_heading
    };

    private int[] slides_desc = {
            R.string.onboarding_ajr_tracker,
            R.string.onboarding_together_cricle,
            R.string.onboarding_gratitude_journal
    };

    @Override
    public int getCount() {
        return slides_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { return view == object; }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_screen_1,container, false);
        ImageView slideImageView = view.findViewById(R.id.boarding_logo);
        TextView slideHeading = view.findViewById(R.id.main_heading);
        TextView slideDesc = view.findViewById(R.id.details);
        TextView dotOne = view.findViewById(R.id.active_dot_1);
        TextView dotTwo = view.findViewById(R.id.active_dot_2);
        TextView dotThree = view.findViewById(R.id.active_dot_3);
        Button begin = view.findViewById(R.id.begin);
        begin.setOnClickListener(this);
        slideImageView.setImageResource(R.drawable.undraw_mindfulness_s);
        slideHeading.setText(slides_heading[position]);
        slideDesc.setText(slides_desc[position]);
        if(position == 1) {
            dotOne.setBackgroundResource(R.drawable.slider_dot_inactive);
            dotTwo.setBackgroundResource(R.drawable.slider_dot_active);
            slideImageView.setImageResource(R.drawable.undraw_high_five_u36);
            begin.setVisibility(View.INVISIBLE);
        } else if( position == 2 ) {
            dotOne.setBackgroundResource(R.drawable.slider_dot_inactive);
            dotTwo.setBackgroundResource(R.drawable.slider_dot_inactive);
            dotThree.setBackgroundResource(R.drawable.slider_dot_active);
            slideImageView.setImageResource(R.drawable.undraw_hello_aeia_sy);
            begin.setVisibility(View.VISIBLE);
        } else {
            begin.setVisibility(View.INVISIBLE);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public void onClick(View view) {
        prefManager.setFirstTimeLaunch(false);
        sliderContext.startActivity(new Intent(sliderContext, LoginSignupActivity.class));
    }
}
