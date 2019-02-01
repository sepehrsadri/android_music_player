package project.com.maktab.musicplayer.database;

import android.app.Application;

import org.greenrobot.greendao.database.Database;


import project.com.maktab.musicplayer.model.orm.DaoMaster;
import project.com.maktab.musicplayer.model.orm.DaoSession;

public class App extends Application {
    private static App mInstance;
    private DaoSession mDaoSession;

    public static App getAppInstance() {
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyDevOpenHelper myDevOpenHelper = new MyDevOpenHelper(this,"song");

        Database database = myDevOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();

        mInstance=this;

    }
}
