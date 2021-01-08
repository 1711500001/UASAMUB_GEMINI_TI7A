package com.example.uas__amub_ti7a_01_07_10_11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {
    ImageView ImageContainer,imgcon;
    TextView tra,userR,salR;
    String pass="",uri;
    private RecyclerView recyclerView;
    private ReceiveAdapter adapter;
    private ArrayList<Receive> receiveArrayList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle extras = getIntent().getExtras();
        tra = findViewById(R.id.transfer);
        userR = findViewById(R.id.userR);
        salR = findViewById(R.id.salR);
        ImageContainer = findViewById(R.id.profil);
        pass = extras.getString("password");
        uri = extras.getString("uri");
        userR.setText(extras.getString("user"));
        salR.setText("Balance "+ NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(Double.parseDouble(extras.getString("saldo"))));

        Picasso.with(getApplicationContext()).load(uri).centerCrop().fit().into(ImageContainer);
        addData();

        tra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SecondActivity.super.onBackPressed();
            }
        });
    }

    void addData(){
        receiveArrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(userR.getText().toString()).child("receive");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String from = ds.child("from").getValue(String.class);
                    String bal = ds.child("balance").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference dateRef = storageRef.child("Photouser").child(from).child(from);
                    dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            receiveArrayList.add(new Receive(from, NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                    .format(Double.parseDouble(bal)), time, uri, ds.getKey(), userR.getText().toString()));
                            recyclerView = (RecyclerView) findViewById(R.id.receive_view);
                            adapter = new ReceiveAdapter(receiveArrayList, getApplicationContext());
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SecondActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                    dateRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference dateRef = storageRef.child("Photouser").child("default").child("profile.png");
                            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    receiveArrayList.add(new Receive(from, NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                            .format(Double.parseDouble(bal)), time, uri, ds.getKey(), userR.getText().toString()));
                                    recyclerView = (RecyclerView) findViewById(R.id.receive_view);
                                    adapter = new ReceiveAdapter(receiveArrayList, getApplicationContext());
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SecondActivity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}