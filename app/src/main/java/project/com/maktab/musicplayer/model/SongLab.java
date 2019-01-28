package project.com.maktab.musicplayer.model;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import project.com.maktab.musicplayer.ListSongs;

public class SongLab {
    private static SongLab mInstance;
    private List<Song> mSongList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;
    final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";


    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public Song getSong(Activity activity, Long id) {
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media._ID + "=" + String.valueOf(id);
        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);

        Song song;
        try {
            if (cursor.getCount() <= 0) return null;

            cursor.moveToFirst();
            String artistName = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            Long albumId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Bitmap bitmap = generateBitmap(activity, albumId);


            song = new Song();
            song.setArtist(artistName);
            song.setId(id);
            song.setTitle(track);
            song.setData(data);
            song.setDuration(duration);
            song.setBitmap(bitmap);
        } finally {
            cursor.close();
        }


        return song;
    }

    public List<Song> getSongListByArtist(Activity activity, Long artistiD) {

        List<Song> result = new ArrayList<>();
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media.ARTIST_ID + "=" + String.valueOf(artistiD);
        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);
        try {
            if (cursor.getCount() <= 0)
                return null;

            while (cursor.moveToNext()) {
                String artistName = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Bitmap bitmap = generateBitmap(activity, albumId);


                Song song = new Song();
                song.setArtist(artistName);
                song.setId(id);
                song.setTitle(track);
                song.setData(data);
                song.setDuration(duration);
                song.setBitmap(bitmap);

                result.add(song);
            }
        } finally {
            cursor.close();
        }


        return result;
    }

    public List<Song> getSongListByAlbum(Activity activity, Long albumId) {
        List<Song> result = new ArrayList<>();
/*        String where = MediaStore.Audio.Media.IS_MUSIC + "!= 0 " + " AND " + "cast(" +
                MediaStore.Audio.Media.ALBUM_ID + "as text) == " + String.valueOf(albumId);*/
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media.ALBUM_ID + "=" + String.valueOf(albumId);

        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);
        try {
            if (cursor.getCount() <= 0)
                return null;

            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String artist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Bitmap bitmap = generateBitmap(activity, albumId);


                Song song = new Song();
                song.setArtist(artist);
                song.setId(id);
                song.setBitmap(bitmap);
                song.setTitle(track);
                song.setData(data);
                song.setDuration(duration);
                song.setBitmap(bitmap);

                result.add(song);
            }
        } finally {
            cursor.close();
        }


        return result;
    }


    public List<Song> getSongList() {
     /*   List<Song> finalList = new ArrayList<>(new HashSet<>(mSongList));

        return finalList;*/
        return mSongList;
    }

    private SongLab() {
        mSongList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
    }

    public static SongLab getInstance() {
        if (mInstance == null)
            mInstance = new SongLab();
        return mInstance;
    }

    public int getSongIndex(Long id) {
        /*for(Song song:mSongList){
            if(song.getId().equals(id))
                return mSongList.indexOf(song);

        }*/
        for (int i = 0; i < mSongList.size(); i++) {
            if (mSongList.get(i).getId().equals(id))
                return i;
            break;
        }
        return -1;

    }


    public void init(Activity activity) {

        final Cursor cursor = activity.getContentResolver().query(uri,
                null, where, null, null);

        try {
            if (cursor.getCount() <= 0) return;

            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String artist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                int artistTracks = 0;
                int artistAlbums = 0;

           /*     int artistTracks = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST));
                int artistAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));*/
                Long artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));

                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Bitmap bitmap = generateBitmap(activity, albumId);

                generateArtistList(artist, artistTracks, artistAlbums, artistId, bitmap, albumId);

                generateAlbumList(artist, album, albumId, bitmap);


                generateSongList(id, artist, track, data, duration, bitmap);

            }
        } finally {
            cursor.close();
        }

    }

    private Bitmap generateBitmap(Activity activity, Long albumId) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bitmap bitmap = null;
        try {

            bitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), albumArtUri);
            /*if (bitmap != null)
                bitmap = Bitmap.createScaledBitmap(bitmap, point.x, point.y, true);*/

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
                  /*  bitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.audio_file);*/
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
    }

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    private void generateArtistList(String artist, int artistTracks, int artistAlbums, Long artistId, Bitmap bitmap, Long albumId) {
        Artist artistModel = new Artist();
        artistModel.setId(artistId);
        artistModel.setAlbums(artistAlbums);
        artistModel.setTracks(artistTracks);
        artistModel.setBitmap(bitmap);
        artistModel.setName(artist);
        artistModel.setAlbumId(albumId);

        mArtistList.add(artistModel);

    }

    private void generateAlbumList(String artist, String album, Long albumId, Bitmap bitmap) {
        Album albumModel = new Album();
        albumModel.setArtist(artist);
        albumModel.setBitmap(bitmap);
        albumModel.setTitle(album);
        albumModel.setId(albumId);

        mAlbumList.add(albumModel);


    }

    private void generateSongList(Long id, String artist, String track, String data, int duration, Bitmap bitmap) {
        Song song = new Song();
        song.setArtist(artist);
        song.setId(id);
        song.setTitle(track);
        song.setData(data);
        song.setDuration(duration);
        song.setBitmap(bitmap);

        mSongList.add(song);
    }
}
