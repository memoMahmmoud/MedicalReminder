package apps.mai.medicalreminder.alarm_medicine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import apps.mai.medicalreminder.AllMedicines;
import apps.mai.medicalreminder.R;
import apps.mai.medicalreminder.data.MedicineColumns;
import apps.mai.medicalreminder.data.MedicineProvider;

/**
 * Created by Mai_ on 25-Nov-16.
 */

public class RingtonePlayingService extends Service {
    private NotificationManager mNM;
    MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = intent.getIntExtra("med_id",0);
        Cursor cursor = getContentResolver().query(MedicineProvider.Medicines.MEDICINES,null,
                MedicineColumns._ID+"=?",new String[]{String.valueOf(id)},null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(MedicineColumns.MEDICINE_NAME));
        float doses = cursor.getFloat(cursor.getColumnIndex(MedicineColumns.NO_DOSAGE));
        Intent intentGoAppFromNotification = new Intent(this.getApplicationContext(),
                AllMedicines.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intentGoAppFromNotification,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this).setContentTitle
                ("Medical Reminder").setContentText("Medicine Name: "+name+"\n Take "+doses+"" +
                " Pills").setContentIntent
                (pendingIntent).setAutoCancel(true).setSmallIcon(R.mipmap.app_icon).build();
        mNM.notify(id,notification);

        mediaPlayer = MediaPlayer.create(this, R.raw.tone);
        mediaPlayer.start();
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        //mediaPlayer.setLooping(true);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        //mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
