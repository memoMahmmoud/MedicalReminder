package apps.mai.medicalreminder.alarm_medicine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mai_ on 22-Nov-16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int i = intent.getIntExtra("med_id",0);
        //Toast.makeText(context.getApplicationContext(),""+i,Toast.LENGTH_SHORT).show();

        //create intent to start ringtone service
        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra("med_id",i);
        context.startService(serviceIntent);
    }
}
