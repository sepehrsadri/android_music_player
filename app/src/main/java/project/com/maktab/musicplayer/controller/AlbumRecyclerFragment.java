package project.com.maktab.musicplayer.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.Album;
import project.com.maktab.musicplayer.model.Song;
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

        mAdapter = new RecyclerViewAdapter(mAlbumList);

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCoverIv;
        private TextView mArtistTv;
        private TextView mAlbumTv;
        private Album mAlbum;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoverIv = itemView.findViewById(R.id.album_item_cover_iv);
            mArtistTv = itemView.findViewById(R.id.album_item_artist_tv);
            mAlbumTv = itemView.findViewById(R.id.album_item_album_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ListSongs.newIntent(getActivity(), "album", mAlbum.getId());
                    startActivity(intent);

                }
            });

        }

        public void bind(Album album) {
            mAlbum = album;
            mCoverIv.setImageBitmap(SongLab.generateBitmap(getActivity(),album.getId()));
            mArtistTv.setText(album.getArtist());
            mAlbumTv.setText(album.getTitle());
        }

    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Album> mAlbumList;

        public void setAlbumList(List<Album> albumList) {
            mAlbumList = albumList;
        }

        public RecyclerViewAdapter(List<Album> albumList) {
            mAlbumList = albumList;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.album_list_item, viewGroup, false);
            RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
            Album album = mAlbumList.get(i);
            recyclerViewHolder.bind(album);

        }

        @Override
        public int getItemCount() {
            return mAlbumList.size();
        }
    }
}
