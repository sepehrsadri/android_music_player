package project.com.maktab.musicplayer.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SongGreenDao {

    @Id(autoincrement = true)
    private Long id;

    private String path;

    private boolean favourite;

    @Generated(hash = 1514706729)
    public SongGreenDao(Long id, String path, boolean favourite) {
        this.id = id;
        this.path = path;
        this.favourite = favourite;
    }

    @Generated(hash = 130882466)
    public SongGreenDao() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getFavourite() {
        return this.favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }


}
