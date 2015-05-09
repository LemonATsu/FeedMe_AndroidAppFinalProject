package prototype.feedme.cat.prototype.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by User on 2014/12/29.
 */
public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service = new Intent(context,NotificationService.class);
        context.startService(service);

    }
}
