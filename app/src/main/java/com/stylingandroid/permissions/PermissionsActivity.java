package com.stylingandroid.permissions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    private static final String EXTRA_PERMISSIONS = "com.stylingandroid.permissions.EXTRA_PERMISSIONS";
    private PermissionsChecker checker;

    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
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
            allPermissionsGranted();
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    private void requestPermissions(String... permissions) {
        //NO-OP
    }

}
