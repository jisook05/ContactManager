package com.example.contactphase1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
* jxk161230 cs4301.002 ContactPhase1
* */
public class ContactArrayAdapter extends RecyclerView.Adapter<ContactArrayAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<Contact> arr;
    Activity activity;

    static final int EDIT=1;

    public ContactArrayAdapter(Activity activity,Context context, ArrayList<Contact> arr) {
        this.activity=activity;
        this.context = context;
        this.arr = arr;
    }

    public ContactArrayAdapter(ArrayList<Contact> arr) {
        this.arr=arr;
    }

    @NonNull
    @Override
    public ContactArrayAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Connect view to recycler xml
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.general_contact, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactArrayAdapter.CustomViewHolder holder, final int position) {
        final Contact contact = arr.get(position);

        // Set Full name: Last, First
        holder.lname.setText(contact.getL_name());
        holder.fname.setText(contact.getF_name());

        // When contact is selected, move to detailed activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Position of the list item
                final int CurrentContact = position;
                //final int CurrentView = holder.getAdapterPosition();
                final int CurrentView = arr.indexOf(contact);
                Log.d("CUR))))", String.valueOf(CurrentView));

                Intent intent = new Intent(context, ContactDetail.class);
                intent.putExtra("Contact", arr.get(CurrentContact));
                intent.putExtra("CurrentView", CurrentView);
                intent.putExtra("editCode", EDIT);
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }




     class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView gen_proc_pic;
        TextView lname;
        TextView fname;

        // Connect views from layout
        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.gen_proc_pic = itemView.findViewById(R.id.gen_pro_pic);
            this.lname = itemView.findViewById(R.id.lname);
            this.fname = itemView.findViewById(R.id.fname);

        }

    }
}

