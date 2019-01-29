package project.com.maktab.musicplayer.controller;


import android.content.Intent;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.Artist;
import project.com.maktab.musicplayer.model.SongLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistRecyclerFragment extends Fragment {
    private List<Artist> mArtistList;
    private RecyclerView mRv;
    private RecyclerAdapter mAdapter;


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

        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerAdapter(mArtistList);

        mRv.setAdapter(mAdapter);


        return view;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private Artist mArtist;
        private CircleImageView mImageView;
        private TextView mArtistTv;
        private TextView mArtistSongsTv;
        private TextView mArtistAlbumsTv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.artist_item_cover);
            mArtistTv = itemView.findViewById(R.id.artist_item_name);
            /*mArtistSongsTv = itemView.findViewById(R.id.artist_item_songs);
            mArtistAlbumsTv = itemView.findViewById(R.id.artist_item_albums);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ListSongs.newIntent(getActivity(),"artist",mArtist.getId());
                    startActivity(intent);
                }
            });

        }

        public void bind(Artist artist) {
            mArtist = artist;
            mImageView.setImageBitmap(artist.getBitmap());
            mArtistTv.setText(artist.getName());
         /*   mArtistSongsTv.setText(artist.getTracks() + "");
            mArtistAlbumsTv.setText(artist.getAlbums() + "");
*/
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<Artist> mArtistList;

        public void setArtistList(List<Artist> artistList) {
            mArtistList = artistList;
        }

        public RecyclerAdapter(List<Artist> artistList) {
            mArtistList = artistList;
        }


        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.artist_list_item, viewGroup, false);
            RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
            Artist artist = mArtistList.get(i);
            recyclerViewHolder.bind(artist);

        }

        @Override
        public int getItemCount() {
            return mArtistList.size();
        }
    }


}
