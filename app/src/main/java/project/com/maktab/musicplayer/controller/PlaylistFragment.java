package project.com.maktab.musicplayer.controller;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private TextView mFavoriteNumTv;
    private ConstraintLayout mFavLayout;

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
            mFavoriteNumTv.setText(SongLab.getInstance().getFavSongList().size() + "Tracks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_recyclerview, container, false);
        mFavoriteNumTv = view.findViewById(R.id.favorite_tracks_num_tv);
        mFavLayout = view.findViewById(R.id.fav_const_layout);

        mFavoriteNumTv.setText(SongLab.getInstance().getFavSongList().size() + "Tracks");

        mFavLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ListSongs.newIntent(getActivity(), "fav", 0l);
                startActivity(intent);
            }
        });


        return view;
    }

}
