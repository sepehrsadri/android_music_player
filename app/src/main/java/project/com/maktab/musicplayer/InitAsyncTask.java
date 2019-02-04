package project.com.maktab.musicplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import project.com.maktab.musicplayer.controller.ViewPagerActivity;
import project.com.maktab.musicplayer.model.SongLab;

import static android.content.Context.MODE_PRIVATE;

public class InitAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    private Activity mStartActivity;
    public static final String SONG_LOAD_PREFS = "songLoadPrefs";
    public static final String IS_IN_DAO = "isInDao";

    public InitAsyncTask(Activity activity) {

        mStartActivity = activity;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Searching For Songs!");
        dialog.show();
    }

    protected Void doInBackground(Void... args) {
        // do background work here
        boolean status;
        SharedPreferences prefs = mStartActivity.getSharedPreferences(SONG_LOAD_PREFS, MODE_PRIVATE);
        boolean loadStatus = prefs.getBoolean(IS_IN_DAO, false);
        if (loadStatus)
            status = SongLab.getInstance().initSongListFromDao();
        else
            status = SongLab.getInstance().initSongList(mStartActivity);


        if (status) {
            Intent intent = ViewPagerActivity.newIntent(mStartActivity);
            mStartActivity.startActivity(intent);
            mStartActivity.finish();
        }


        return null;
    }

    protected void onPostExecute(Void result) {
        // do UI work here
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
