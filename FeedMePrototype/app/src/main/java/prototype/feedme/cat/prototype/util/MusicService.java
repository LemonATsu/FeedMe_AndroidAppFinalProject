package prototype.feedme.cat.prototype.util;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import prototype.feedme.cat.prototype.R;

public class MusicService extends Service {
    private MediaPlayer player;
    private final static String datapath = "android.resource://" + "prototype.feedme.cat.prototype" + "/raw" + "/bgm";

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create( this, R.raw.bgm);
        player.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Destroy MusicService");
        if(player != null) {
            player.stop();
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        super.onStart(intent, startId);
        System.out.println("on StartCommand");
        player.start();
        return Service.START_STICKY_COMPATIBILITY;
    }
}
