package cz.cvut.fit.vorobvla.semestralka.refresh;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by vova on 5/5/15.
 */
public class WakeLockHelper {
    private static final String LOCK_NAME = "cz.cvut.fit.vorobvla.semestralka";
    private static PowerManager.WakeLock wakeLock = null;

    public static synchronized void acquire(Context context){
        if (wakeLock == null){
            wakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE))
                    .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME);
        }
        wakeLock.acquire();
    };

    public static synchronized void release(){
        if ((wakeLock != null)&& (wakeLock.isHeld())){
            wakeLock.release();
        }
    };
}
