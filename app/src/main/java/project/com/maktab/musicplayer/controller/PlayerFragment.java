package project.com.maktab.musicplayer.controller;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.Utilities;
import project.com.maktab.musicplayer.model.SongLab;
import project.com.maktab.musicplayer.model.orm.LyricsLab;
import project.com.maktab.musicplayer.model.orm.SongEntity;

import static project.com.maktab.musicplayer.database.App.MUSIC_CHANEL_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements Runnable, MediaPlayer.OnCompletionListener {
    private static final String SONG_ID_ARG = "song_id_arg";
    private static final int MUSIC_NOTIFICATION_ID = 22;
    private NotificationManagerCompat mNotificationManager;

    private Toolbar mToolbar;
    private SongEntity mSong;
    private TextView mSeekBarStatusTv;
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
    public static final String TAG = "songPlayTag";
    private AppCompatImageButton mNextSongIbtn, mPreviousSongIbtn, mShuffleSongIbtn, mRepeateSongIbtn;
    private CallBacks mCallBacks;
    private AppCompatCheckBox mRepeateAllCheckBox;
    private TextView mSongBarNameTv;
    private TextView mArtistBarNameTv;
    private RecyclerView mRecyclerView;
    private TextView mLyricsTextView;
    private LyricsAdapter mLyricsAdapter;
    private AppCompatCheckBox mLyricsImageCheckBox;
    private List<String> mTextList;
    private List<Integer> mDurationList;
    private boolean mShowLyrics;
    private SongEntity mNotificationSong;
    private LottieAnimationView mAnimationView;
    private boolean mShowUnsynce = false;
    boolean hasLyrics;
    boolean lyricsStatus;
    private boolean _hasLoadedOnce = false; // your boolean field


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

    private void updateUI() {
        List<String> list
                = LyricsLab.getmInstance().getUnsynceLyrics();
        if (mLyricsAdapter == null) {
            mLyricsAdapter = new LyricsAdapter(list);
            mRecyclerView.setAdapter(mLyricsAdapter);
        } else {
            mLyricsAdapter.setLyricsList(list);
            mLyricsAdapter.notifyDataSetChanged();

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
        mHandler = new Handler();
        mSong = SongLab.getInstance().getSong(id);
        mNotificationSong = mSong;


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_to_playlist:
                showDialogPlaylist();
                return true;
            case R.id.add_lyrics_to_song:
                Intent intent = LyricsActivity.newIntent(getActivity(), mSong.getId());
                startActivity(intent);
                return true;
            case R.id.share_song_menu_item:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                Uri uri = Uri.parse("file:///" + mSong.getData());
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Sound File"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.player_fragment_coordinator, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showDialogPlaylist() {
        PlaylistDialogFragment fragment = PlaylistDialogFragment.newInstance(mSong.getId());
        fragment.show(getFragmentManager(), "playlist");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"check it is coming or no");
        controlLyrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.player_fragment_coordinator, container, false);
        setHasOptionsMenu(true);
   /*     mTvSongArtist = view.findViewById(R.id.player_song_artist);
        mTvSongName = view.findViewById(R.id.player_song_name);*/
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
        mRecyclerView = view.findViewById(R.id.player_recyclerview_lyrics);
        mLyricsTextView = view.findViewById(R.id.player_song_lyrics);
        mAnimationView = view.findViewById(R.id.has_lyrics_lottie);
        mToolbar = view.findViewById(R.id.player_fragment_bar);
        mLyricsImageCheckBox = view.findViewById(R.id.show_lyrics_check_box);
        mShowLyrics = false;

        PlayerActivity activity = (PlayerActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.inflateMenu(R.menu.player_fragment_coordinator);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_add_to_playlist:
                        showDialogPlaylist();
                        return true;
                    case R.id.add_lyrics_to_song:
                        Intent intent = LyricsActivity.newIntent(getActivity(), mSong.getId());
                        startActivity(intent);

                        return true;
                    case R.id.share_song_menu_item:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("audio/*");
                        Uri uri = Uri.parse(mSong.getData());
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share Sound File"));
                        return true;
                    default:
                        return false;
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        controlLyrics();


        mSongCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowUnsynce = !mShowUnsynce;
                if (!hasLyrics)
                    Toast.makeText(getActivity(), "no lyrics for this song", Toast.LENGTH_SHORT).show();
                if (mShowUnsynce && hasLyrics) {
                    mLyricsTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {

                    mLyricsTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        mLyricsImageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mShowLyrics = isChecked;
                if (isChecked) {
                    mLyricsTextView.setVisibility(View.VISIBLE);
                    if (!hasLyrics) {
                        Toast.makeText(getActivity(), "no lyrics for this song", Toast.LENGTH_SHORT).show();
                        mLyricsTextView.setVisibility(View.GONE);
                    }

                } else mLyricsTextView.setVisibility(View.GONE);

            }
        });

        /*    boolean status = LyricsLab.getmInstance().lyricsStatusAndGenerate(mSong.getId());
            if (status) {
                mLyricsTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                updateUI();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mLyricsTextView.setVisibility(View.VISIBLE);
            }*/


//        mCallBacks.setToolbar(mToolbar);

//        ((PlayerActivity) getActivity()).setSupportActionBar(mToolbar);


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


        /*mTvSongName.setText(mSong.getTitle());
        mTvSongArtist.setText(mSong.getArtist());*/

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

                if (mShowLyrics) {
                    for (int i = 0; i < mDurationList.size(); i++) {
                        if (x == mDurationList.get(i)){
                            mLyricsTextView.setText(mTextList.get(i));

                            if(x==mDurationList.get(mDurationList.size()-1)){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLyricsTextView.setText("");
                                    }
                                },5000);
                            }
                        /*
                            if (mLyricsTextView.getVisibility() == View.GONE) {
                                mLyricsTextView.animate()
                                        .translationY(mLyricsTextView.getHeight()).alpha(1.0f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                super.onAnimationStart(animation);
                                                mLyricsTextView.setVisibility(View.VISIBLE);
                                                mLyricsTextView.setAlpha(0.0f);

                                            }
                                        });
                            } else {
                                mLyricsTextView.animate()
                                        .translationY(0).alpha(0.0f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mLyricsTextView.setVisibility(View.GONE);
                                            }
                                        });
                            }
*/

                        }
                    }
                }

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

            private void lyricsAnimation(int index) {
                mLyricsTextView.animate()
                        .translationY(mLyricsTextView.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(null);
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

    private void controlLyrics() {
        mRecyclerView.setVisibility(View.GONE);
        mLyricsTextView.setVisibility(View.GONE);
        hasLyrics = LyricsLab.getmInstance().hasLyricsText(mSong.getId());
        lyricsStatus= LyricsLab.getmInstance().lyricsStatusAndGenerate(mSong.getId());
        if (hasLyrics) {
            mAnimationView.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAnimationView.cancelAnimation();
                    mAnimationView.setVisibility(View.GONE);
                }
            }, 5000);
            updateUI();
            if (!lyricsStatus) {
                mTextList = LyricsLab.getmInstance().getSynceLyrics();
                mDurationList = LyricsLab.getmInstance().getSynceDurationLyrics();
            }

        }
    }

    private void setShuffleDrawble(boolean shuffle) {
        if (shuffle)
            mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_shuffle));
        else
            mShuffleSongIbtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_shuffle));
    }

    public void playSong(boolean loop) {
        mWasPlaying = false;

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
                showNotification();
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

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !_hasLoadedOnce) {
//                showNotification();
                _hasLoadedOnce = true;
            }
        }
        if (!isVisibleToUser) {
            clearMediaPlayer();
        }

    }

    private void showNotification() {
        mNotificationManager = NotificationManagerCompat.from(getActivity());
        Notification notification = new NotificationCompat.Builder(getActivity(), MUSIC_CHANEL_ID)
                .setSmallIcon(R.drawable.icon_malhaar5)
                .setLargeIcon(SongLab.generateBitmap(getActivity(), mNotificationSong.getAlbumId()))
                .setContentTitle(mNotificationSong.getTitle())
                .setContentText(mNotificationSong.getArtist())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        mNotificationManager.notify(MUSIC_NOTIFICATION_ID, notification);
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

    private class LyricsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public LyricsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.lyrics_text_item_list);
        }

        public void bind(String text) {
            mTextView.setText(text);
        }

    }

    private class LyricsAdapter extends RecyclerView.Adapter<LyricsViewHolder> {
        private List<String> mLyricsList;

        public void setLyricsList(List<String> lyricsList) {
            mLyricsList = lyricsList;
        }

        public LyricsAdapter(List<String> lyricsList) {
            mLyricsList = lyricsList;
        }

        @NonNull
        @Override
        public LyricsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.lyrics_list_item, viewGroup, false);
            LyricsViewHolder viewHolder = new LyricsViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull LyricsViewHolder lyricsViewHolder, int i) {
            String item = mLyricsList.get(i);
            lyricsViewHolder.bind(item);

        }

        @Override
        public int getItemCount() {
            return mLyricsList.size();
        }
    }


}
