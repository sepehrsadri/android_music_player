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

    public SongEntity getSong(Long id) {
        List<SongEntity> result =mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.Id.eq(id))
                .list();
        if(result.size()>0)
            return result.get(0);


        return null;
    }

    public List<SongEntity> getFavSongList(){
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
        if(result.size()<=0)
            return null;


        return result;
    }

    public List<SongEntity> getSongListByAlbum(Long albumId) {
        List<SongEntity> result = mSongDao.queryBuilder()
                .where(SongEntityDao.Properties.AlbumId.eq(albumId))
                .list();
        if(result.size()<=0)
            return null;


        return result;
    }


    public List<SongEntity> getSongList() {

        return mSongList;
    }
    public void updateSong(SongEntity song){
        mSongDao.update(song);
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
        SharedPreferences.Editor editor = activity.getSharedPreferences(ViewPagerActivity.SONG_LOAD_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(ViewPagerActivity.IS_IN_DAO, true);
        editor.apply();
        return true;

    }

    public static Bitmap generateBitmap(Activity activity, Long albumId) {

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

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

    public List<SongEntity> getSearchList(String text) {
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
