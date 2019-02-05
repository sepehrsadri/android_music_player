package project.com.maktab.musicplayer.controller;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

import com.squareup.picasso.Picasso;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.Album;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private SectionSearchAdapter mAdapter;
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

        mAdapter = new SectionSearchAdapter(null, null, null);
        mRecyclerView.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            List<SongEntity> list;
            List<Album> mAlbumList;
            List<Artist> mArtistList;

            @Override
            public boolean onQueryTextSubmit(String s) {
//                list = SongLab.getInstance().getSearchedSongList(getActivity(), s);
                list = SongLab.getInstance().getSongSearchList(s);
                mAlbumList = SongLab.getInstance().getSearchAlbumList(s);
                mArtistList = SongLab.getInstance().getArtistSearchList(s);
                mAdapter.setSongList(list);
                mAdapter.setAlbumList(mAlbumList);
                mAdapter.setArtistList(mArtistList);
                mAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                list = SongLab.getInstance().getSearchedSongList(getActivity(), s);
                list = SongLab.getInstance().getSongSearchList(s);
                mAlbumList = SongLab.getInstance().getSearchAlbumList(s);
                mArtistList = SongLab.getInstance().getArtistSearchList(s);
                mAdapter.setSongList(list);
                mAdapter.setAlbumList(mAlbumList);
                mAdapter.setArtistList(mArtistList);
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
        private Album mAlbum;
        private Artist mArtist;
        private TextView mSongDuration;

        public SearchRvHolder(@NonNull View itemView) {
            super(itemView);
            mCircleImageView = itemView.findViewById(R.id.cover_image);
            mSongTv = itemView.findViewById(R.id.songs_name_tv);
            mArtistTv = itemView.findViewById(R.id.artist_name_tv);
            mSongDuration = itemView.findViewById(R.id.songs_duration_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PlayerActivity.newIntent(getActivity(), mSong.getSongId());
                    dismiss();
                    startActivity(intent);
                }
            });

        }

        public void bind(Album album) {
            mAlbum = album;
            Picasso.get().load(SongLab.generateUri(album.getId())).into(mCircleImageView);
            mSongTv.setText(album.getTitle());
            mArtistTv.setText(album.getArtist());
            mSongDuration.setText("");
        }

        public void bind(Artist artist) {
            mArtist = artist;
            Picasso.get().load(SongLab.generateUri(artist.getAlbumId())).into(mCircleImageView);
            mSongTv.setText(artist.getName());
            mArtistTv.setText(SongLab.getInstance().getArtistSongsNumber(artist.getId()) + " Tracks ");
            mSongDuration.setText("");
        }

        public void bind(SongEntity song) {
            mSong = song;
            Picasso.get().load(SongLab.generateUri(song.getAlbumId())).into(mCircleImageView);
//            mCircleImageView.setImageBitmap(SongLab.generateBitmap(getActivity(), song.getAlbumId()));
            mSongTv.setText(song.getTitle());
            mArtistTv.setText(song.getArtist());
            mSongDuration.setText(SongLab.convertDuration(song.getDuration()));
        }

    }

    private class SectionSearchAdapter extends SimpleSectionedAdapter<SearchRvHolder> {
        private List<Album> mAlbumList;
        private List<SongEntity> mSongList;
        private List<Artist> mArtistList;

        public SectionSearchAdapter(List<Album> albumList, List<SongEntity> songList, List<Artist> artistList) {
            mAlbumList = albumList;
            mSongList = songList;
            mArtistList = artistList;
        }

        public void setAlbumList(List<Album> albumList) {
            mAlbumList = albumList;
        }

        public void setSongList(List<SongEntity> songList) {
            mSongList = songList;
        }

        public void setArtistList(List<Artist> artistList) {
            mArtistList = artistList;
        }

        @Override
        protected String getSectionHeaderTitle(int section) {
            if (section == 0)
                return "Songs";
            else if (section == 1)
                return "Albums";
            else
                return "Artists";
        }

        @Override
        protected int getSectionCount() {
            return 3;
        }

        @Override
        protected int getItemCountForSection(int section) {
            if (section == 0) {
                if (mSongList == null)
                    return 0;
                else
                    return mSongList.size();
            } else if (section == 1) {
                if (mAlbumList == null)
                    return 0;
                else
                    return mAlbumList.size();
            } else {
                if (mArtistList == null)
                    return 0;
                else
                    return mArtistList.size();
            }
        }

        @Override
        protected SearchRvHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.songs_list_item, parent, false);
            SearchRvHolder searchRvHolder = new SearchRvHolder(view);
            return searchRvHolder;
        }

        @Override
        protected void onBindItemViewHolder(SearchRvHolder holder, int section, int position) {
            SongEntity songEntity = mSongList.get(position);
            Album album = mAlbumList.get(position);
            Artist artist = mArtistList.get(position);
            holder.bind(album);
            holder.bind(songEntity);
            holder.bind(artist);

        }
    }

   /* private class SearchRvAdapter extends RecyclerView.Adapter<SearchRvHolder> {
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
    */


}

