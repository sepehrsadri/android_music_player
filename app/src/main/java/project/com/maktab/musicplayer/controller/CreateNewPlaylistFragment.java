package project.com.maktab.musicplayer.controller;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.PlayList;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewPlaylistFragment extends DialogFragment {
    private EditText mPlayListNameEt;
    private ImageButton mSubmitImageBtn;
    private Long mSongId;
    private SongEntity mSong;
    private static final String SONG_ID_ARGS = "songIdArgsCreate";


    public static CreateNewPlaylistFragment newInstance(Long songId) {

        Bundle args = new Bundle();
        args.putLong(SONG_ID_ARGS, songId);
        CreateNewPlaylistFragment fragment = new CreateNewPlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CreateNewPlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_playlist, container, false);
        mPlayListNameEt = view.findViewById(R.id.create_playlist_name_et);
        mSubmitImageBtn = view.findViewById(R.id.submit_playlist_name_i_btn);

        mSubmitImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayList playList = new PlayList();
                String name = mPlayListNameEt.getText().toString();
                playList.setName(name);
                Long id = PlaylistLab.getmInstance().insert(playList);
                mSong.setPlaylistId(id);
                SongLab.getInstance().updateSong(mSong);

                dismiss();
            }
        });


        return view;
    }

}
