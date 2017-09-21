package baishuai.github.io.smsforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import baishuai.github.io.smsforward.ui.MainActivity;

/**
 * Created by dream on 2017/9/21.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        //这个必须添加flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
