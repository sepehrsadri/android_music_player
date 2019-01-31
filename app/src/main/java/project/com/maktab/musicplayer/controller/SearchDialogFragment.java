package project.com.maktab.musicplayer.controller;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.Song;
import project.com.maktab.musicplayer.model.SongEntity;
import project.com.maktab.musicplayer.model.SongLab;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private SearchRvAdapter mAdapter;
    private Toolbar mToolbar;

    public static SearchDialogFragment newInstance() {

        Bundle args = new Bundle();

        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_dialog, container, false);
        mRecyclerView = view.findViewById(R.id.search_fragment_recycler_view);
        mSearchView = view.findViewById(R.id.search_fragment_search_view);
        mToolbar = view.findViewById(R.id.search_fragment_bar);

        ((ViewPagerActivity) getActivity()).setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new SearchRvAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            List<SongEntity> list;

            @Override
            public boolean onQueryTextSubmit(String s) {
//                list = SongLab.getInstance().getSearchedSongList(getActivity(), s);
                list = SongLab.getInstance().getSearchList(s);
                mAdapter.setSongList(list);
                mAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                list = SongLab.getInstance().getSearchedSongList(getActivity(), s);
                list = SongLab.getInstance().getSearchList(s);
                mAdapter.setSongList(list);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return view;
    }

    private class SearchRvHolder extends RecyclerView.ViewHolder {
        private CircleImageView mCircleImageView;
        private TextView mSongTv;
        private TextView mArtistTv;
        private SongEntity mSong;

        public SearchRvHolder(@NonNull View itemView) {
            super(itemView);
            mCircleImageView = itemView.findViewById(R.id.cover_image);
            mSongTv = itemView.findViewById(R.id.songs_name_tv);
            mArtistTv = itemView.findViewById(R.id.artist_name_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PlayerActivity.newIntent(getActivity(), mSong.getId());
                    dismiss();
                    startActivity(intent);
                }
            });

        }

        public void bind(SongEntity song) {
            mSong = song;
            mCircleImageView.setImageBitmap(song.getBitmap());
            mSongTv.setText(song.getTitle());
            mArtistTv.setText(song.getArtist());
        }

    }

    private class SearchRvAdapter extends RecyclerView.Adapter<SearchRvHolder> {
        private List<SongEntity> mSongList;

        public void setSongList(List<SongEntity> songList) {
            mSongList = songList;
        }

        public SearchRvAdapter(List<SongEntity> songList) {
            mSongList = songList;
        }

        @NonNull
        @Override
        public SearchRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.songs_list_item, viewGroup, false);
            SearchRvHolder rvHolder = new SearchRvHolder(view);
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchRvHolder searchRvHolder, int i) {
            SongEntity song = mSongList.get(i);
            if (song != null)
                searchRvHolder.bind(song);

        }

        @Override
        public int getItemCount() {
            if (mSongList == null)
                return 0;
            else
                return mSongList.size();
        }
    }


}
