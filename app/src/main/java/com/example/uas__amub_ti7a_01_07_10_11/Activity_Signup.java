package com.example.uas__amub_ti7a_01_07_10_11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Activity_Signup extends AppCompatActivity {
    private TextView sup,act,sin,fbook;
    private EditText usrusr,pswd,mail;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sup = (TextView)findViewById(R.id.sup);
        sin = (TextView)findViewById(R.id.sin);
        fbook = (TextView)findViewById(R.id.fboook);
        act = (TextView)findViewById(R.id.act);
        mail = (EditText)findViewById(R.id.mal);
        pswd = (EditText)findViewById(R.id.pswd);
        usrusr = (EditText)findViewById(R.id.usrusr);
        Intent it = new Intent(this, Activity_Login.class);

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Signup.super.onBackPressed();
            }
        });

        act.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Proses Simpan ke Database Firebase
                if(!(mail.getText()+"").isEmpty() && !(usrusr.getText()+"").isEmpty() && !(pswd.getText()+"").isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, waiting....", Toast.LENGTH_LONG).show();
                    reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(usrusr.getText().toString()).child("account");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                snapshot.getRef().child("username").setValue(usrusr.getText().toString());
                                snapshot.getRef().child("password").setValue(pswd.getText().toString());
                                snapshot.getRef().child("email").setValue(mail.getText().toString());
                                snapshot.getRef().child("saldo").setValue("100000");
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                startActivity(it);
                            } else {
                                usrusr.setText("");
                                pswd.setText("");
                                Toast.makeText(getApplicationContext(), "Username is already!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Fill all!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}