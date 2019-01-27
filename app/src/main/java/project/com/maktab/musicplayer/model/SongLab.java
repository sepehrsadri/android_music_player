package project.com.maktab.musicplayer.model;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongLab {
    private static SongLab mInstance;
    private List<Song> mSongList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;

    public List<Album> getAlbumList() {
        return mAlbumList;
    }



    public List<Song> getSongList() {
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


    public void init(Activity activity) {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        final Cursor cursor = activity.getContentResolver().query(uri,
                null, where, null, null);

        try {
            if (cursor == null) return;

            while (cursor.moveToNext()) {
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
                Long artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));

                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                Bitmap bitmap = null;
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(
                            activity.getContentResolver(), albumArtUri);
                    if (bitmap != null)
                        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);

                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                  /*  bitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.audio_file);*/
                } catch (IOException e) {

                    e.printStackTrace();
                }

                generateArtistList(artist, artistTracks, artistAlbums, artistId, bitmap);

                generateAlbumList(artist, album, albumId, bitmap);


                generateSongList(artist, track, data, duration, bitmap);

            }
        } finally {
            cursor.close();
        }

    }

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    private void generateArtistList(String artist, int artistTracks, int artistAlbums, Long artistId, Bitmap bitmap) {
        Artist artistModel  = new Artist();
        artistModel.setId(artistId);
        artistModel.setAlbums(artistAlbums);
        artistModel.setTracks(artistTracks);
        artistModel.setBitmap(bitmap);
        artistModel.setName(artist);

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

    private void generateSongList(String artist, String track, String data, int duration, Bitmap bitmap) {
        Song song = new Song();
        song.setArtist(artist);
        song.setBitmap(bitmap);
        song.setTitle(track);
        song.setData(data);
        song.setDuration(duration);
        song.setBitmap(bitmap);

        mSongList.add(song);
    }
}
