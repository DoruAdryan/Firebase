package com.fiipractic.android.firebase.realtime_db;

/**
 * Created by dorunechifor.
 */
public class Contact {
    public String name, phone;
    
    @SuppressWarnings("unused")
    public Contact() {
        // Default constructor required for calls to
        // DataSnapshot.getValue(Contact.class)
    }
    
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
