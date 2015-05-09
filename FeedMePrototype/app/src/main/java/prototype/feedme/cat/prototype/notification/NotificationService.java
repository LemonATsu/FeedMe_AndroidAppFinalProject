package prototype.feedme.cat.prototype.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.activity.MainActivity;

/**
 * Created by User on 2014/12/29.
 */
public class NotificationService extends Service {
    private final int ID_My_Notification = 1;
    private NotificationManager notificationManager;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Toast.makeText(this, "onStarty", Toast.LENGTH_LONG).show();
        displayNotification();
        stopSelf();
    }

    public void displayNotification() {

        Intent intents = new Intent(getBaseContext(), MainActivity.class);
        intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getBaseContext(), 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager= (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Your cat is bothering you!")
                .setContentText("Feed me something~")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(ID_My_Notification, notification);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


}
