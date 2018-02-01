
package veeresh.a3c.realm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RecordList extends RealmObject {

    @SerializedName("records")
    @Expose
    private RealmList<Record> records = null;
    @SerializedName("quote_max")
    @Expose
    private String quoteMax;
    @SerializedName("quote_available")
    @Expose
    private String quoteAvailable;

    public RealmList<Record> getRecords() {
        return records;
    }

    public void setRecords(RealmList<Record> records) {
        this.records = records;
    }

    public String getQuoteMax() {
        return quoteMax;
    }

    public void setQuoteMax(String quoteMax) {
        this.quoteMax = quoteMax;
    }

    public String getQuoteAvailable() {
        return quoteAvailable;
    }

    public void setQuoteAvailable(String quoteAvailable) {
        this.quoteAvailable = quoteAvailable;
    }

}
