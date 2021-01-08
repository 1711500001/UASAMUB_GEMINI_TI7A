package com.example.uas__amub_ti7a_01_07_10_11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Transfer> dataList;
    private Task<Void> reference;
    public TransferAdapter(ArrayList<Transfer> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transfer transfer = dataList.get(position);
        String key = dataList.get(position).getKey();
        holder.txtUser.setText(dataList.get(position).getUser());
        holder.txtJml.setText("Transfer "+dataList.get(position).getJml());
        holder.txtTgl.setText(dataList.get(position).getTgl());
        Picasso.with(context).load(dataList.get(position).getPhoto()).centerCrop().fit().into(holder.imageView);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeItem(transfer, key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void removeItem(Transfer transfer, String key) {
        int position = dataList.indexOf(transfer);
        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(dataList.get(position).getU()).child("transfer").child(key).removeValue();
        reference.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Removed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtUser, txtJml, txtTgl,del;
        private ImageView imageView;
        private CardView cdvw;
        public ViewHolder(View itemView) {
            super(itemView);
            cdvw = (CardView) itemView.findViewById(R.id.cdvw);
            txtUser = (TextView) itemView.findViewById(R.id.user);
            txtJml = (TextView) itemView.findViewById(R.id.jml);
            txtTgl = (TextView) itemView.findViewById(R.id.tgl);
            del = (TextView) itemView.findViewById(R.id.del);
            imageView = (ImageView) itemView.findViewById(R.id.profillist);
        }
    }
}
