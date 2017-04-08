package com.fiipractic.android.firebase.authenticate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.fiipractic.android.firebase.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    
    @BindView(R.id.btn_profile_remove_user)
    Button btnRemoveUser;
    @BindView(R.id.btn_profile_sign_out)
    Button btnSignOut;
    @BindView(R.id.pb_profile_loading)
    ProgressBar pbLoading;
    
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Set username
        final String username = getIntent().getStringExtra(KEY_USERNAME);
        ((TextView) findViewById(R.id.tv_profile_username)).setText(username);
        
        // Get firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        
        // Get current user
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();
                if (mCurrentUser == null) {
                    // user mAuth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        
        initViews();
        setViewActions();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
    }
    
    private void setViewActions() {
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    
    public void signOut() {
        mAuth.signOut();
    }
    
    private void deleteUser() {
        pbLoading.setVisibility(View.VISIBLE);
        if (mCurrentUser != null) {
            mCurrentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Your profile is deleted :( Create a new account now!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProfileActivity.this, SignUpActivity.class));
                                finish();
                                pbLoading.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                pbLoading.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        pbLoading.setVisibility(View.GONE);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
