package project.com.maktab.musicplayer.controller;


import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.List;

import project.com.maktab.musicplayer.PictureUtils;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.PlayList;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDialogFragment extends DialogFragment {
    private ImageButton mCreatePlaylistIb;
    private RecyclerView mPlaylistRv;
    private static final String SONG_ID_ARGS = "songIdArgs";
    private Long mSongId;
    private SongEntity mSong;
    private PlaylistAdapter mAdapter;

    public static PlaylistDialogFragment newInstance(Long songId) {

        Bundle args = new Bundle();
        args.putLong(SONG_ID_ARGS, songId);
        PlaylistDialogFragment fragment = new PlaylistDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PlaylistDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongId = getArguments().getLong(SONG_ID_ARGS);
        mSong = SongLab.getInstance().getSong(mSongId);
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
        View view = inflater.inflate(R.layout.fragment_playlist_dialog, container, false);
        mCreatePlaylistIb = view.findViewById(R.id.create_play_list_ib);
        mPlaylistRv = view.findViewById(R.id.play_list_choice_rv);

        mPlaylistRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        updateUI();
        List<PlayList> playLists = PlaylistLab.getmInstance().getAllList();
        mAdapter = new PlaylistAdapter(playLists);
        mPlaylistRv.setAdapter(mAdapter);
        mCreatePlaylistIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewPlaylistFragment fragment = CreateNewPlaylistFragment.newInstance(mSongId);
                fragment.show(getFragmentManager(), "create");
                dismiss();

            }
        });

        return view;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlaylistAdapter(PlaylistLab.getmInstance().getAllList());
            mPlaylistRv.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayLists(PlaylistLab.getmInstance().getAllList());
            mAdapter.notifyDataSetChanged();
        }
    }


    private class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCover;
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

                    mSong.setPlaylistId(mPlayList.getId());
                    SongLab.getInstance().updateSong(mSong);
                    dismiss();
                }
            });
        }

        public void bind(PlayList playList) {
            mPlayList = playList;
            mName.setText(playList.getName());
            if (playList.getImage() == null)
                mCover.setImageResource(R.drawable.icon_malhaar5);
            else{
                Bitmap bitmap = null;
                try {
                    bitmap  = PictureUtils.decodeUri(getActivity(), Uri.parse(playList.getImage()));
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
        private List<PlayList> mPlayLists;

        public void setPlayLists(List<PlayList> playLists) {
            mPlayLists = playLists;
        }

        public PlaylistAdapter(List<PlayList> playLists) {
            mPlayLists = playLists;
        }

        @NonNull
        @Override
        public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.playlist_list_item, viewGroup, false);
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
