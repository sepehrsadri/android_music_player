package project.com.maktab.musicplayer.controller;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.PictureUtils;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.JoinLab;
import project.com.maktab.musicplayer.model.orm.JoinSongsWithPlaylist;
import project.com.maktab.musicplayer.model.orm.PlayList;
import project.com.maktab.musicplayer.model.orm.PlaylistLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewPlaylistFragment extends DialogFragment {
    private static final int GET_FROM_GALLERY_REQ_CODE = 12;
    private EditText mPlayListNameEt;
    private ImageButton mSubmitImageBtn;
    private Long mSongId;
    private SongEntity mSong;
    private CircleImageView mCirclePlayListCover;
    private ImageButton mUploadCoverIbtn;
    private TextInputLayout mPlayListNameInputLayout;
    private static final String SONG_ID_ARGS = "songIdArgsCreate";
    private PlayList mPlayList;


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
        mPlayList = new PlayList();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    /* @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }*/

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
        mCirclePlayListCover = view.findViewById(R.id.play_list_circle_image_cover);
        mPlayListNameInputLayout = view.findViewById(R.id.create_play_list_name_layout);
        mUploadCoverIbtn = view.findViewById(R.id.upload_play_list_cover_i_btn);

        mUploadCoverIbtn.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GET_FROM_GALLERY_REQ_CODE);
        });


        mSubmitImageBtn.setOnClickListener(v -> {

            if(!validatePlayListName())
                return;


            String name = mPlayListNameEt.getText().toString();
            mPlayList.setName(name);
            Long id = PlaylistLab.getmInstance().insert(mPlayList);
            mPlayList.getSongsWithThisPlaylist().add(mSong);
            PlaylistLab.getmInstance().update(mPlayList);
            JoinSongsWithPlaylist join = new JoinSongsWithPlaylist();
            join.setSongId(mSongId);
            join.setPlaylistId(id);
            JoinLab.getmJoinInstance().insertJoins(join);


//            mSong.setPlaylistId(id);


            SongLab.getInstance().updateSong(mSong);
            dismiss();
            Toast.makeText(getActivity(), "added to playlist " + name, Toast.LENGTH_SHORT).show();
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            return;
        }
        if (requestCode == GET_FROM_GALLERY_REQ_CODE) {
            Uri imageUri = data.getData();
            if(imageUri==null)
                return;

            mPlayList.setImage(imageUri.toString());
            Bitmap selectedImage = null;
//            ExifInterface exif = null;

            try {
                selectedImage = PictureUtils.decodeUri(getActivity(), imageUri);
//                selectedImage = PictureUtils.modifyOrientation(selectedImage,imageUri.getPath());
//                exif = new ExifInterface(imageUri.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
           /* int orientaion  = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap finalBitmap = PictureUtils.rotateBitmap(selectedImage,orientaion);
*/
                /*
                for casting uri to bitmap but take too much size.
                InputStream imageStream = null;
                imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(imageStream);*/

            mCirclePlayListCover.setImageBitmap(selectedImage);

        }
    }

    private boolean validatePlayListName() {
        if (mPlayListNameEt.getText().toString().trim().isEmpty()) {
            mPlayListNameInputLayout.setError(getString(R.string.err_msg_password));
            requestFocus(mPlayListNameEt);
            return false;
        } else {
           mPlayListNameInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
