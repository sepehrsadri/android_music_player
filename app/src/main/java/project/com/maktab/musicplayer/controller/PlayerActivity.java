package project.com.maktab.musicplayer.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Random;

import project.com.maktab.musicplayer.R;
import project.com.maktab.musicplayer.model.orm.SongEntity;
import project.com.maktab.musicplayer.model.SongLab;

public class PlayerActivity extends AppCompatActivity implements PlayerFragment.CallBacks {
    private static final String ID_EXTRA = "id_extra_song";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;
    private List<SongEntity> mSongList;
    public static boolean mShuffle;
    public static boolean mRepeateAll;


    public static Intent newIntent(Context context, Long songId) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(ID_EXTRA, songId);
        return intent;
    }

    public int getSongIndex(Long id) {
        int index = -1;

        for (int i = 0; i < mSongList.size(); i++) {

            if (mSongList.get(i).getSongId().equals(id)) {
                index = i;
                break;

            }
        }
        return index;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        mViewPager = findViewById(R.id.player_view_pager);
        mTabLayout = findViewById(R.id.player_tab_layout);
        mSongList = SongLab.getInstance().getSongList();

        Long id = getIntent().getLongExtra(ID_EXTRA, 0);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(getSongIndex(id));


        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlayerFragment.newInstance(id))
                .commit();*/


    }


    @Override
    public void nextSong() {
        if (!mShuffle) {
            int current = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(current + 1);
        } else
            mViewPager.setCurrentItem(randomGenerator());
    }

    @Override
    public void previousSong() {
        if (!mShuffle) {
            int current = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(current - 1);
        } else
            mViewPager.setCurrentItem(randomGenerator());
    }

    @Override
    public void repeateList() {
        mViewPager.setCurrentItem(0);
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {
            return PlayerFragment.newInstance(mSongList.get(i).getId());
        }

        @Override
        public int getCount() {
            return mSongList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mSongList.get(position).getTitle();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    private int randomGenerator() {
        Random random = new Random();
        int low = 0;
        int high = mSongList.size();
        int result = random.nextInt(high - low) + low;
        return result;

    }


}
