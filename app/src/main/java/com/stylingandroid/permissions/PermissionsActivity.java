package com.stylingandroid.permissions;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "com.stylingandroid.permissions.EXTRA_PERMISSIONS";
    private static final String EXTRA_FINISH = "com.stylingandroid.permissions.EXTRA_FINISH";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionsChecker checker;
    private boolean requiresCheck;

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
        requiresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck) {
            String[] permissions = getPermissions();

            if (checker.lacksPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        } else {
            requiresCheck = true;
        }
    }

    private String[] getPermissions() {
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        if (permissions == null) {
            permissions = MainActivity.PERMISSIONS;
        }
        return permissions;
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void allPermissionsGranted() {
        if (shouldFinish()) {
            finish();
        } else {
            startMainActivity();
        }
    }

    private boolean shouldFinish() {
        Intent intent = getIntent();
        return intent.getBooleanExtra(EXTRA_FINISH, false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;
            showMissingPermissionDialog();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.help);
        dialogBuilder.setMessage(R.string.string_help_text);
        dialogBuilder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }

     private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
