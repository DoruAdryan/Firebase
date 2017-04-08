package com.fiipractic.android.firebase.crash;

import com.google.firebase.crash.FirebaseCrash;

import com.fiipractic.android.firebase.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by dorunechifor.
 */
public class CrashActivity extends AppCompatActivity {
    private static final String TAG = "CrashActivity";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        
        // Checkbox to indicate when to catch the thrown exception.
        final CheckBox cbCatchCrash = (CheckBox) findViewById(R.id.cb_crash_catch);
        
        // Button that causes the NullPointerException to be thrown.
        Button crashButton = (Button) findViewById(R.id.btn_crash_cause);
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log that crash button was clicked. This version of Crash.log() will include the
                // message in the crash report as well as show the message in logcat.
                FirebaseCrash.logcat(Log.INFO, TAG, "Crash button clicked");
                
                // If checkbox is checked, catch the exception and report it using
                // Crash.report(). Otherwise throw the exception and let Firebase Crash automatically
                // report the crash.
                if (cbCatchCrash.isChecked()) {
                    try {
                        throw new NullPointerException();
                    } catch (NullPointerException ex) {
                        FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
                        FirebaseCrash.report(ex);
                    }
                } else {
                    throw new NullPointerException();
                }
            }
        });
        
        // Log that the Activity was created. This version of Crash.log() will include the message
        // in the crash report but will not be shown in logcat.
        // Log event
        FirebaseCrash.log("Activity created");
    }
}