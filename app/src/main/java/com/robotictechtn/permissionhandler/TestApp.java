package com.robotictechtn.permissionhandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class TestApp extends HandledPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add permissions
        ArrayList<String> locationPermissions = new ArrayList<>();
        locationPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        locationPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        AddPermissions(locationPermissions);
        AddPermission(Manifest.permission.BLUETOOTH);

        // Check permissions
        CheckPermissions();
    }

    public void btnCheckBT_onClick(View view) {
        if (GetPermissionStatus(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "BLUETOOTH PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "BLUETOOTH PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCheckCourse_onClick(View view) {
        if (GetPermissionStatus(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "COURSE LOCATION PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "COURSE LOCATION PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCheckFine_onClick(View view) {
        if (GetPermissionStatus(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "FINE LOCATION PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "FINE LOCATION PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }
}