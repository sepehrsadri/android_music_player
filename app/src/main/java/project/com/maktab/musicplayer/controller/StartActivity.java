package project.com.maktab.musicplayer.controller;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import project.com.maktab.musicplayer.R;

public class StartActivity extends AppCompatActivity {
    private TextView mStartPlayerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mStartPlayerTextView = findViewById(R.id.start_player_text_view);
        startPlayer();



    }
    private void startPlayer() {

        SpannableString spannableCreate = new SpannableString("Start Player");
        ClickableSpan clickableSpanCreateAccount = new ClickableSpan() {
            @Override
            public void onClick(View textView) {


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
}
