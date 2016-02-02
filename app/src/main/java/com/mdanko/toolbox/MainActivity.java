package com.mdanko.toolbox;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private RuntimePermissions perms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        perms = new RuntimePermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAudioPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        perms.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupToolbar() {
    }

    private void getAudioPermission() {
        String[] AudioPermissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
        };
        perms.getPermission(this, new RuntimePermissions.Request(AudioPermissions) {
            @Override
            public void onGrant() {
                Log.i("MDanko", "audio permission granted");
            }

            @Override
            public void onDeny() {
                Log.i("MDanko", "audio permission denied");
            }

            @Override
            public void explainWhy(String permission) {
                Log.i("MDanko", "just because");
            }
        });
    }
}
