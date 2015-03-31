package com.bestbuy.alwayson.receiver;

import com.bestbuy.alwayson.service.OnBootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Receiver is invoked whenever device boots up
 * 
 * @author VaibhavS
 *
 */
public class OnBootReceiver extends BroadcastReceiver {   

    @Override
    public void onReceive(Context context, Intent intent) {

     Intent myIntent = new Intent(context, OnBootService.class);
     context.startService(myIntent);
    }
}