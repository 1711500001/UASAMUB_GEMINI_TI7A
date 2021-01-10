package com.example.uas__amub_ti7a_01_07_10_11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PulsaActivity extends AppCompatActivity {
    EditText toUser, nominal, sandi;
    TextView cancel, proses, user, saldo;

    DatabaseReference reference;


    /*DatabaseReference reference;
    StorageReference storage;
    Locale locale = new Locale("en", "US");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulsa);
        /*user = findViewById(R.id.usr);
        saldo = findViewById(R.id.sld);
        nominal = findViewById(R.id.nominal);
        sandi = findViewById(R.id.sandi);

        Bundle extras = getIntent().getExtras();

        cancel = findViewById(R.id.cancel);
        proses = findViewById(R.id.proses);




        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(user.getText().toString()).child("account");
        *//*reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //user.setText(snapshot.child("username").getValue().toString());
                user.setText(snapshot.child(""));

                saldo.setText(snapshot.child("balance").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*//*

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(PulsaActivity.this, MainActivity.class);
                startActivity(goback);
            }
        });


    }*/
    }
}


