package com.example.habitreminder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.R;

import java.util.List;

public class JournalAdapterHome extends RecyclerView.Adapter<JournalAdapterHome.ViewHolderJournal> {

    Context mContx;
    List<JournalData> mData;
    private  final  int limit = 2;

    public JournalAdapterHome() {
    }

    public JournalAdapterHome(Context mContx, List<JournalData> mData) {
        this.mContx = mContx;
        this.mData = mData;

        System.out.println(mData);
        System.out.println(mData.size());
    }


    @Override
    public ViewHolderJournal onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContx).inflate(R.layout.journal_recycler_view, null, false);
        return new ViewHolderJournal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJournal holder, int position) {
        JournalData journalData = mData.get(position);

        if(journalData.getjDescription().contains(" "))
            holder.jDescription.setText(journalData.getjDescription().split(" ")[0] + " ... ");
        else
            holder.jDescription.setText(journalData.getjDescription());

            holder.journalDate.setText(journalData.getDateJournal());

    }

    @Override
    public int getItemCount() {

//        if(mData.size() > limit){
//            return limit;
//        }
//        else {
            return mData.size();
//        }
    }

    public static class ViewHolderJournal extends RecyclerView.ViewHolder {
        public TextView jDescription;
        public TextView journalDate;



        public ViewHolderJournal(@NonNull View itemView) {
            super(itemView);

            jDescription= itemView.findViewById(R.id.tv_journal_description);
            journalDate = itemView.findViewById(R.id.journalDate);
        }

    }
}