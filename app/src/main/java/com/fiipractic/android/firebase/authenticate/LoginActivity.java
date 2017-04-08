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
public class LoginActivity extends AppCompatActivity {
    
    @BindView(R.id.btn_login_sign_in)
    Button btnLogin;
    @BindView(R.id.btn_login_sign_up)
    Button btnSignUp;
    @BindView(R.id.btn_login_reset_password)
    Button btnResetPassword;
    @BindView(R.id.et_login_email)
    EditText etEmail;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.pb_login_loading)
    ProgressBar pbLoading;
    
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        
        if (mAuth.getCurrentUser() != null) {
            final Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.KEY_USERNAME, mAuth.getCurrentUser().getEmail());
            startActivity(intent);
            finish();
        }
        
        // set the view now
        setContentView(R.layout.activity_login);
        
        initViews();
        setViewActions();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
    }
    
    private void setViewActions() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    
    private void signIn() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        pbLoading.setVisibility(View.VISIBLE);
        
        // Authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        pbLoading.setVisibility(View.GONE);
                        
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                etPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra(ProfileActivity.KEY_USERNAME, email);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
