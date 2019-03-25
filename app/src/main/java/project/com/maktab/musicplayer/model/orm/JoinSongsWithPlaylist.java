package project.com.maktab.musicplayer.model.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinSongsWithPlaylist {

    @Id(autoincrement = true)
    private Long id;

    private long songId;

    private long playlistId;

    @Generated(hash = 435448097)
    public JoinSongsWithPlaylist(Long id, long songId, long playlistId) {
        this.id = id;
        this.songId = songId;
        this.playlistId = playlistId;
    }

    @Generated(hash = 265449799)
    public JoinSongsWithPlaylist() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getSongId() {
        return this.songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getPlaylistId() {
        return this.playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }




}
