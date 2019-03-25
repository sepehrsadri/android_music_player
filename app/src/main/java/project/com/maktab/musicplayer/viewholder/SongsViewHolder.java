package project.com.maktab.musicplayer.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.controller.PlayerActivity;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

public class SongsViewHolder extends BaseViewHolder {
    private CircleImageView mCoverIv;
    private TextView mSongTv;
    private TextView mArtistTv;
    private SongEntity mSong;
    private TextView mSongDuration;

    public SongsViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mCoverIv = itemView.findViewById(R.id.cover_image);
        mSongTv = itemView.findViewById(R.id.songs_name_tv);
        mArtistTv = itemView.findViewById(R.id.artist_name_tv);
        mSongDuration = itemView.findViewById(R.id.songs_duration_item);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayerActivity.newIntent(context, mSong.getSongId());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public void bind(Object obj) {
        mSong = (SongEntity) obj;
        Picasso.get().load(SongLab.generateUri(mSong.getAlbumId()))
                .placeholder(R.drawable.icon_malhaar5)
                .into(mCoverIv);
//            mCoverIv.setImageBitmap(SongLab.generateBitmap(getActivity(), song.getAlbumId()));
        mSongTv.setText(mSong.getTitle());
        mArtistTv.setText(mSong.getArtist());
        mSongDuration.setText(SongLab.convertDuration(mSong.getDuration()));
    }
}