package cz.cvut.fit.vorobvla.semestralka.refresh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakeToRefreshFeedsReciever extends BroadcastReceiver {
    public WakeToRefreshFeedsReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //going to refresh feeds, don't fall asleep
        WakeLockHelper.acquire(context);
        context.startService(new Intent(context, UpdateFeedsIntentService.class));
    }
}
