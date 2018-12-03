package mobile.ufc.br.novosispu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private int seconds = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Timer t = new Timer();
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            seconds = 0;
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    seconds++;
                }
            }, 0, 1000);
        } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            t.cancel();
            Toast.makeText(context, "Você esteve fora por " + seconds + " segundos", Toast.LENGTH_SHORT).show();
        }
    }
}
