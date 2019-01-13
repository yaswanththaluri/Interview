package com.maya.aadhya.interview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileEdit extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        auth = FirebaseAuth.getInstance();
        if (auth!=null)
        {
            Toast.makeText(this, "User exixts", Toast.LENGTH_SHORT).show();
        }


        signout = (Button)findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                if (auth.getCurrentUser()==null)
                {
                    Intent i = new Intent(ProfileEdit.this, Authentication.class);
                    startActivity(i);
                }
            }
        });
    }



}
