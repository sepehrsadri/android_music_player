package project.com.maktab.musicplayer.database;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.greenrobot.greendao.database.Database;

import project.com.maktab.musicplayer.model.orm.DaoMaster;
import project.com.maktab.musicplayer.model.orm.DaoSession;

public class App extends Application {
    private static App mInstance;
    private DaoSession mDaoSession;
    public final static String MUSIC_CHANEL_ID = "MUSIC_CHANNEL";

    public static App getAppInstance() {
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyDevOpenHelper myDevOpenHelper = new MyDevOpenHelper(this, "song");

        Database database = myDevOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = createAppNotificationChanel(MUSIC_CHANEL_ID, "Music Player",
                    "Music Player", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        mInstance = this;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createAppNotificationChanel(final String chanelId,
                                                            final String chanelName,
                                                            final String chanelDescription,
                                                            final int chanelImportance) {
        NotificationChannel channel = new NotificationChannel(chanelId, chanelName, chanelImportance);
        channel.setDescription(chanelDescription);
        return channel;
    }

}
