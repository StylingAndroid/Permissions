package com.stylingandroid.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};
    private PermissionsChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivity(this, PERMISSIONS);
    }
}
