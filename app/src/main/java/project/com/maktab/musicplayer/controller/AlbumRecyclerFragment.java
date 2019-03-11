package project.com.maktab.musicplayer.controller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.ViewHolderTypes;
import project.com.maktab.musicplayer.adapter.RecyclerViewAdapter;
import project.com.maktab.musicplayer.model.Album;
import project.com.maktab.musicplayer.model.SongLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumRecyclerFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Album> mAlbumList;
    private RecyclerViewAdapter mAdapter;

    public AlbumRecyclerFragment() {
        // Required empty public constructor
    }

    public static AlbumRecyclerFragment newInstance() {

        Bundle args = new Bundle();

        AlbumRecyclerFragment fragment = new AlbumRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbumList = SongLab.getInstance().getAlbumList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

//        mAdapter = new RecyclerViewAdapter(mAlbumList);
        mAdapter = new RecyclerViewAdapter<Album>(getActivity(), mAlbumList, ViewHolderTypes.ALBUM);

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
}
