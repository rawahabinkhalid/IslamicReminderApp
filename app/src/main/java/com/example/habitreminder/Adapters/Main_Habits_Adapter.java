package com.example.habitreminder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.R;
import com.example.habitreminder.habits.AddFrequency;
import com.example.habitreminder.habits.Subhabits_Fragment;

import java.util.List;
import java.util.Objects;

public class Main_Habits_Adapter extends RecyclerView.Adapter<Main_Habits_Adapter.Main_habitsVH> {
    Context mContx;
    List<String> mData;
    FragmentTransaction fragmentTransaction;
    public Main_Habits_Adapter() {
    }

    public Main_Habits_Adapter(Context mContx, List<String> mData) {
        this.mContx = mContx;
        this.mData = mData;
    }

    @Override
    public Main_habitsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContx).inflate(R.layout.main_habits_layouts ,parent , false);
        return  new Main_habitsVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull Main_habitsVH holder, final int position) {
        holder.habits_title.setText(mData.get(position));
        holder.habit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContx, "subchild", Toast.LENGTH_SHORT).show();
                SharedPreferences companyId = ((FragmentActivity) mContx).getSharedPreferences("Name",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = companyId.edit();
                editor.putString("name", mData.get(position));
                editor.commit();
                FragmentTransaction fragmentTransaction= ((FragmentActivity)mContx).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container_habits,new Subhabits_Fragment());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class Main_habitsVH extends RecyclerView.ViewHolder {
        private TextView habits_title;
        private LinearLayout habit_layout;
        public Main_habitsVH(@NonNull View itemView) {
            super(itemView);
            habits_title  = itemView.findViewById(R.id.habits_title);
            habit_layout = itemView.findViewById(R.id.habit_layout);
        }

    }

}
