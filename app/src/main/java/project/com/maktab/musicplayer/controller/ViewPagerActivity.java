package project.com.maktab.musicplayer.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import project.com.maktab.musicplayer.InitAsyncTask;
import project.com.maktab.musicplayer.R;

;

public class ViewPagerActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    public static final String SONG_LOAD_PREFS = "songLoadPrefs";
    public static final String IS_IN_DAO = "isInDao";
    public static final String AUTO_START = "autoStart";

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ViewPagerActivity.class);
        return intent;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_music_item_menu:
                AlertDialog dialog = new AlertDialog.Builder(ViewPagerActivity.this)
                        .setTitle(R.string.scan_music)
                        .setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ScanMusicAndRestart();
                            }
                        })
                        .setNegativeButton(R.string.no_dialog,null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void ScanMusicAndRestart() {
        SharedPreferences.Editor editor = getSharedPreferences(InitAsyncTask.SONG_LOAD_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(InitAsyncTask.IS_IN_DAO, false);
        editor.putBoolean(AUTO_START,true);
        editor.commit();
        Intent mStartActivity = new Intent(ViewPagerActivity.this, StartActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(ViewPagerActivity.this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) ViewPagerActivity.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_coordinator, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_coordinator);
        mViewPager = findViewById(R.id.main_view_pager);
        mTabLayout = findViewById(R.id.main_tab_layout);
        mToolbar = findViewById(R.id.main_toolbar);
        mSearchView = findViewById(R.id.search_view_song);

        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
//        SongLab.getInstance().initSongList(this);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showSearchDialog();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showSearchDialog();
                return true;
            }
        });
    }

    private void showSearchDialog() {
        SearchDialogFragment dialogFragment = SearchDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "show fragment");
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return SongsRecyclerFragment.newInstance("all", 0l);
                case 1:
                    return AlbumRecyclerFragment.newInstance();
                case 2:
                    return ArtistRecyclerFragment.newInstance();
                case 3:
                    return PlaylistFragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.songs);
                case 1:
                    return getString(R.string.album);
                case 2:
                    return getString(R.string.artists);
                case 3:
                    return getString(R.string.playlist);
                default:
                    return super.getPageTitle(position);
            }
        }
    }

    public class SongsTabLayout extends TabLayout {

        public SongsTabLayout(Context context) {
            super(context);
        }

        public SongsTabLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SongsTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            ViewGroup tabLayout = (ViewGroup) getChildAt(0);
            int childCount = tabLayout.getChildCount();

            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int tabMinWidth = displayMetrics.widthPixels / childCount;

            for (int i = 0; i < childCount; ++i) {
                tabLayout.getChildAt(i).setMinimumWidth(tabMinWidth);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


}
