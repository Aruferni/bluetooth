package com.example.albertfernie.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    HashMap<String, String> hashMac = new HashMap<>();
    public static BluetoothSocket socket = null;
    //private String addres = "F4:0E:22:AC:48:FC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBTState();
        //Server lines
        AcceptThread act = new AcceptThread(mBluetoothAdapter);
        act.run();
        try {
            act.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Client lines
        /*ConnectThread cnt = new ConnectThread(mBluetoothAdapter);
        cnt.run();*/
    }

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBluetoothAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d("bt", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    hashMac.put(device.getName(), device.getAddress());
                }
            }
        }

    }

}
