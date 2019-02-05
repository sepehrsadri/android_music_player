package project.com.maktab.musicplayer.controller;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.PictureUtils;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.PlayList;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private TextView mFavoriteNumTv;
    private CardView mFavCardView;
    private PlaylistAdapter mAdapter;

    public static PlaylistFragment newInstance() {

        Bundle args = new Bundle();

        PlaylistFragment fragment = new PlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mFavoriteNumTv != null)
                mFavoriteNumTv.setText(SongLab.getInstance().getFavSongList().size() + "  " + " Tracks ");
            updateUI();
        }

    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlaylistAdapter(PlaylistLab.getmInstance().getAllList());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayLists(PlaylistLab.getmInstance().getAllList());
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_recyclerview, container, false);
        mFavoriteNumTv = view.findViewById(R.id.favorite_tracks_num_tv);
        mFavCardView = view.findViewById(R.id.fav_const_card_view);
        mRecyclerView = view.findViewById(R.id.play_list_recycler_view);
       /* LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);*/


        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        updateUI();

        mFavoriteNumTv.setText(SongLab.getInstance().getFavSongList().size() + "  " + " Tracks ");

        mFavCardView.setOnClickListener(v -> {
            Intent intent = ListSongs.newIntent(getActivity(), "fav", 0l);
            startActivity(intent);
        });


        return view;
    }

    private class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mCover;
        private TextView mName;
        private TextView mNumberOfsongs;
        private PlayList mPlayList;


        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            mCover = itemView.findViewById(R.id.play_list_cover_iv);
            mName = itemView.findViewById(R.id.play_list_name_tv);
            mNumberOfsongs = itemView.findViewById(R.id.play_list_songs_num_tv);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ListSongs.newIntent(getActivity(), "playlist", mPlayList.getId());
                    startActivity(intent);

                }
            });
        }

        public void bind(PlayList playList) {
            mPlayList = playList;
            mName.setText(playList.getName());
            if (playList.getImage() == null)
                mCover.setImageResource(R.drawable.icon_malhaar5);
            else {
                Bitmap bitmap = null;
                try {
                    bitmap = PictureUtils.decodeUri(getActivity(), Uri.parse(playList.getImage()));
                    mCover.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            String numOfSongs = String.valueOf(PlaylistLab.getmInstance().getSongList(playList.getId()).size()) + " ";
            mNumberOfsongs.setText(numOfSongs + "Songs");

        }
    }

    private class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
        private List<PlayList> mPlayLists = new ArrayList<>();

        public void setPlayLists(List<PlayList> playLists) {
            mPlayLists = playLists;
        }

        public PlaylistAdapter(List<PlayList> playLists) {
            mPlayLists = playLists;
        }

        @NonNull
        @Override
        public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.playlist_list_item_dialog, viewGroup, false);
            PlaylistViewHolder viewHolder = new PlaylistViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PlaylistViewHolder playlistViewHolder, int i) {
            PlayList playList = mPlayLists.get(i);
            playlistViewHolder.bind(playList);

        }

        @Override
        public int getItemCount() {
            return mPlayLists.size();
        }
    }


}
