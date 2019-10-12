package com.csce4623.ahnelson.todolist;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                Toast.makeText(context, "Connection Lost! Network Disconnected", Toast.LENGTH_SHORT).show();
            } else {
               // Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}