package com.stylingandroid.permissions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS = "com.stylingandroid.permissions.EXTRA_PERMISSIONS";
    private static final String EXTRA_FINISH = "com.stylingandroid.permissions.EXTRA_FINISH";
    private PermissionsChecker checker;

    public static void startActivity(Activity activity, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_FINISH, true);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        checker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] permissions = getPermissions();

        if (checker.lacksPermissions(permissions)) {
            requestPermissions(permissions);
        } else {
            if (shouldFinish()) {
                finish();
            } else {
                startMainActivity();
            }
        }
    }

    private String[] getPermissions() {
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        if (permissions == null) {
            permissions = MainActivity.PERMISSIONS;
        }
        return permissions;
    }

    private boolean shouldFinish() {
        Intent intent = getIntent();
        return intent.getBooleanExtra(EXTRA_FINISH, false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }

    private void requestPermissions(String... permissions) {
        //NO-OP
    }

}
