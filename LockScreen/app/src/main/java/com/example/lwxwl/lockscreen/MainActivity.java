package com.example.lwxwl.lockscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button lock;
    private Button enable;
    private Button disable;

    static final int RESULT_ENABLE = 1;

    DevicePolicyManager devicePolicyManager;
    ActivityManager activityManager;
    ComponentName componentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);

        setContentView(R.layout.activity_main);

        lock = (Button) findViewById(R.id.btn_lock);
        enable = (Button) findViewById(R.id.btn_enable);
        disable = (Button) findViewById(R.id.btn_disable);

        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == lock) {
            boolean active = devicePolicyManager.isAdminActive(componentName);
            if (active) {
                devicePolicyManager.lockNow();
            }
        }

        if (view == enable) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Additional text explaining why this needs to be added.");
            startActivityForResult(intent, RESULT_ENABLE);
        }

        if (view == disable) {
            devicePolicyManager.removeActiveAdmin(componentName);
            updateButtonStates();
        }

    }

    private void updateButtonStates() {
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active) {
            enable.setEnabled(false);
            disable.setEnabled(true);
        } else {
            enable.setEnabled(true);
            disable.setEnabled(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enable!");
                } else {
                    Log.i("DeviceAdminSample", "Admin enable failed! ");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
