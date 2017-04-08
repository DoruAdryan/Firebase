package com.fiipractic.android.firebase.authenticate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.fiipractic.android.firebase.R;

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
public class ResetPasswordActivity extends AppCompatActivity {
    
    @BindView(R.id.et_reset_passwd_email)
    EditText etEmail;
    @BindView(R.id.btn_reset_passwd_reset)
     Button btnReset;
    @BindView(R.id.pb_reset_passwd_loading)
    ProgressBar pbLoading;
    
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    
        initViews();
        setViewActions();
    
        mAuth = FirebaseAuth.getInstance();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
    }
    
    private void setViewActions() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    
    private void resetPassword() {
        final String email = etEmail.getText().toString().trim();
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
            return;
        }
        
        pbLoading.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        
                        pbLoading.setVisibility(View.GONE);
                    }
                });
    }
}
