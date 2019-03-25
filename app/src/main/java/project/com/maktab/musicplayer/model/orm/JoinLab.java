package project.com.maktab.musicplayer.model.orm;

import project.com.maktab.musicplayer.database.App;

public class JoinLab {
    private static JoinLab mJoinInstance;
    private DaoSession mDaoSession;
    private JoinSongsWithPlaylistDao mJoinDao;

    private JoinLab() {
        mDaoSession = App.getAppInstance().getDaoSession();
        mJoinDao = mDaoSession.getJoinSongsWithPlaylistDao();
    }

    public static JoinLab getmJoinInstance() {
        if(mJoinInstance==null)
            mJoinInstance = new JoinLab();
        return mJoinInstance;
    }

    public void insertJoins (JoinSongsWithPlaylist join){
        mJoinDao.insert(join);
    }

}
