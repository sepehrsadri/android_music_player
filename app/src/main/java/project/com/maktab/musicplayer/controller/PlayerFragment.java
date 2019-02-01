package project.com.maktab.musicplayer.controller;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.RenderScript;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.Utilities;
import project.com.maktab.musicplayer.model.orm.SongEntity;
import project.com.maktab.musicplayer.model.SongLab;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements Runnable, MediaPlayer.OnCompletionListener {
    private static final String SONG_ID_ARG = "song_id_arg";

    private Toolbar mToolbar;
    private SongEntity mSong;
    private TextView mTvSongName, mTvSongArtist, mSeekBarStatusTv;
    private CircleImageView mSongCoverIv;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private boolean mWasPlaying;
    private boolean mRepeateSong = false;
    private ImageView mBackGroundIv;
    private AppCompatCheckBox mLikeCheckBox;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private FloatingActionButton mActionButton;
    private static boolean mShuffle = false;
    private AppCompatImageButton mNextSongIbtn, mPreviousSongIbtn, mShuffleSongIbtn, mRepeateSongIbtn;
    private CallBacks mCallBacks;
    private AppCompatCheckBox mRepeateAllCheckBox;
    private TextView mSongBarNameTv;
    private TextView mArtistBarNameTv;



    public static PlayerFragment newInstance(Long songId) {

        Bundle args = new Bundle();
        args.putLong(SONG_ID_ARG, songId);
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (PlayerActivity.mRepeateAll) {
            clearMediaPlayer();
            mCallBacks.repeateList();

        }
    }

    public interface CallBacks {
        public void nextSong();

        public void previousSong();

        public void repeateList();

        public void setToolbar(Toolbar toolbar);
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
        mSong = SongLab.getInstance().getSong(id);
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
        View view = inflater.inflate(R.layout.player_fragment_coordinator, container, false);
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
        mRepeateAllCheckBox = view.findViewById(R.id.repeate_all_check_box);
        mBackGroundIv = view.findViewById(R.id.cover_background_image);
        mLikeCheckBox = view.findViewById(R.id.like_song_check_box);
        mToolbar = view.findViewById(R.id.player_fragment_bar);


        mCallBacks.setToolbar(mToolbar);

        mSongBarNameTv = mToolbar.findViewById(R.id.song_name_bar_tv);
        mArtistBarNameTv = mToolbar.findViewById(R.id.song_artist_bar_tv);

        mSongBarNameTv.setText(mSong.getTitle());
        mArtistBarNameTv.setText(mSong.getArtist());




        RenderScript renderScript = RenderScript.create(getActivity());
        Utilities utilities = new Utilities(renderScript);
        Bitmap blurBitmap = utilities.blur(SongLab.generateBitmap(getActivity(), mSong.getAlbumId()), 4f, 1);
        BitmapDrawable ob = new BitmapDrawable(getResources(), blurBitmap);
        mBackGroundIv.setImageBitmap(blurBitmap);

        mLikeCheckBox.setChecked(mSong.getFavourite());

        mLikeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSong.setFavourite(isChecked);
                SongLab.getInstance().updateSong(mSong);
            }
        });
        mRepeateAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    PlayerActivity.mRepeateAll = true;
                else PlayerActivity.mRepeateAll = false;

            }
        });

        if (PlayerActivity.mRepeateAll)
            mRepeateAllCheckBox.setChecked(true);
        else
            mRepeateAllCheckBox.setChecked(false);

        setShuffleDrawble(PlayerActivity.mShuffle);


        mTvSongName.setText(mSong.getTitle());
        mTvSongArtist.setText(mSong.getArtist());

        mRepeateSongIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeateSong = !mRepeateSong;
                if (!mRepeateSong)
                    mRepeateSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_repeat));
                else
                    mRepeateSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_repeat_song));


            }
        });

        mMediaPlayer.setOnCompletionListener(this);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(mRepeateSong);
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
        mSongCoverIv.setImageBitmap(SongLab.generateBitmap(getActivity(), mSong.getAlbumId()));
        return view;
    }

    private void setShuffleDrawble(boolean shuffle) {
        if (shuffle)
            mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_shuffle));
        else
            mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_shuffle));
    }

    public void playSong(boolean loop) {
        mWasPlaying =false;

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
