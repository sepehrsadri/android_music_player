package project.com.maktab.musicplayer.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import project.com.maktab.musicplayer.R;

public class LyricsActivity extends AppCompatActivity {
    private static final String SONG_ID_EXTRA = "project.com.maktab.musicplayer.controller.SongIdExtra";
    private Long mSongId;


    public static Intent newIntent(Context context, Long songId) {
        Intent intent = new Intent(context, LyricsActivity.class);
        intent.putExtra(SONG_ID_EXTRA, songId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_songs);
        mSongId = getIntent().getLongExtra(SONG_ID_EXTRA, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();



    }
}
