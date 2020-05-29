package com.example.habitreminder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.R;
import com.example.habitreminder.habits.AddFrequency;
import com.example.habitreminder.habits.HabitSelectionActivityFrag;
import com.example.habitreminder.habits.Subhabits_Fragment;
import com.example.habitreminder.userhome.FragmentHome;

import java.util.List;

public class HomeScreen_Habits_Adapter extends RecyclerView.Adapter<HomeScreen_Habits_Adapter.Main_habitsVH> {
    Context mContx;
    List<HabitsData> mData;
    FragmentTransaction fragmentTransaction;
    public HomeScreen_Habits_Adapter() {
    }

    public HomeScreen_Habits_Adapter(Context mContx, List<HabitsData> mData) {
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
        if(position < 5) {
            holder.habits_title.setText(mData.get(position).getName());


            holder.habit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences companyId = ((FragmentActivity) mContx).getSharedPreferences("Name_main",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = companyId.edit();
                    editor.putString("name_main", mData.get(position).getName());
                    editor.putString("key_main", mData.get(position).getKey());
                    editor.commit();
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) mContx).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_habits, new AddFrequency());
                    fragmentTransaction.commit();
                }
            });
        } else {
            holder.habits_title.setText("More...");


            holder.habit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    SharedPreferences companyId = ((FragmentActivity) mContx).getSharedPreferences("Name_main",
//                            Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = companyId.edit();
//                    editor.putString("name_main", mData.get(position).getName());
//                    editor.putString("key_main", mData.get(position).getKey());
//                    editor.commit();
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) mContx).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_habits, new HabitSelectionActivityFrag());
                    fragmentTransaction.commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 6;
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
