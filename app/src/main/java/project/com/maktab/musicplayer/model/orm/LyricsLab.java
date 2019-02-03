package project.com.maktab.musicplayer.model.orm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.com.maktab.musicplayer.controller.PlayerFragment;
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

    public List<Integer> getLyricsDuration(Long songId) {
        List<Integer> durationList = new ArrayList<>();
        List<Lyrics> lyricsList = mLyricsDao.queryBuilder()
                .where(LyricsDao.Properties.SongId.eq(songId))
                .list();
        if (lyricsList.size() <= 0)
            return null;
        for (Lyrics lyrics : lyricsList) {
            durationList.add(lyrics.getDuration());
        }


        return durationList;
    }


    public List<String> getLyricsText(Long songId) {
        List<String> stringList = new ArrayList<>();
        boolean isSingle = false;
        int listIndex = 0;
        String[] stringArray;
        List<Lyrics> lyricsList = mLyricsDao.queryBuilder()
                .where(LyricsDao.Properties.SongId.eq(songId))
                .list();
        if (lyricsList.size() > 0) {
            for (Lyrics lyrics : lyricsList) {
                stringList.add(lyrics.getText());
                if (lyrics.getDuration() == -1) {
                    isSingle = true;
                    listIndex =  lyricsList.indexOf(lyrics);
                    break;
                }
            }
            if (isSingle) {
                String allText = lyricsList.get(listIndex).getText();
                stringArray = new String[allText.length()];
                String delimiter = "\n";
                stringArray = allText.split(delimiter);
                stringList = Arrays.asList(stringArray);
                PlayerFragment.mLyricsStatus = false;
                return stringList;

            }
            return stringList;

        } else return null;
    }
}
