package project.com.maktab.musicplayer.model.orm;


import android.graphics.Bitmap;
import android.net.Uri;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Songs")
public class SongEntity {

    @Id(autoincrement = true)
    private Long id;
    @Transient
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Uri getArt() {
        return this.art;
    }

    public void setArt(Uri art) {
        this.art = art;
    }

    public Long getSongId() {
        return this.songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getPlaylistId() {
        return this.playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public boolean getFavourite() {
        return this.favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    private String artist;
    private String title;
    private String data;
    private Long artistId;
    private String albumName;

    private Long albumId;
    private int duration;
    @Convert(converter = UriConverter.class,columnType = String.class)
    private Uri art;
    private Long songId;

    private long playlistId;

    private boolean favourite;

    @Generated(hash = 1531959408)
    public SongEntity(Long id, String artist, String title, String data,
            Long artistId, String albumName, Long albumId, int duration, Uri art,
            Long songId, long playlistId, boolean favourite) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.data = data;
        this.artistId = artistId;
        this.albumName = albumName;
        this.albumId = albumId;
        this.duration = duration;
        this.art = art;
        this.songId = songId;
        this.playlistId = playlistId;
        this.favourite = favourite;
    }

    @Generated(hash = 274420887)
    public SongEntity() {
    }

    public static class UriConverter implements PropertyConverter<Uri, String> {

        @Override
        public Uri convertToEntityProperty(String databaseValue) {
            return Uri.parse(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(Uri entityProperty) {
            return entityProperty.toString();
        }
    }



}
