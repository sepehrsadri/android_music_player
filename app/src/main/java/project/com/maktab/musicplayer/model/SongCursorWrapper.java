package project.com.maktab.musicplayer.model;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class SongCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public SongCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Song getSong(Activity activity) {
        ;

        Long id = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        String artist = getString(
                getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String album = getString(
                getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        String track = getString(
                getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String data = getString(
                getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        Long albumId = getLong(
                getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

           /*     int artistTracks = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST));
                int artistAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));*/
        Long artistId = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
        int duration = getInt(
                getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), albumArtUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Song song = new Song();
        song.setArtist(artist);
        song.setId(id);
        song.setTitle(track);
        song.setData(data);
        song.setDuration(duration);
        song.setBitmap(bitmap);
        song.setAlbumId(albumId);
        song.setArtistId(artistId);
        song.setAlbumName(album);


        return song;
    }


}
