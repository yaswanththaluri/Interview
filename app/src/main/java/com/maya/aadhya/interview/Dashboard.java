package com.maya.aadhya.interview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    //ActionBarToggle for the Hamburger icon on the top left for that we need this ActionBarToggle
    ActionBarDrawerToggle mActionBarDrawerToggle;
    private TextView mTextView;
    private NavigationView mNavigationView;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        TextView name = findViewById(R.id.greetname);
        name.setText("Welcome, "+user.getDisplayName()+" !");

        mNavigationView = findViewById(R.id.navigation);
        View headerView = mNavigationView.getHeaderView(0);

        try {
            TextView t = headerView.findViewById(R.id.nameNav);
            t.setText(user.getDisplayName());

            ImageView v = headerView.findViewById(R.id.navImg);
            Glide.with(this).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(user.getPhotoUrl()).into(v);
        }catch (Exception e)
        {
            Toast.makeText(Dashboard.this, "Please Fill the Profile", Toast.LENGTH_SHORT).show();
        }

        ImageView v = findViewById(R.id.applyint);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this, ApplyInterview.class);
                startActivity(i);
            }
        });

        ImageView v2 = findViewById(R.id.applied);
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(Dashboard.this, PendingInterview.class);
                startActivity(i2);
            }
        });

        ImageView v3 = findViewById(R.id.completed);
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(Dashboard.this, CompletedInterview.class);
                startActivity(i3);
            }
        });




        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_set:
                        Intent i4 = new Intent(Dashboard.this, ApplyInterview.class);
                        startActivity(i4);
                        break;
                    case R.id.nav_applied:
                        Intent i2 = new Intent(Dashboard.this, PendingInterview.class);
                        startActivity(i2);
                        break;
                    case R.id.nav_completed:
                        mTextView.setText("Item 3 selected");
                        getSupportActionBar().setTitle("Item 3");
                        break;

                    case R.id.onlinetrainings:

                        break;

                    case R.id.nav_profile:
                        Intent i3 = new Intent(Dashboard.this, CompletedInterview.class);
                        startActivity(i3);
                        break;

                    case R.id.aboutus:

                        break;

                    case R.id.contactus:
                        
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        Intent i5 = new Intent(Dashboard.this, Authentication.class);
                        finish();
                        startActivity(i5);
                        break;
                }
                //close the drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;



            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(Dashboard.this, mDrawerLayout, R.string.opendrawrer, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
