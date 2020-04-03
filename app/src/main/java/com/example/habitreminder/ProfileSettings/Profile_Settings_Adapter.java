package com.example.habitreminder.ProfileSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.habitreminder.R;

import java.util.List;

public class Profile_Settings_Adapter extends RecyclerView.Adapter<Profile_Settings_Adapter.ViewHolder> {
    public ImageButton btn_back;
    private Context context;
    private List<Profile_Settings_Model> profileExploreList;
    private Button btn_update;
    private FragmentTransaction fragmentTransaction;

    public Profile_Settings_Adapter(Context context, List<Profile_Settings_Model> profileExploreList) {
        this.context = context;
        this.profileExploreList = profileExploreList;
        Log.i("profileExploreList", String.valueOf(profileExploreList.size()));

    }

    @NonNull
    @Override
    public Profile_Settings_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view_inflate = LayoutInflater.from(context).inflate(R.layout.fragment_profile_recycler_layout, parent, false);
        return new Profile_Settings_Adapter.ViewHolder(view_inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Profile_Settings_Adapter.ViewHolder holder, final int position) {
        holder.profile_button_name.setText(profileExploreList.get(position).getName());

        holder.profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                LayoutInflater inflater = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View view_popup = inflater.inflate(R.layout.popup_alert_dialog, null);
                alertDialog.setView(view_popup);
                final TextView txt_header1 = view_popup.findViewById(R.id.txt_header1);
                final TextView tv_info_description = view_popup.findViewById(R.id.tv_info_description);
                final ImageButton img_btn = view_popup.findViewById(R.id.image_button);
                txt_header1.setText(profileExploreList.get(position).getName());
                tv_info_description.setText(profileExploreList.get(position).getDescription());
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return profileExploreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView profile_button_name;
        public ImageButton profile_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_button_name = itemView.findViewById(R.id.profile_button_name);
            profile_button = itemView.findViewById(R.id.profile_button);
        }
    }
}
