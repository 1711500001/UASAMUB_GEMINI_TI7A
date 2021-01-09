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

public class MainActivity extends AppCompatActivity {
    ImageView ImageContainer;
    TextView rec, user,saldo;
    FloatingActionButton fab;
    EditText toUser,sandi,nominal;
    private RecyclerView recyclerView;
    private TransferAdapter adapter;
    public ArrayList<Transfer> transferArrayList;
    Integer photo_max = 1;
    Uri photo_location;
    String pass="";
    Intent it1;
    DatabaseReference reference;
    StorageReference storage;
    Locale locale = new Locale("en", "US");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        user = findViewById(R.id.usr);
        saldo = findViewById(R.id.sld);
        ImageContainer = findViewById(R.id.profil);
        pass = extras.getString("password");
        user.setText(extras.getString("user"));
        saldo.setText("Balance "+NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(Double.parseDouble(extras.getString("saldo"))));
        loadPhoto();
        addData();

        rec = findViewById(R.id.receive);
        it1 = new Intent(this, SecondActivity.class);
        rec.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(user.getText().toString()).child("account");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        it1.putExtra("user", snapshot.child("username").getValue().toString());
                        it1.putExtra("password", snapshot.child("password").getValue().toString());
                        it1.putExtra("saldo", snapshot.child("saldo").getValue().toString());
                        startActivity(it1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

    }
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.view_custom_dialog);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView cancelButton = dialog.findViewById(R.id.cancel);
        TextView processButton = dialog.findViewById(R.id.proses);
        toUser = dialog.findViewById(R.id.toUser);
        nominal = dialog.findViewById(R.id.nominal);
        sandi = dialog.findViewById(R.id.sandi);
        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerToUser();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Menampilkan custom dialog...
        dialog.show();
    }
    public void save(){
        //Proses Simpan ke Database Firebase
        int sal = (int) Double.parseDouble(((saldo.getText()+"").substring(9)).replace(",",""));
        if (!(nominal.getText() + "").isEmpty() && !(toUser.getText() + "").isEmpty() && !(sandi.getText() + "").isEmpty()) {
            if(sal>=Integer.parseInt(nominal.getText()+"")) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Date date = ts;
                String key = (date.toString()).replace(".", ":");
                Toast.makeText(getApplicationContext(), "Please, waiting....", Toast.LENGTH_LONG).show();
                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(user.getText().toString()).child("transfer").child(key);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            if (pass.equals(sandi.getText().toString())) {
                                ////simpan ke Transfer
                                Timestamp ts = new Timestamp(System.currentTimeMillis());
                                Date date = ts;
                                GregorianCalendar gcalendar = new GregorianCalendar();
                                snapshot.getRef().child("to").setValue(toUser.getText().toString());
                                snapshot.getRef().child("balance").setValue(nominal.getText().toString());
                                snapshot.getRef().child("time").setValue(gcalendar.get(Calendar.DATE) + "/" + gcalendar.get(Calendar.MONTH)
                                        + "/" + gcalendar.get(Calendar.YEAR) + " " + date.getHours() + ":" + date.getMinutes());

                                //simpan ke Receive
                                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(toUser.getText().toString()).child("receive").child(key);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Timestamp ts = new Timestamp(System.currentTimeMillis());
                                        Date date = ts;
                                        GregorianCalendar gcalendar = new GregorianCalendar();
                                        snapshot.getRef().child("from").setValue(user.getText().toString());
                                        snapshot.getRef().child("balance").setValue(nominal.getText().toString());
                                        snapshot.getRef().child("time").setValue(gcalendar.get(Calendar.DATE) + "/" + gcalendar.get(Calendar.MONTH)
                                                + "/" + gcalendar.get(Calendar.YEAR) + " " + date.getHours() + ":" + date.getMinutes());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //Edit Saldo user tujuan
                                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(toUser.getText().toString()).child("account");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int b = Integer.parseInt(snapshot.child("saldo").getValue().toString());
                                        int n = Integer.parseInt(nominal.getText().toString());
                                        snapshot.getRef().child("saldo").setValue((b + n) + "");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //Edit Saldo user saat ini
                                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(user.getText().toString()).child("account");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int b = Integer.parseInt(snapshot.child("saldo").getValue().toString());
                                        int n = Integer.parseInt(nominal.getText().toString());
                                        snapshot.getRef().child("saldo").setValue((b - n) + "");
                                        saldo.setText("Balance " + NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                                .format(Double.parseDouble((b - n) + "")));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Toast.makeText(getApplicationContext(), "Transfer success", Toast.LENGTH_SHORT).show();
                                addData();

                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Balance is not enough", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all!", Toast.LENGTH_SHORT).show();

        }
    }
    public void VerToUser(){
        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(toUser.getText().toString()).child("account");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(!toUser.getText().toString().equals(user.getText().toString())) {
                        save();
                    }else {
                        Toast.makeText(getApplicationContext(), "Don't transfer to your account!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Recipient not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addData(){
        transferArrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(user.getText().toString()).child("transfer");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String to = ds.child("to").getValue(String.class);
                    String bal = ds.child("balance").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference dateRef = storageRef.child("Photouser").child(to).child(to);
                    dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            transferArrayList.add(new Transfer(to, NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                    .format(Double.parseDouble(bal)), time, uri, ds.getKey(), user.getText().toString()));
                            recyclerView = findViewById(R.id.transfer_view);
                            adapter = new TransferAdapter(transferArrayList, getApplicationContext());
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
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
                                    transferArrayList.add(new Transfer(to, NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                            .format(Double.parseDouble(bal)), time, uri, ds.getKey(), user.getText().toString()));
                                    recyclerView = findViewById(R.id.transfer_view);
                                    adapter = new TransferAdapter(transferArrayList, getApplicationContext());
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
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

    public void img(View view) {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == photo_max && resultCode == RESULT_OK && data !=null && data.getData()!=null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(ImageContainer);
            storage = FirebaseStorage.getInstance().getReference().child("Photouser").child(user.getText().toString());
            if(photo_location != null){
                Toast.makeText(getApplicationContext(), "Please wait....", Toast.LENGTH_LONG).show();
                StorageReference storageReference = storage.child(user.getText().toString());
                storageReference.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(task -> {

                });
            }
        }
    }
    public void loadPhoto(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("Photouser").child(user.getText().toString()).child(user.getText().toString());
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).centerCrop().fit().into(ImageContainer);
                it1.putExtra("uri", uri.toString());
            }
        });

    }
}