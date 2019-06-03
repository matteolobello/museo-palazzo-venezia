package it.matteolobello.palazzovenezia.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 505;

    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public static boolean hasPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                PermissionUtil.PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED;
    }
}
