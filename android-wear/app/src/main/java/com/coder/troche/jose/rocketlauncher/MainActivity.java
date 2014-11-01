package com.coder.troche.jose.rocketlauncher;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends Activity {

    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean D = true;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the Bluetooth service
    private BluetoothConnectionService mBluetoothService = null;

    private static final int REQUEST_ENABLE_BT = 3;
    private BluetoothDevice mSeeedBluetoothDevice = null;

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");


        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            if(D) Log.e(TAG, "Bluetooth is not available");
            finish();
            return;
        }

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        boolean seeedBluetoothFound = false;

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(D) Log.i(TAG, "Device: " + device.getName());

                if (device.getName().equals("SeeedBTSlave")) {
                    mSeeedBluetoothDevice = device;
                    seeedBluetoothFound = true;
                }
            }
        }
        if (!seeedBluetoothFound) {
            if(D) Log.e(TAG, "Seeed Bluetooth is not paired yet. Please pair it.");
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupBluetooth() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the Bluetooth connection
        } else {
            if (mBluetoothService == null) setupBluetooth();
        }

        connectDevice();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "++ ON RESUME ++");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "++ ON PAUSE ++");
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop the Bluetooth service
        if (mBluetoothService != null) mBluetoothService.stop();

        if (D) Log.e(TAG, "++ ON STOP ++");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) Log.e(TAG, "++ ON DESTROY ++");
    }

    private void setupBluetooth() {
        Log.d(TAG, "setupBluetooth()");

        // Initialize the BluetoothConnectionService to perform bluetooth connections
        mBluetoothService = new BluetoothConnectionService(this, mHandler);
    }

    // The Handler that gets information back from the BluetoothConnectionService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a Bluetooth connection
                    setupBluetooth();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    finish();
                }
        }
    }

    private void connectDevice() {
        // Attempt to connect to the device
        mBluetoothService.connect(mSeeedBluetoothDevice, false);
    }

}
