package project.com.maktab.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListSongs extends AppCompatActivity {
    private static final String STATUS_EXTRA = "status_extra";
    private static final String ID_EXTRA = "id_extra";


    public static Intent newIntent(Context context, String status, Long id) {
        Intent intent = new Intent(context, ListSongs.class);
        intent.putExtra(STATUS_EXTRA, status);
        intent.putExtra(ID_EXTRA, id);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_songs);
        String status = getIntent().getStringExtra(STATUS_EXTRA);
        Long id = getIntent().getLongExtra(ID_EXTRA, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SongsRecyclerFragment.newInstance(status, id))
                .commit();


    }
}
