package com.fiipractic.android.firebase.authenticate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.fiipractic.android.firebase.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dorunechifor.
 */
public class SignUpActivity extends AppCompatActivity {
    
    @BindView(R.id.btn_register_sign_in)
    Button btnLogin;
    @BindView(R.id.btn_register_sign_up)
    Button btnSignUp;
    @BindView(R.id.btn_register_reset_password)
    Button btnResetPassword;
    
    @BindView(R.id.et_register_email)
    EditText etEmail;
    @BindView(R.id.et_register_password)
    EditText etPassword;
    @BindView(R.id.pb_register_loading)
    ProgressBar pbLoading;
    
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        initViews();
        setViewActions();
        
        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
    }
    
    private void setViewActions() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
            }
        });
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }
    
    private void signUp() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        pbLoading.setVisibility(View.VISIBLE);
        
        //create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        pbLoading.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the mAuth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                            intent.putExtra(ProfileActivity.KEY_USERNAME, email);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
