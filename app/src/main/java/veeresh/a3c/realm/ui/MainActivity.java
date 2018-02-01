package veeresh.a3c.realm.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import veeresh.a3c.realm.R;
import veeresh.a3c.realm.database.RealmManager;
import veeresh.a3c.realm.events.MyEvents;
import veeresh.a3c.realm.models.Record;
import veeresh.a3c.realm.models.RecordList;
import veeresh.a3c.realm.networking.RetrofitConnection;
import veeresh.a3c.realm.ui.adapters.RecordsAdapter;
import veeresh.a3c.realm.utilities.APIOptions;
import veeresh.a3c.realm.utilities.EndlessRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PLAYER_NAME = "playerName";
    public static final String PLAYER_SCORE = "playerScore";
    public static final String PLAYER_MATCHES = "playerMatches";
    public static final String PLAYER_DESC = "playerDesc";
    public static final String PLAYER_COUNTRY = "playerCountry";
    public static final String PLAYER_IMAGE = "playerImage";
    public static final String PLAYER_FAV = "playerFav";


    private RecordsAdapter adapter;
    private RealmResults<Record> recordRealmResults;
    @BindView(R.id.rv_records)
    RecyclerView recyclerView;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.action_sort)
    ImageView sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RealmManager.open();
        initUI();
        search();
    }

    private void search() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    adapter.updateData(RealmManager.recordsDao().loadRecords());
                } else {
                    recordRealmResults = RealmManager.recordsDao().loadSearch(newText.toLowerCase());
                    adapter.updateData(recordRealmResults);
                }
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void initUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recordRealmResults = RealmManager.recordsDao().loadRecords();
        adapter = new RecordsAdapter(recordRealmResults, this);
        recyclerView.setAdapter(adapter);
        addScrollListener(linearLayoutManager, recyclerView);
        validateRecyclerView();

    }

    private void addScrollListener(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView) {
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "Load More");
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void getBatsman() {
        if (isNetworkAvailable()) {
            RetrofitConnection.getService().getBatsmen(APIOptions.getOptions()).enqueue(new Callback<RecordList>() {
                @Override
                public void onResponse(Call<RecordList> call, Response<RecordList> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            saveResponse(response.body().getRecords());
                            loadFromDB();
                        });
                    }
                }

                @Override
                public void onFailure(Call<RecordList> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromDB() {
        recordRealmResults = RealmManager.recordsDao().loadRecords();
        adapter.updateData(recordRealmResults);
        validateRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        RealmResults<Record> realmResults = RealmManager.recordsDao().loadRecords();
        if (realmResults.size() == 0) {
            getBatsman();
        } else {
            adapter.updateData(RealmManager.recordsDao().loadRecords());
            validateRecyclerView();
        }
    }

    private void saveResponse(List<Record> response) {
        RealmManager.recordsDao().saveRecords(response);
    }

    @Override
    protected void onDestroy() {
        RealmManager.close();
        super.onDestroy();
    }

    public void validateRecyclerView() {
        if (recordRealmResults != null && recordRealmResults.isEmpty()) {
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @OnClick(R.id.action_sort)
    public void sort() {
        showSortDialog();
    }

    @OnClick(R.id.action_favorites)
    public void navigateToFavorites() {
        Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
        startActivity(intent);
    }

    private void showSortDialog() {

        final CharSequence[] choice = {"Most Runs Scored", "Most Matches Played", "Least Runs Scored", "Least Matches Played"};

        new AlertDialog.Builder(this)
                .setTitle("Sort By")
                .setSingleChoiceItems(choice, -1, null)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    if (selectedPosition == 0)
                        sortByRuns();
                    else if (selectedPosition == 1)
                        sortByMatches();
                    else if (selectedPosition == 2)
                        sortByLeastRuns();
                    else if (selectedPosition == 3)
                        sortByLeastMatches();
                })
                .show();
    }

    private void sortByLeastMatches() {
        recordRealmResults = RealmManager.recordsDao().loadLeastMatches();
        adapter.updateData(recordRealmResults);
    }

    private void sortByLeastRuns() {
        recordRealmResults = RealmManager.recordsDao().loadLeastRuns();
        adapter.updateData(recordRealmResults);
    }

    private void sortByMatches() {
        recordRealmResults = RealmManager.recordsDao().loadMostMatches();
        adapter.updateData(recordRealmResults);
    }

    private void sortByRuns() {
        recordRealmResults = RealmManager.recordsDao().loadMostRuns();
        adapter.updateData(recordRealmResults);
    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void navigateEvent(MyEvents.RecordEvent recordEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, recordEvent.getImageView(), "source");
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(PLAYER_NAME, recordEvent.getRecord().getName());
        intent.putExtra(PLAYER_COUNTRY, recordEvent.getRecord().getCountry());
        intent.putExtra(PLAYER_DESC, recordEvent.getRecord().getDescription());
        intent.putExtra(PLAYER_SCORE, recordEvent.getRecord().getTotalScore());
        intent.putExtra(PLAYER_MATCHES, recordEvent.getRecord().getMatchesPlayed());
        intent.putExtra(PLAYER_FAV, recordEvent.getRecord().isFavourite());
        intent.putExtra(PLAYER_IMAGE, recordEvent.getRecord().getImage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
