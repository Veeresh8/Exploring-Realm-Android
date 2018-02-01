package veeresh.a3c.realm.database;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import veeresh.a3c.realm.models.Record;

/**
 * Created by Veeresh on 3/11/17.
 */
public class RecordsDao {

    private Realm mRealm;

    public RecordsDao(@NonNull Realm realm) {
        mRealm = realm;
    }

    public void saveRecords(final List<Record> recordLists) {
        mRealm.executeTransaction(realm -> mRealm.copyToRealmOrUpdate(recordLists));
    }

    public void saveToFavorites(Record record) {
        mRealm.executeTransaction(realm -> {
            record.setFavourite(true);
            mRealm.copyToRealmOrUpdate(record);
        });
    }

    public void removeFromFavorites(Record record) {
        mRealm.executeTransaction(realm -> {
            record.setFavourite(false);
            mRealm.copyToRealmOrUpdate(record);
        });
    }

    public RealmResults<Record> loadRecords() {
        return mRealm.where(Record.class).findAllSorted("id");
    }

    public RealmResults<Record> loadFavRecords() {
        return mRealm.where(Record.class).equalTo("isFavourite", true).findAll();
    }

    public RealmResults<Record> loadMostRuns() {
        RealmResults<Record> result = mRealm.where(Record.class).findAll();
        result = result.sort("totalScore", Sort.DESCENDING);
        return result;
    }

    public RealmResults<Record> loadSearch(String query) {
        return mRealm.where(Record.class).contains("name", query, Case.INSENSITIVE).findAll();
    }

    public RealmResults<Record> loadMostMatches() {
        RealmResults<Record> result = mRealm.where(Record.class).findAll();
        result = result.sort("matchesPlayed", Sort.DESCENDING);
        return result;
    }

    public RealmResults<Record> loadLeastRuns() {
        RealmResults<Record> result = mRealm.where(Record.class).findAll();
        result = result.sort("totalScore");
        return result;
    }

    public RealmResults<Record> loadLeastMatches() {
        RealmResults<Record> result = mRealm.where(Record.class).findAll();
        result = result.sort("matchesPlayed");
        return result;
    }


}
