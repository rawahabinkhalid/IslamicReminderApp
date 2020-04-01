package com.example.habitreminder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {

    Context mContx;
    List<HabitsData> mData;

    public HabitsAdapter() {
    }

    public HabitsAdapter(Context mContx, List<HabitsData> mData) {
        this.mContx = mContx;
        this.mData = mData;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContx).inflate(R.layout.reminder_layout, parent, false);
    return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
       HabitsData habitsData =mData.get(position);

       holder.habit_title.setText(habitsData.getHabit_title());
       holder.time.setText(habitsData.getTime());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class  HabitViewHolder extends RecyclerView.ViewHolder{
      public TextView habit_title;
      public TextView time;

       public HabitViewHolder(@NonNull View itemView) {
           super(itemView);

           habit_title=itemView.findViewById(R.id.write_habits);
           time = itemView.findViewById(R.id.reminder_time);
       }
   }
}
