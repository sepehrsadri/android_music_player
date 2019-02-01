package project.com.maktab.musicplayer.model.orm;

import java.util.ArrayList;
import java.util.List;

import project.com.maktab.musicplayer.database.App;

public class PlaylistLab {
    private static PlaylistLab mInstance;
    private DaoSession mDaoSession;
    private PlayListDao mPlayListDao;
    private List<PlayList> mPlayLists;

    private PlaylistLab() {
        mPlayLists = new ArrayList<>();
        mDaoSession = App.getAppInstance().getDaoSession();
        mPlayListDao = mDaoSession.getPlayListDao();
    }

    public void insert(PlayList playList) {
        List<PlayList> checkList = mPlayListDao.queryBuilder()
                .where(PlayListDao.Properties.Id.eq(playList.getId()))
                .list();
        if (checkList.size() <= 0)
            mPlayListDao.insert(playList);
        else
            mPlayListDao.update(playList);

    }
    public List<PlayList> getPlaylist(Long id){
        List<PlayList> result  = mPlayListDao.queryBuilder()
                .where(PlayListDao.Properties.Id.eq(id))
                .list();
        if(result.size()>0)
            return result;

        return null;
    }
    public List<PlayList> getAllList(){
        return mPlayListDao.loadAll();
    }

    public static PlaylistLab getmInstance() {
        if (mInstance == null)
            mInstance = new PlaylistLab();
        return mInstance;
    }
}
