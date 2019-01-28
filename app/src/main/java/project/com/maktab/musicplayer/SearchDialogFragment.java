package project.com.maktab.musicplayer;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.model.Song;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;


    public SearchDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_dialog, container, false);
        mRecyclerView = view.findViewById(R.id.search_fragment_recycler_view);
        mSearchView = view.findViewById(R.id.search_fragment_search_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



        return view;
    }

    private class SearchRvHolder extends RecyclerView.ViewHolder {
        private CircleImageView mCircleImageView;
        private TextView mSongTv;
        private TextView mArtistTv;
        private Song mSong;

        public SearchRvHolder(@NonNull View itemView) {
            super(itemView);
            mCircleImageView = itemView.findViewById(R.id.cover_image);
            mSongTv = itemView.findViewById(R.id.songs_name_tv);
            mArtistTv = itemView.findViewById(R.id.artist_name_tv);

        }

        public void bind(Song song) {
            mSong = song;
            mCircleImageView.setImageBitmap(song.getBitmap());
            mSongTv.setText(song.getTitle());
            mArtistTv.setText(song.getArtist());
        }

    }

    private class SearchRvAdapter extends RecyclerView.Adapter<SearchRvHolder> {
        private List<Song> mSongList;

        public void setSongList(List<Song> songList) {
            mSongList = songList;
        }

        public SearchRvAdapter(List<Song> songList) {
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
            Song song = mSongList.get(i);
            searchRvHolder.bind(song);

        }

        @Override
        public int getItemCount() {
            return mSongList.size();
        }
    }


}
