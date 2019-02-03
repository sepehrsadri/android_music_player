package project.com.maktab.musicplayer.controller;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.Lyrics;
import project.com.maktab.musicplayer.model.orm.LyricsLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends android.support.v4.app.Fragment implements Runnable {

    private static final String SONG_ID_ARGS = "songIdArgs";

    private SongEntity mSong;
    private Long mSongId;
    private FloatingActionButton mPlayBtn;
    private EditText mLyricsEditText;
    private Button mSaveBtn, mSynceBtn;
    private SeekBar mSeekBar;
    private TextView mSeekBarTextView;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private TextView mDisplayLyricsTextView;
    private boolean mWasPlaying;
    private String[] mLyricsArray;
    private static int counter = 0;
    private TextInputLayout mInputLayout;


    public static LyricsFragment newInstance(Long songId) {

        Bundle args = new Bundle();
        args.putLong(SONG_ID_ARGS, songId);
        LyricsFragment fragment = new LyricsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongId = getArguments().getLong(SONG_ID_ARGS, 0);
        mSong = SongLab.getInstance().getSong(mSongId);
        counter = 0;
        mHandler = new Handler();

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mSong.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LyricsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);
        mLyricsEditText = view.findViewById(R.id.lyrics_text_edit_text);
        mSeekBar = view.findViewById(R.id.lyrics_seek_bar);
        mSeekBarTextView = view.findViewById(R.id.lyrics_seek_bar_status_tv);
        mPlayBtn = view.findViewById(R.id.lyrics_play_float_btn);
        mSaveBtn = view.findViewById(R.id.save_lyrics_btn);
        mSynceBtn = view.findViewById(R.id.synce_lyrics_btn);
        mDisplayLyricsTextView = view.findViewById(R.id.lyrics_display_text_view);
        mInputLayout = view.findViewById(R.id.lyrics_text_edit_text_layout);

        mDisplayLyricsTextView.setVisibility(View.GONE);
        mLyricsEditText.setVisibility(View.VISIBLE);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(false);
            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateLyricsInput())
                    return;
                String multiLines = mLyricsEditText.getText().toString();
                String delimiter = "\n";
                mLyricsArray = multiLines.split(delimiter);
                mLyricsEditText.setVisibility(View.GONE);
                mInputLayout.setVisibility(View.GONE);
                mDisplayLyricsTextView.setVisibility(View.VISIBLE);
                mDisplayLyricsTextView.setText(mLyricsArray[counter]);

                Lyrics lyrics = new Lyrics();
                lyrics.setText(multiLines);
                lyrics.setSongId(mSongId);
                lyrics.setDuration(-1);
                LyricsLab.getmInstance().addLyric(lyrics);


            }
        });
        mSynceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lyrics lyrics = new Lyrics();
                int duration = mSeekBar.getProgress();
                lyrics.setDuration(roundSecond(duration));
                lyrics.setText(mLyricsArray[counter]);
                lyrics.setSongId(mSongId);
                LyricsLab.getmInstance().addLyric(lyrics);


                if (counter < mLyricsArray.length)
                    mDisplayLyricsTextView.setText(mLyricsArray[++counter]);

            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekBarTextView.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x < 10)
                    mSeekBarTextView.setText("0:0" + x);
                else
                    mSeekBarTextView.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = mSeekBarTextView.getWidth();
                mSeekBarTextView.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    mPlayBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));
                    mSeekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekBarTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });


        return view;
    }

    private void clearMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;


        }
    }

    public void playSong(boolean loop) {
        mWasPlaying = false;

        try {


            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                clearMediaPlayer();
                mSeekBar.setProgress(0);
                mWasPlaying = true;
                mPlayBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));
            }


            if (!mWasPlaying) {

                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    try {
                        mMediaPlayer.setDataSource(mSong.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                setPauseImage();

                mMediaPlayer.prepare();
                mMediaPlayer.setVolume(0.5f, 0.5f);
                mMediaPlayer.setLooping(loop);
                mSeekBar.setMax(mMediaPlayer.getDuration());

                mMediaPlayer.start();
                new Thread(this).start();

            }

            mWasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clearMediaPlayer();
    }

    private void setPauseImage() {
        mPlayBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
    }

    @Override
    public void run() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        int total = mMediaPlayer.getDuration();


        while (mMediaPlayer != null && mMediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mMediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            mSeekBar.setProgress(currentPosition);

        }
    }
    private boolean validateLyricsInput() {
        if (mLyricsEditText.getText().toString().trim().isEmpty()) {
            mInputLayout.setError(getString(R.string.err_msg_password));
            requestFocus(mLyricsEditText);
            return false;
        } else {
            mInputLayout.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private int roundSecond(int progress) {
        int x = (int) Math.ceil(progress / 1000f);

        if (x < 10)
            return x;
        else
            return x;


    }
}
