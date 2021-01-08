package com.example.uas__amub_ti7a_01_07_10_11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Activity_Login extends AppCompatActivity {
    private TextView fbook,acc,sin,sup;
    private EditText user,pswd;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sup = (TextView)findViewById(R.id.sup);
        sin = (TextView)findViewById(R.id.sin);
        fbook = (TextView)findViewById(R.id.fboook);
        acc = (TextView)findViewById(R.id.act);
        user = (EditText)findViewById(R.id.user);
        pswd = (EditText)findViewById(R.id.pswd);
        Intent it1 = new Intent(this, Activity_Signup.class);
        Intent it2 = new Intent(this, MainActivity.class);
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(it1);
            }
        });

        sin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sin.setEnabled(false);
                sin.setText("Loading...");
                sup.setEnabled(false);

                final String username = user.getText()+"";
                final String password = pswd.getText()+"";

                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username empty!", Toast.LENGTH_SHORT).show();
                    sin.setEnabled(true);
                    sin.setText("Sign In");
                }
                else {
                    if(password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Password empty!", Toast.LENGTH_SHORT).show();
                        sin.setEnabled(true);
                        sin.setText("Sign In");
                    }else{
                        reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(username).child("account");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String passwordFromFirebase = snapshot.child("password").getValue().toString();
                                    if (password.equals(passwordFromFirebase)){

                                        it2.putExtra("user", snapshot.child("username").getValue().toString());
                                        it2.putExtra("password", snapshot.child("password").getValue().toString());
                                        it2.putExtra("saldo", snapshot.child("saldo").getValue().toString());
                                        startActivity(it2);

                                        user.setText("");
                                        pswd.setText("");
                                        sin.setEnabled(true);
                                        sup.setEnabled(true);
                                        sin.setText("Sign In");
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                        sin.setEnabled(true);
                                        sup.setEnabled(true);
                                        sin.setText("Sign In");

                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Wrong Username", Toast.LENGTH_SHORT).show();
                                    sin.setEnabled(true);
                                    sup.setEnabled(true);
                                    sin.setText("Sign In");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
        });
    }
}