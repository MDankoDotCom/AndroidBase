package com.mdanko.toolbox;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;

public class RuntimePermissions {
    abstract public static class Request {
        String[] permissions;
        public Request(String... permissions) {
            this.permissions = permissions;
        }
        abstract public void onGrant();
        abstract public void onDeny();
        abstract public void explainWhy(String permission);
    }

    private int trackingId;
    private SparseArray<Request> trackingRequests = new SparseArray<>();

    public void getPermission(Activity activity, Request req) {
        Log.i("MDanko", "calling getPermission");
        if (arePermissionsGranted(activity, req.permissions)) {
            req.onGrant();
        } else {
            for (String permission : req.permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                    req.explainWhy(permission);
            }
            trackingRequests.put(++trackingId, req);
            ActivityCompat.requestPermissions(activity, req.permissions, trackingId); // ask user for permissions
        }
    }

    // copy signature of Android 6.0 (M) Activity Permission Request notifier
    // each activity MUST call this method
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Request req = trackingRequests.get(requestCode);
        if (req == null)
            throw new RuntimeException("UNKNOWN requestCode ... ?");
        trackingRequests.remove(requestCode);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                req.onDeny(); // gracefully disable the feature
                return;
            }
        }
        req.onGrant(); // grant feature access
    }

    private static boolean arePermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }
}