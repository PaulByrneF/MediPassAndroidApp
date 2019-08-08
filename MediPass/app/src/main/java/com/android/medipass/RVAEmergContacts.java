package com.android.medipass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RVAEmergContacts extends RecyclerView.Adapter<RVAEmergContacts.ViewHolder> {

    private static final String TAG = "RVAEmergContacts";
    private List<Contact> myContacts = new ArrayList<>();
    private Context context;

    public RVAEmergContacts(Context context, List<Contact> myContacts) {
        this.myContacts = myContacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contact, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.phNumberTV.setText(myContacts.get(position).getPhNumber());
        holder.cNameTV.setText(myContacts.get(position).getcName());

        Toast.makeText(context, myContacts.get(position).getPhNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return myContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView parentLayout;
        TextView phNumberTV, cNameTV;

        public ViewHolder(View itemView) {
            super(itemView);

            phNumberTV = itemView.findViewById(R.id.phNumberTV);
            cNameTV = itemView.findViewById(R.id.cNameTV);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
