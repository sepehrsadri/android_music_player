package project.com.maktab.musicplayer.model.orm;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


@Entity
public class Lyrics {

    private String text;

    private long duration;

    @Id(autoincrement = true)
    private Long id;


    private Long songId;

    @ToOne(joinProperty = "songId")
    private SongEntity song;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1187789870)
    private transient LyricsDao myDao;

    @Generated(hash = 233993193)
    public Lyrics(String text, long duration, Long id, Long songId) {
        this.text = text;
        this.duration = duration;
        this.id = id;
        this.songId = songId;
    }

    @Generated(hash = 949589868)
    public Lyrics() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSongId() {
        return this.songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    @Generated(hash = 119665583)
    private transient Long song__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 450372155)
    public SongEntity getSong() {
        Long __key = this.songId;
        if (song__resolvedKey == null || !song__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongEntityDao targetDao = daoSession.getSongEntityDao();
            SongEntity songNew = targetDao.load(__key);
            synchronized (this) {
                song = songNew;
                song__resolvedKey = __key;
            }
        }
        return song;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1109099569)
    public void setSong(SongEntity song) {
        synchronized (this) {
            this.song = song;
            songId = song == null ? null : song.getId();
            song__resolvedKey = songId;
        }
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
    @Generated(hash = 2139248163)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLyricsDao() : null;
    }


}
