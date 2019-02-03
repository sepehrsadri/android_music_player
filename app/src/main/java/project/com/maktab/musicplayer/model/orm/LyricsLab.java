package project.com.maktab.musicplayer.model.orm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.com.maktab.musicplayer.database.App;

public class LyricsLab {
    private static LyricsLab mInstance;
    private DaoSession mDaoSession;
    private LyricsDao mLyricsDao;
    private List<String> mUnsynceLyrics;
    private List<String> mSynceLyrics;
    private List<Integer> mSynceDurationLyrics;

    public List<Integer> getSynceDurationLyrics() {
        return mSynceDurationLyrics;
    }

    public List<String> getUnsynceLyrics() {
        String[] mLyricsArray;
        String multiLines = mUnsynceLyrics.get(0);
        mLyricsArray = new String[multiLines.length()];
        String delimiter = "\n";
        mLyricsArray = multiLines.split(delimiter);
        mUnsynceLyrics = Arrays.asList(mLyricsArray);
        return mUnsynceLyrics;
    }

    public List<String> getSynceLyrics() {
        return mSynceLyrics;
    }


    private LyricsLab() {
        mDaoSession = App.getAppInstance().getDaoSession();
        mLyricsDao = mDaoSession.getLyricsDao();
        mUnsynceLyrics = new ArrayList<>();
        mSynceLyrics = new ArrayList<>();

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

    public void generateSyncedLyricsText(Long songId) {
        mSynceLyrics = new ArrayList<>();
        mSynceDurationLyrics = new ArrayList<>();
        List<Lyrics> query = mLyricsDao.queryBuilder()
                .where(LyricsDao.Properties.SongId.eq(songId))
                .orderAsc(LyricsDao.Properties.Duration)
                .list();
        for (Lyrics lyrics : query) {
            if (lyrics.getDuration() != -1) {
                mSynceLyrics.add(lyrics.getText());
                mSynceDurationLyrics.add(lyrics.getDuration());

            }
        }


    }

    public boolean hasLyricsText(Long songId) {
        List<Lyrics> lyricsList = mLyricsDao.queryBuilder()
                .where(LyricsDao.Properties.SongId.eq(songId))
                .list();
        return lyricsList.size() > 0;
    }

    /**
     * @param songId
     * @return true for Unsynce list and false for synce list
     */
    public boolean lyricsStatusAndGenerate(Long songId) {
        mUnsynceLyrics = new ArrayList<>();
        List<Lyrics> lyricsList = mLyricsDao.queryBuilder()
                .where(LyricsDao.Properties.SongId.eq(songId))
                .list();
        for (Lyrics lyrics : lyricsList) {
            if (lyrics.getDuration() == -1) {
                mUnsynceLyrics.add(lyrics.getText());
                break;

            }
        }
        if (lyricsList.size() <= 1)
            return true;
        generateSyncedLyricsText(songId);
        return false;


    }
}
