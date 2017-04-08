package com.fiipractic.android.firebase.realtime_db;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.fiipractic.android.firebase.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dorunechifor.
 */
public class RealTimeDbActivity extends AppCompatActivity {
    
    private static final String TAG = "RealtimeDbActivity";
    
    @BindView(R.id.tv_realtime_db_details)
    TextView tvDetails;
    @BindView(R.id.et_realtime_db_name)
    EditText etName;
    @BindView(R.id.et_realtime_db_phone)
    EditText etPhone;
    @BindView(R.id.btn_realtime_db_save)
    Button btnSaveUpdate;
    
    private DatabaseReference mFirebaseDatabase;
    
    private String mContactId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_db);
        
        initViews();
        setViewActions();
    
        FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
        
        // get reference to 'contacts' node
        mFirebaseDatabase = firebaseInstance.getReference("contacts");
        
        // store app title to 'app_title' node
        firebaseInstance.getReference("app_title").setValue("Firebase RealTime DB");
        
        // app_title change listener
        firebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "App title updated");
                
                String appTitle = dataSnapshot.getValue(String.class);
                
                // update toolbar title
                //noinspection ConstantConditions
                getSupportActionBar().setTitle(appTitle);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        
        toggleButton();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
    }
    
    private void setViewActions() {
        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                
                // Check for already existed contactId
                if (TextUtils.isEmpty(mContactId)) {
                    createContact(name, phone);
                } else {
                    updateContact(name, phone);
                }
            }
        });
    }
    
    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(mContactId)) {
            btnSaveUpdate.setText(getString(R.string.action_save));
        } else {
            btnSaveUpdate.setText(getString(R.string.action_update));
        }
    }
    
    /**
     * Creating new contact node under 'contacts'
     */
    private void createContact(String name, String phone) {
        // In real apps this contactId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(mContactId)) {
            mContactId = mFirebaseDatabase.push().getKey();
        }
        
        Contact contact = new Contact(name, phone);
        
        mFirebaseDatabase.child(mContactId).setValue(contact);
        
        addContactChangeListener();
    }
    
    /**
     * Contact data change listener
     */
    private void addContactChangeListener() {
        mFirebaseDatabase.child(mContactId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                
                // Check for null
                if (contact == null) {
                    Log.e(TAG, "Contact data is null!");
                    return;
                }
                
                Log.e(TAG, "Contact data is changed!" + contact.name + ", " + contact.phone);
                
                // Display newly updated name and email
                tvDetails.setText(contact.name + ", " + contact.phone);
                
                // clear edit text
                etName.setText("");
                etPhone.setText("");
                
                toggleButton();
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read contact", error.toException());
            }
        });
    }
    
    private void updateContact(String name, String phone) {
        // updating the contact via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(mContactId).child("name").setValue(name);
        
        if (!TextUtils.isEmpty(phone))
            mFirebaseDatabase.child(mContactId).child("phone").setValue(phone);
    }
}
