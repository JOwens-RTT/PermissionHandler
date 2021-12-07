package com.robotictechtn.permissionhandler;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Consumer;

/**
 * If you have an application that requires special permissions, then instead of extending Activity
 * or AppCompatActivity in your main activity; extend HandledPermissionsActivity. It provides the
 * functionality of extending AppCompatActivity but handles the permissions requests and statuses
 * automatically. In the onCreate method for the activity, use AddPermission() to identify permissions
 * your app needs, and then use CheckPermissions() to handle the checking and requesting feature
 * permissions. Afterwards use GetPermissionStatus() to enable or disable the features the app
 * uses based on the input the user has already provided.
 */
public abstract class HandledPermissionsActivity extends AppCompatActivity {
    private static final String TAG = "Permission Handler";
    private final int PERMISSION_REQUEST_CODE = 100;
    private ArrayList<String> permissions;
    private Dictionary<String, Integer> permissionCodes;
    private Dictionary<String, Integer> permissionStatus;
    private boolean permissionsChecked = false;

    public HandledPermissionsActivity() {
        permissionCodes = new Hashtable<>();
        permissions = new ArrayList<>();
        permissionStatus = new Hashtable<>();
    }

    /**
     * Add a permission ID to the list of permissions to check. Permissions cannot be added after
     * CheckPermissions() is called.
     *
     * @param permissionID Permission ID String. Sourced from Manifest.permission.{PERMISSION}
     */
    public void AddPermission(String permissionID){
        if (permissionsChecked) return; // Don't add permissions after checking the first time
        Log.d(TAG, "Adding " + permissionID);
        permissions.add(permissionID);
        permissionCodes.put(permissionID, permissions.size());
    }

    /**
     * Adds a list of permission IDs to the list of permissions to check. Permissions cannot be added
     * after CheckPermissions() is called.
     *
     * @param permissionIDs Permission ID Strings contained in any iterable array. ID Strings sourced
     *                      from Manifest.permission.{PERMISSION}
     */
    public void AddPermissions(Iterable<String> permissionIDs){
        if (permissionsChecked) return; // Don't add permissions after checking the first time
        permissionIDs.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Log.d(TAG, "Adding " + s);
                permissions.add(s);
                permissionCodes.put(s, permissions.size());
            }
        });
    }

    /**
     * Gets the current status for a requested permission. Use this to determine if certain functionality
     * in your application needs to be enabled/disabled based on permission status. Will only provide
     * a PackageManager.PERMISSION_DENIED response until CheckPermissions() is called.
     *
     * @param permissionID Permission ID String. Sourced from Manifest.permission.{PERMISSION}
     * @return Permission status. Returns either PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED
     */
    public int GetPermissionStatus(String permissionID) {
        Integer result = permissionStatus.get(permissionID);
        if (result == null) return PackageManager.PERMISSION_DENIED; // Assume permission is denied until proven otherwise
        return result;
    }

    /**
     * Checks and requests the permission to use the features you requested in AddPermission and
     * AddPermissions. No permission status is known until this function is called. This function
     * should only be called after all desired permissions are added. This function should only be
     * called once at the beginning of the app.
     */
    public void CheckPermissions(){
        Log.d(TAG, "Checking permissions...");
        // Check current permission status
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String permission : permissions) {
            permissionStatus.put(permission, ContextCompat.checkSelfPermission(getApplicationContext(), permission));
            if (permissionStatus.get(permission) == PackageManager.PERMISSION_DENIED) {
                neededPermissions.add(permission);
                Log.d(TAG, "Requesting permission for " + permission);
            }
            else{
                Log.d(TAG, permission + " already GRANTED");
            }
        }

        // Request permissions
        String[] temp = new String[neededPermissions.size()];
        neededPermissions.toArray(temp);
        if (temp != null && temp.length > 0)
            ActivityCompat.requestPermissions(this, temp, PERMISSION_REQUEST_CODE);
        permissionsChecked = true;
    }

    /**
     * Processes the permission request results. See: <href>https://developer.android.com/reference/androidx/core/app/ActivityCompat.OnRequestPermissionsResultCallback</href>
     * for details.
     *
     * @param requestCode Request identifier passed when the request was made
     * @param permissions String array of Permission IDs
     * @param grantResults Int array of Permission Statuses for each Permission ID
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE){
            for (int i = 0; i < permissions.length; i++){
                permissionStatus.put(permissions[i], grantResults[i]);
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, permissions[i] + " : GRANTED");
                }
                else {
                    Log.d(TAG, permissions[i] + " : DENIED");
                }
            }
        }
    }
}
