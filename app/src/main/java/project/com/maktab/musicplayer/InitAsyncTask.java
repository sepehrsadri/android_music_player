package project.com.maktab.musicplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import project.com.maktab.musicplayer.controller.StartActivity;
import project.com.maktab.musicplayer.controller.ViewPagerActivity;
import project.com.maktab.musicplayer.model.SongLab;

public class InitAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private StartActivity mStartActivity;

        public InitAsyncTask(StartActivity activity) {
            mStartActivity = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Search For Songs!");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            Boolean status = SongLab.getInstance().init(mStartActivity);
            if(status==true){
                Intent intent =  ViewPagerActivity.newIntent(mStartActivity);
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
