package project.com.maktab.musicplayer.controller;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.ViewHolderTypes;
import project.com.maktab.musicplayer.adapter.RecyclerViewAdapter;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.Song;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsRecyclerFragment extends Fragment {
    RecyclerView mSongsRv;
    private List<SongEntity> mSongList;
    private static final String STATUS_ARGS = "status_args";
    private static final String ID_ARGS = "id_args";
    private RecyclerViewAdapter mAdapter;
    private String listPicker;
    private Long id;

    public static SongsRecyclerFragment newInstance(String status, Long id) {

        Bundle args = new Bundle();
        args.putString(STATUS_ARGS, status);
        args.putLong(ID_ARGS, id);
        SongsRecyclerFragment fragment = new SongsRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SongsRecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listPicker = getArguments().getString(STATUS_ARGS, "");
        id = getArguments().getLong(ID_ARGS, 0);
        mSongList = SongLab.getInstance().getSongList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.recycler_view, container, false);
        mSongsRv = view.findViewById(R.id.recycler_view);


        mSongsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (listPicker.equalsIgnoreCase("album"))
            mAdapter = new RecyclerViewAdapter<SongEntity>(getActivity(), SongLab.getInstance().getSongListByAlbum(id), ViewHolderTypes.SONG);
        else if (listPicker.equalsIgnoreCase("artist"))
            mAdapter = new RecyclerViewAdapter<SongEntity>(getActivity(), SongLab.getInstance().getSongListByArtist(id), ViewHolderTypes.SONG);
        else if (listPicker.equalsIgnoreCase("fav"))
            mAdapter = new RecyclerViewAdapter<SongEntity>(getActivity(), SongLab.getInstance().getFavSongList(), ViewHolderTypes.SONG);
        else if (listPicker.equalsIgnoreCase("playlist"))
            mAdapter = new RecyclerViewAdapter<SongEntity>(getActivity(), PlaylistLab.getmInstance().getSongList(id), ViewHolderTypes.SONG);

        else
            mAdapter = new RecyclerViewAdapter<SongEntity>(getActivity(), mSongList, ViewHolderTypes.SONG);

        mSongsRv.setHasFixedSize(true);

        mSongsRv.setAdapter(mAdapter);


        return view;
    }





}
