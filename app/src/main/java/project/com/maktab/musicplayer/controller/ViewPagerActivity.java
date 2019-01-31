package project.com.maktab.musicplayer.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.database.App;
import project.com.maktab.musicplayer.model.SongLab;
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

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ViewPagerActivity.class);
        return intent;

    }

/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*     SharedPreferences prefs = getSharedPreferences(SONG_LOAD_PREFS, MODE_PRIVATE);
        boolean loadStatus = prefs.getBoolean(IS_IN_DAO, false);
        if (loadStatus)
            SongLab.getInstance().initSongListFromDao();
        else
            SongLab.getInstance().initSongList(this);*/

        // Here, thisActivity is the current activity
  /*      if (ContextCompat.checkSelfPermission(ViewPagerActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ViewPagerActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ViewPagerActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }*/
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
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);


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
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
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
                default:
                    return super.getPageTitle(position);
            }
        }
    }


}
