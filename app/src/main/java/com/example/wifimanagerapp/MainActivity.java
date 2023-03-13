package com.example.wifimanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ScanResult> wifiList;
    ListView wifi_list;
    ArrayList<String> results;
    ArrayAdapter wifiListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiList = new ArrayList<ScanResult>();
        results = new ArrayList<>();
        wifiListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        wifi_list = findViewById(R.id.wifi_list);
        wifi_list.setAdapter(wifiListAdapter);
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    updateList();
                } else {
                    Toast.makeText(c, "No jala", Toast.LENGTH_SHORT).show();
                }
            }
            @SuppressLint("MissingPermission")
            private void updateList(){
//                results = new ArrayList<>();
                wifiList = wifiManager.getScanResults();
                for(ScanResult result: wifiList){
                    if(results.stream().anyMatch(s -> s.equals(result.SSID.toString()))){
                        continue;
                    }
                    results.add(result.SSID.toString());
//                    Toast.makeText(MainActivity.this, result.SSID.toString(), Toast.LENGTH_SHORT).show();
                }
                wifiListAdapter.notifyDataSetChanged();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            Toast.makeText(this, "No jala 2", Toast.LENGTH_SHORT).show();
        }
    }
}