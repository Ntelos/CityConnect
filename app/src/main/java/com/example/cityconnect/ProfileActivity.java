package com.example.cityconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView name, email, id;
    Button logout_button;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout_button = findViewById(R.id.logout_button);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        id = findViewById(R.id.profile_id);
        image = findViewById(R.id.profile_image);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
            id.setText(signInAccount.getId());
            Glide.with(this).load(String.valueOf(signInAccount.getPhotoUrl()))
                    .apply(new RequestOptions().override(600, 200)).into(image);
        }

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //terminate this activity
                finish();
            }
        });
    }
}