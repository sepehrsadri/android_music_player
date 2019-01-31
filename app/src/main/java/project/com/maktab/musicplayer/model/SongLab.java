package project.com.maktab.musicplayer.model;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
    final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";


    public List<Album> getAlbumList() {
        generateAlbumList();
        return mAlbumList;
    }

    public Song getSong(Long id) {
        for (Song song : mSongList) {
            if (song.getId().equals(id))
                return song;
        }

        return null;
    }

    public List<Song> getSongListByArtist(Long artistId) {
        List<Song> result = new ArrayList<>();
        for (Song song : mSongList) {
            if (song.getArtistId().equals(artistId))
                result.add(song);
        }
        return result;
    }

    public List<Song> getSongListByAlbum(Long albumId) {
        List<Song> result = new ArrayList<>();
        for (Song song : mSongList) {
            if (song.getAlbumId().equals(albumId))
                result.add(song);
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



    public boolean init(Activity activity) {

        SongCursorWrapper cursorWrapper = new SongCursorWrapper(activity.getContentResolver().query(uri,
                null, where, null, null));

        try {
            if (cursorWrapper.getCount() <= 0) return true;

            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {

                mSongList.add(cursorWrapper.getSong(activity));


//                Bitmap bitmap = generateBitmap(activity, albumId);

                cursorWrapper.moveToNext();
         /*       generateArtistList(artist, artistId, bitmap, albumId);

                generateAlbumList(artist, album, albumId, bitmap);


                generateSongList(id, artist, track, data, duration, bitmap, artistId, albumId);
*/
            }
        } finally {
            cursorWrapper.close();
        }
        return true;

    }

    private Bitmap generateBitmap(Activity activity, Long albumId) {
      /*  Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);*/
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
        return bitmap;
    }

    public List<Song> getSearchList(String text) {
        List<Song> list = new ArrayList<>();

        text = text.toLowerCase();
        for (Song item : mSongList) {
            if (item.getTitle().toLowerCase().contains(text)) {
                list.add(item);
            }
        }


        return list;
    }


    public List<Artist> getArtistList() {
        generateArtistList();
        return mArtistList;
    }

    private void generateArtistList() {
        for (Song song : mSongList) {
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
        for (Song song : mSongList) {
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

  /*  private void generateSongList(Long id, String artist, String track, String data, int duration, Bitmap bitmap, Long artistId, Long albumId) {
        Song song = new Song();
        song.setArtist(artist);
        song.setId(id);
        song.setTitle(track);
        song.setData(data);
        song.setDuration(duration);
        song.setBitmap(bitmap);
        song.setAlbumId(albumId);
        song.setArtistId(artistId);
        mSongList.add(song);
    }*/


}
