package project.com.maktab.musicplayer.viewholder;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.controller.ListSongs;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.SongLab;

public class ArtistViewHolder extends BaseViewHolder {
    private Artist mArtist;
    private ImageView mImageView;
    private TextView mArtistTv;
    private TextView mArtistSongsTv;
    private TextView mArtistAlbumsTv;
    private TextView mArtistSongsNumber;

    public ArtistViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.artist_item_cover);
        mArtistTv = itemView.findViewById(R.id.artist_item_name);
        mArtistSongsNumber = itemView.findViewById(R.id.artist_song_number_item);
            /*mArtistSongsTv = itemView.findViewById(R.id.artist_item_songs);
            mArtistAlbumsTv = itemView.findViewById(R.id.artist_item_albums);*/

        itemView.setOnClickListener(v -> {
            Intent intent = ListSongs.newIntent(context, "artist", mArtist.getId());
            context.startActivity(intent);
        });

    }


    @Override
    public void bind(Object obj) {
        mArtist = (Artist) obj;
        Picasso.get()
                .load(SongLab.generateUri(mArtist.getAlbumId()))
                    .placeholder(R.drawable.icon_malhaar5)
                .into(mImageView);
//            mImageView.setImageBitmap(SongLab.generateBitmap(getActivity(), artist.getAlbumId()));
        mArtistTv.setText(mArtist.getName());
        mArtistSongsNumber.setText(SongLab.getInstance().getArtistSongsNumber(mArtist.getId()) + " Songs ");
         /*   mArtistSongsTv.setText(artist.getTracks() + "");
            mArtistAlbumsTv.setText(artist.getAlbums() + "");
*/
    }
}
