package project.com.maktab.musicplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.ViewHolderTypes;
import project.com.maktab.musicplayer.model.Album;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.Song;
import project.com.maktab.musicplayer.model.orm.SongEntity;
import project.com.maktab.musicplayer.viewholder.AlbumViewHolder;
import project.com.maktab.musicplayer.viewholder.ArtistViewHolder;
import project.com.maktab.musicplayer.viewholder.BaseViewHolder;
import project.com.maktab.musicplayer.viewholder.SongsViewHolder;

public class RecyclerViewAdapter<M> extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<M> modelList;
    private ViewHolderTypes mViewHolderTypes;

    public void setModelList(List<M> modelList) {
        this.modelList = modelList;
    }

    public RecyclerViewAdapter(Context context, List<M> modelList, ViewHolderTypes viewholders) {
        mContext = context;
        mViewHolderTypes = viewholders;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        BaseViewHolder viewHolder = null;
        switch (mViewHolderTypes) {
            case SONG:
                view = inflater.inflate(R.layout.songs_list_item, viewGroup, false);
                viewHolder = new SongsViewHolder(view, mContext);
                break;
            case ALBUM:
                view = inflater.inflate(R.layout.album_list_item, viewGroup, false);
                viewHolder = new AlbumViewHolder(view, mContext);
                break;
            case ARTIST:
                view = inflater.inflate(R.layout.artist_list_item, viewGroup, false);
                viewHolder = new ArtistViewHolder(view, mContext);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        switch (mViewHolderTypes) {
            case SONG:
                SongEntity song = (SongEntity) modelList.get(i);
                baseViewHolder.bind(song);
                break;
            case ALBUM:
                Album album = (Album) modelList.get(i);
                baseViewHolder.bind(album);
                break;
            case ARTIST:
                Artist artist = (Artist) modelList.get(i);
                baseViewHolder.bind(artist);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
