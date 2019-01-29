package project.com.maktab.musicplayer.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import project.com.maktab.musicplayer.InitAsyncTask;
import project.com.maktab.musicplayer.R;

public class StartActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12;
    private TextView mStartPlayerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mStartPlayerTextView = findViewById(R.id.start_player_text_view);
        startPlayer();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new InitAsyncTask(StartActivity.this).execute();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    askReadExternalPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void startPlayer() {

        SpannableString spannableCreate = new SpannableString("Start Player");
        ClickableSpan clickableSpanCreateAccount = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                askReadExternalPermission();


            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableCreate.setSpan(clickableSpanCreateAccount, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStartPlayerTextView.setText(spannableCreate);
        mStartPlayerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mStartPlayerTextView.setHighlightColor(Color.TRANSPARENT);
    }

    private void askReadExternalPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(StartActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(StartActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
}
