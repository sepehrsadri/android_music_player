package project.com.maktab.musicplayer.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.ViewHolderTypes;
import project.com.maktab.musicplayer.adapter.RecyclerViewAdapter;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.SongLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistRecyclerFragment extends Fragment {
    private List<Artist> mArtistList;
    private RecyclerView mRv;
    private RecyclerViewAdapter mAdapter;


    public ArtistRecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtistList = SongLab.getInstance().getArtistList();

    }

    public static ArtistRecyclerFragment newInstance() {

        Bundle args = new Bundle();

        ArtistRecyclerFragment fragment = new ArtistRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        mRv = view.findViewById(R.id.recycler_view);

        mRv.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mAdapter =  new RecyclerViewAdapter<Artist>(getActivity(), mArtistList, ViewHolderTypes.ARTIST);

        mRv.setAdapter(mAdapter);


        return view;
    }






}
