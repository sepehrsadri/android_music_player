package project.com.maktab.musicplayer.model.orm;

import project.com.maktab.musicplayer.database.App;

public class LyricsLab {
    private static LyricsLab mInstance;
    private DaoSession mDaoSession;
    private LyricsDao mLyricsDao;

    private LyricsLab() {
        mDaoSession = App.getAppInstance().getDaoSession();
        mLyricsDao = mDaoSession.getLyricsDao();

    }

    public void addLyric(Lyrics lyrics) {
        mLyricsDao.insert(lyrics);
    }

    public static LyricsLab getmInstance() {
        if (mInstance == null)
            mInstance = new LyricsLab();
        return mInstance;
    }
}
