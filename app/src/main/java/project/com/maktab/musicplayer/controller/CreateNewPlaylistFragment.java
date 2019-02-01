package project.com.maktab.musicplayer.controller;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.orm.PlayList;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewPlaylistFragment extends DialogFragment {
    private EditText mPlayListNameEt;
    private ImageButton mSubmitImageBtn;


    public CreateNewPlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_create_new_playlist, container, false);
        mPlayListNameEt.findViewById(R.id.create_playlist_name_et);
        mSubmitImageBtn.findViewById(R.id.submit_playlist_name_i_btn);

        mSubmitImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayList playList = new PlayList();
                String name = mPlayListNameEt.getText().toString();
                playList.setName(name);
                PlaylistLab.getmInstance().insert(playList);
                dismiss();
            }
        });


        return view;
    }

}
