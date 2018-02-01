package veeresh.a3c.realm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import veeresh.a3c.realm.R;
import veeresh.a3c.realm.utilities.MyCustomTextView;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.profile_picture)
    CircleImageView profilePicture;
    @BindView(R.id.batsman_name)
    MyCustomTextView batsmanName;
    @BindView(R.id.country)
    MyCustomTextView country;
    @BindView(R.id.runs)
    MyCustomTextView runs;
    @BindView(R.id.matches)
    MyCustomTextView matches;
    @BindView(R.id.desc)
    MyCustomTextView desc;
    @BindView(R.id.share)
    MyCustomTextView share;
    @BindView(R.id.fav_player)
    MyCustomTextView favoritePlayer;

    private String description;


    @OnClick(R.id.share)
    public void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, description);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        checkIntent();

    }

    private void checkIntent() {
        Intent intent = getIntent();


        if (intent.getStringExtra(MainActivity.PLAYER_NAME) != null)
            batsmanName.setText(intent.getStringExtra(MainActivity.PLAYER_NAME));
        if (intent.getStringExtra(MainActivity.PLAYER_COUNTRY) != null)
            country.setText(intent.getStringExtra(MainActivity.PLAYER_COUNTRY));
        if (intent.getStringExtra(MainActivity.PLAYER_DESC) != null){
            desc.setText(intent.getStringExtra(MainActivity.PLAYER_DESC));
            description = intent.getStringExtra(MainActivity.PLAYER_DESC);
        }
        if (intent.getStringExtra(MainActivity.PLAYER_IMAGE) != null)
            Glide.with(this).load(intent.getStringExtra(MainActivity.PLAYER_IMAGE)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(profilePicture);

        runs.setText("Runs " + String.valueOf(intent.getLongExtra(MainActivity.PLAYER_SCORE, 0)));
        matches.setText("Matches " + String.valueOf(intent.getLongExtra(MainActivity.PLAYER_MATCHES, 0)));

        boolean isFav = intent.getBooleanExtra(MainActivity.PLAYER_FAV, false);
        if (isFav)
            favoritePlayer.setVisibility(View.VISIBLE);
        else
            favoritePlayer.setVisibility(View.GONE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
