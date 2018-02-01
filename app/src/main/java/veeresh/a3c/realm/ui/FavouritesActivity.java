package veeresh.a3c.realm.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import veeresh.a3c.realm.R;
import veeresh.a3c.realm.database.RealmManager;
import veeresh.a3c.realm.models.Record;
import veeresh.a3c.realm.ui.adapters.FavouritesAdapter;

public class FavouritesActivity extends AppCompatActivity {


    private FavouritesAdapter adapter;
    private ArrayList<Record> recordArrayList = new ArrayList<>();

    @BindView(R.id.rv_fav)
    RecyclerView recyclerView;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouritesAdapter(recordArrayList, this);
        recyclerView.setAdapter(adapter);
        loadFav();
        validateRecyclerView();

    }

    private void loadFav() {
        final RealmResults<Record> realmResults = RealmManager.recordsDao().loadFavRecords();
        if (realmResults != null) {
            recordArrayList.addAll(realmResults);
            adapter.notifyDataSetChanged();
        }

    }


    public void validateRecyclerView() {
        if (recordArrayList.isEmpty()) {
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
