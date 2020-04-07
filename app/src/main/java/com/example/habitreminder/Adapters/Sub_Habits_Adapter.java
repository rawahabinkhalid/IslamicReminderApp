package com.example.habitreminder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.R;
import com.example.habitreminder.habits.AddFrequency;
import com.example.habitreminder.habits.Subhabits_Fragment;

import java.util.List;

public class Sub_Habits_Adapter extends RecyclerView.Adapter<Sub_Habits_Adapter.Main_habitsVH> {
    Context mContx;
    List<SubHabits> mData;
    FragmentTransaction fragmentTransaction;
    public Sub_Habits_Adapter() {
    }

    public Sub_Habits_Adapter(Context mContx, List<SubHabits> mData) {
        this.mContx = mContx;
        this.mData = mData;
    }

    @Override
    public Main_habitsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContx).inflate(R.layout.sub_habits_layouts ,parent , false);
        return  new Main_habitsVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull Main_habitsVH holder, final int position) {
        SubHabits habitsData = mData.get(position);
//        if(habitsData.getName().contains(" "))
//            holder.sub_habits_title.setText(habitsData.getName().split(" ")[1] + " ... " );
//        else
            holder.sub_habits_title.setText(habitsData.getName());

       // holder.sub_habits_title.setText(habitsData.getName());
        holder.sub_habit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences companyId = ((FragmentActivity) mContx).getSharedPreferences("Name",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = companyId.edit();
                editor.putString("name", mData.get(position).getName());
                editor.commit();
                FragmentTransaction fragmentTransaction= ((FragmentActivity)mContx).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container_habits,new AddFrequency());
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class Main_habitsVH extends RecyclerView.ViewHolder {
        private TextView sub_habits_title;
        private LinearLayout sub_habit_layout;
        public Main_habitsVH(@NonNull View itemView) {
            super(itemView);
            sub_habits_title  = itemView.findViewById(R.id.sub_habits_title);
            sub_habit_layout = itemView.findViewById(R.id.sub_habit_layout);
        }

    }

}
