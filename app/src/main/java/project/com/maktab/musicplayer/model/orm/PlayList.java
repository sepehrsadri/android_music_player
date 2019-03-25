package project.com.maktab.musicplayer.model.orm;


import android.net.Uri;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

import project.com.maktab.musicplayer.model.Song;


@Entity
public class PlayList {

    @Id(autoincrement = true)
    private Long id;

    private String name;


    private String image;

    @ToMany
    @JoinEntity(entity = JoinSongsWithPlaylist.class,sourceProperty = "playlistId",targetProperty = "songId")
    private List<SongEntity> songsWithThisPlaylist;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 472247056)
    private transient PlayListDao myDao;

    @Generated(hash = 254385661)
    public PlayList(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    @Generated(hash = 438209239)
    public PlayList() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 755591213)
    public List<SongEntity> getSongsWithThisPlaylist() {
        if (songsWithThisPlaylist == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongEntityDao targetDao = daoSession.getSongEntityDao();
            List<SongEntity> songsWithThisPlaylistNew = targetDao._queryPlayList_SongsWithThisPlaylist(id);
            synchronized (this) {
                if (songsWithThisPlaylist == null) {
                    songsWithThisPlaylist = songsWithThisPlaylistNew;
                }
            }
        }
        return songsWithThisPlaylist;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 552164143)
    public synchronized void resetSongsWithThisPlaylist() {
        songsWithThisPlaylist = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 469739525)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlayListDao() : null;
    }






}
