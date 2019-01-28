package project.com.maktab.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayerActivity extends AppCompatActivity {
    private static final String ID_EXTRA = "id_extra_song";


    public static Intent newIntent(Context context,Long songId){
        Intent intent = new Intent(context,PlayerActivity.class);
        intent.putExtra(ID_EXTRA,songId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
