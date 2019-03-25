package project.com.maktab.musicplayer.model.orm;

import java.util.ArrayList;
import java.util.List;

import project.com.maktab.musicplayer.database.App;

public class PlaylistLab {
    private static PlaylistLab mInstance;
    private DaoSession mDaoSession;
    private PlayListDao mPlayListDao;
    private List<PlayList> mPlayLists;
//    private JoinSongsWithPlaylistDao mJoinDao;

    private PlaylistLab() {
        mPlayLists = new ArrayList<>();
        mDaoSession = App.getAppInstance().getDaoSession();
        mPlayListDao = mDaoSession.getPlayListDao();
//        mJoinDao = mDaoSession.getJoinSongsWithPlaylistDao();
    }


    public void update(PlayList playList){
        mPlayListDao.update(playList);

    }

    public Long insert(PlayList playList) {
    /*    List<PlayList> checkList = mPlayListDao.queryBuilder()
                .where(PlayListDao.Properties.Id.eq(playList.getId()))
                .list();
        else
            mPlayListDao.update(playList);
        if (checkList.size() <= 0)*/
        Long insert = mPlayListDao.insert(playList);
        return insert;
    }

    public List<SongEntity> getSongList(Long playList) {
        List<PlayList> songList = mPlayListDao.queryBuilder()
                .where(PlayListDao.Properties.Id.eq(playList))
                .list();

        if (songList.size() > 0)
            return songList.get(0).getSongsWithThisPlaylist();


        return null;
    }

    public List<PlayList> getAllList() {
        return mPlayListDao.loadAll();
    }

    public static PlaylistLab getmInstance() {
        if (mInstance == null)
            mInstance = new PlaylistLab();
        return mInstance;
    }
}
