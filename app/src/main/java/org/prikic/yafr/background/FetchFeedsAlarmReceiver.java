package org.prikic.yafr.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FetchFeedsAlarmReceiver extends BroadcastReceiver{

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FetchFeedsService.class);
        context.startService(i);
    }
}
