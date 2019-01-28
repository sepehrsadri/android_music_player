package project.com.maktab.musicplayer;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import project.com.maktab.musicplayer.model.Song;
import project.com.maktab.musicplayer.model.SongLab;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements Runnable {
    private static final String SONG_ID_ARG = "song_id_arg";

    private Song mSong;
    private TextView mTvSongName, mTvSongArtist, mSeekBarStatusTv;
    private ImageView mSongCoverIv;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private boolean mWasPlaying, mRepeateSong;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private FloatingActionButton mActionButton;
    private static boolean mShuffle=false;
    private AppCompatImageButton mNextSongIbtn, mPreviousSongIbtn, mShuffleSongIbtn, mRepeateSongIbtn;
    private CallBacks mCallBacks;


    public static PlayerFragment newInstance(Long songId) {

        Bundle args = new Bundle();
        args.putLong(SONG_ID_ARG, songId);
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallBacks {
        public void nextSong();

        public void previousSong();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBacks = (PlayerActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Long id = getArguments().getLong(SONG_ID_ARG, 0);
        mSong = SongLab.getInstance().getSong(getActivity(), id);
        mHandler = new Handler();
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mSong.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        mTvSongArtist = view.findViewById(R.id.player_song_artist);
        mTvSongName = view.findViewById(R.id.player_song_name);
        mSongCoverIv = view.findViewById(R.id.player_song_cover);
        mSeekBar = view.findViewById(R.id.player_seek_bar);
        mActionButton = view.findViewById(R.id.floatingActionButton);
        mSeekBarStatusTv = view.findViewById(R.id.seek_bar_status_tv);
        mNextSongIbtn = view.findViewById(R.id.play_next_iBtn);
        mPreviousSongIbtn = view.findViewById(R.id.previous_song_iBtn);
        mShuffleSongIbtn = view.findViewById(R.id.shuffle_play);
        mRepeateSongIbtn = view.findViewById(R.id.song_repeate_iBtn);
        setShuffleDrawble(PlayerActivity.mShuffle);

        mSongCoverIv.setImageBitmap(mSong.getBitmap());
        mTvSongName.setText(mSong.getTitle());
        mTvSongArtist.setText(mSong.getArtist());

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekBarStatusTv.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x < 10)
                    mSeekBarStatusTv.setText("0:0" + x);
                else
                    mSeekBarStatusTv.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = mSeekBarStatusTv.getWidth();
                mSeekBarStatusTv.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    mActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));
                    mSeekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekBarStatusTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        mNextSongIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMediaPlayer();
                mCallBacks.nextSong();

            }
        });
        mPreviousSongIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMediaPlayer();
                mCallBacks.previousSong();
            }
        });
        mShuffleSongIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShuffle = !mShuffle;
                PlayerActivity.mShuffle = mShuffle;
                setShuffleDrawble(mShuffle);

            }
        });

        return view;
    }

    private void setShuffleDrawble(boolean shuffle) {
        if(shuffle)
        mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_no_shuffle));
        else
            mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_shuffle));
    }

    public void playSong() {

        try {


            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                clearMediaPlayer();
                mSeekBar.setProgress(0);
                mWasPlaying = true;
                mActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));
            }


            if (!mWasPlaying) {

                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }

                setPauseImage();

                mMediaPlayer.prepare();
                mMediaPlayer.setVolume(0.5f, 0.5f);
                mMediaPlayer.setLooping(false);
                mSeekBar.setMax(mMediaPlayer.getDuration());

                mMediaPlayer.start();
                new Thread(this).start();

            }

            mWasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setPauseImage() {
        mActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
    }

    private void clearMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;


        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            clearMediaPlayer();
        }
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
}
