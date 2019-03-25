package project.com.maktab.musicplayer.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.controller.ListSongs;
import project.com.maktab.musicplayer.model.Album;
import project.com.maktab.musicplayer.model.SongLab;

public class AlbumViewHolder extends BaseViewHolder {
    private ImageView mCoverIv;
    private TextView mArtistTv;
    private TextView mAlbumTv;
    private Album mAlbum;

    public AlbumViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mCoverIv = itemView.findViewById(R.id.album_item_cover_iv);
        mArtistTv = itemView.findViewById(R.id.album_item_artist_tv);
        mAlbumTv = itemView.findViewById(R.id.album_item_album_tv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ListSongs.newIntent(context, "album", mAlbum.getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public void bind(Object obj) {
        mAlbum = (Album) obj;
        Picasso.get().load(SongLab.generateUri(mAlbum.getId()))
                .placeholder(R.drawable.icon_malhaar5)
                .into(mCoverIv);
//            mCoverIv.setImageBitmap(SongLab.generateBitmap(getActivity(),album.getId()));
        mArtistTv.setText(mAlbum.getArtist());
        mAlbumTv.setText(mAlbum.getTitle());
    }
}
