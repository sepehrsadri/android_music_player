package project.com.maktab.musicplayer.model;

import android.app.Activity;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.com.maktab.musicplayer.controller.ViewPagerActivity;
import project.com.maktab.musicplayer.database.App;
import project.com.maktab.musicplayer.model.orm.DaoSession;
import project.com.maktab.musicplayer.model.orm.SongEntity;
import project.com.maktab.musicplayer.model.orm.SongEntityDao;

import static android.content.Context.MODE_PRIVATE;

public class SongLab {
    private static SongLab mInstance;
    private List<SongEntity> mSongList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;
    private DaoSession mDaoSession;
    private SongEntityDao mSongDao;


    final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";


    public List<Album> getAlbumList() {

        return mAlbumList;
    }

    public int getArtistSongsNumber(Long artistId) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.ArtistId.eq(artistId))
                .list();

        return result.size();
    }

    public SongEntity getSong(Long id) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.Id.eq(id))
                .list();
        if (result.size() > 0)
            return result.get(0);


        return null;
    }

    public List<SongEntity> getFavSongList() {
        List<SongEntity> result =
                mSongDao.queryBuilder()
                        .where(SongEntityDao.Properties.Favourite.eq(true))
                        .list();

        return result;
    }

    public List<SongEntity> getSongListByArtist(Long artistId) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.ArtistId.eq(artistId))
                .list();
        if (result.size() <= 0)
            return null;


        return result;
    }

    public List<SongEntity> getSongListByAlbum(Long albumId) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.AlbumId.eq(albumId))
                .list();
        if (result.size() <= 0)
            return null;


        return result;
    }


    public List<SongEntity> getSongList() {

        return mSongList;
    }

    public void updateSong(SongEntity song) {
        mSongDao.update(song);
    }

    public SongEntity getSongWithId(Long id) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.Id.eq(id))
                .list();
        if (result.size() > 0)
            return result.get(0);

        return null;
    }

    private SongLab() {
        mSongList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mDaoSession = App.getAppInstance().getDaoSession();
        mSongDao = mDaoSession.getSongEntityDao();


    }

    public static SongLab getInstance() {
        if (mInstance == null)
            mInstance = new SongLab();
        return mInstance;
    }


    public boolean initSongListFromDao() {
        mSongList = mSongDao.loadAll();
        generateAlbumList();
        generateArtistList();
        return true;
    }


    public boolean initSongList(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(ViewPagerActivity.SONG_LOAD_PREFS, MODE_PRIVATE);
        boolean isAuto = prefs.getBoolean(ViewPagerActivity.AUTO_START, false);
        if (isAuto)
            mSongDao.deleteAll();
        SongCursorWrapper cursorWrapper = new SongCursorWrapper(activity.getContentResolver().query(uri,
                null, where, null, null));

        try {
            if (cursorWrapper.getCount() <= 0) return true;

            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {

                Song song = cursorWrapper.getSong(activity);
                if (!containsSongName(song.getTitle())) {
                    SongEntity songEntity = new SongEntity();
                    songEntity.setData(song.getData());
                    songEntity.setAlbumName(song.getAlbumName());
                    songEntity.setArtistId(song.getArtistId());
                    songEntity.setArt(song.getArt());
                    songEntity.setDuration(song.getDuration());
                    songEntity.setArtist(song.getArtist());
                    songEntity.setSongId(song.getId());
                    songEntity.setTitle(song.getTitle());
                    songEntity.setAlbumId(song.getAlbumId());
                    songEntity.setBitmap(song.getBitmap());

                    mSongDao.insert(songEntity);
                }
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
            mSongList = mSongDao.loadAll();
        }
        initSongListFromDao();

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(ViewPagerActivity.IS_IN_DAO, true);
        if (isAuto)
            editor.putBoolean(ViewPagerActivity.AUTO_START, false);
        editor.apply();
        return true;

    }

    public static String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }

    public static Uri generateUri(Long albumId) {

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        return albumArtUri;
    }

    public static Bitmap generateBitmap(Activity activity, Long albumId) {
        Uri albumArtUri = generateUri(albumId);

        Bitmap bitmap = null;
        try {

            bitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), albumArtUri);
            /*if (bitmap != null)
                bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);*/

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
    }

    public List<Album> getSearchAlbumList(String text){
        List<Album> result  = new ArrayList<>();
        for(SongEntity song:mSongList){
            if(song.getAlbumName().toLowerCase().contains(text)){
                Album album = new Album();
                album.setTitle(song.getAlbumName());
                album.setArtist(song.getArtist());
                album.setId(song.getAlbumId());
                result.add(album);
            }
        }

        return result;
    }

    public List<Artist> getArtistSearchList(String text){
        List<Artist> result  = new ArrayList<>();
        for(SongEntity song:mSongList){
            if(song.getArtist().toLowerCase().contains(text)){
                Artist artist = new Artist();
                artist.setName(song.getArtist());
                artist.setAlbumId(song.getAlbumId());
                artist.setId(song.getArtistId());
                result.add(artist);
            }
        }

        return result;
    }
    public List<SongEntity> getSongSearchList(String text) {
        List<SongEntity> list = new ArrayList<>();

        text = text.toLowerCase();
        for (SongEntity item : mSongList) {
            if (item.getTitle().toLowerCase().contains(text)) {
                list.add(item);
            }
        }


        return list;
    }


    public List<Artist> getArtistList() {
        return mArtistList;
    }

    private void generateArtistList() {
        for (SongEntity song : mSongList) {
            Artist artist = new Artist();
            String artistName = song.getArtist();
            artist.setName(artistName);
            artist.setId(song.getArtistId());
            artist.setBitmap(song.getBitmap());
            artist.setAlbumId(song.getAlbumId());

            if (!containsNameArtist(artistName))
                mArtistList.add(artist);
        }
    }

    public boolean containsSongName(final String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return mSongList.stream().filter(o -> o.getTitle().equals(name)).findFirst().isPresent();
        }
        return false;
    }

    public boolean containsNameAlbum(final String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return mAlbumList.stream().filter(o -> o.getTitle().equals(name)).findFirst().isPresent();
        }
        return false;
    }

    public boolean containsNameArtist(final String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return mArtistList.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
        }
        return false;
    }

    private void generateAlbumList() {
        for (SongEntity song : mSongList) {
            Album albumModel = new Album();
            String albumName = song.getAlbumName();
            albumModel.setArtist(song.getArtist());
            albumModel.setBitmap(song.getBitmap());
            albumModel.setTitle(albumName);
            albumModel.setId(song.getAlbumId());
            if (!containsNameAlbum(albumName))
                mAlbumList.add(albumModel);
        }
    }

}
