package com.example.habitreminder.Calender;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.R;

import java.util.List;

public class journalList extends RecyclerView.Adapter<journalList.JournalViewHolder> {
    private List<String> data;
    public journalList(List<String> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.journal_items, parent,false);

        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        String description = data.get(position);
        holder.journalDescription.setText(description);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder{
        TextView journalDescription;
        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            journalDescription = itemView.findViewById(R.id.journal_description_item);
        }
    }

}
