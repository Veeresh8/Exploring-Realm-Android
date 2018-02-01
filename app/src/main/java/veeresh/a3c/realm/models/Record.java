
package veeresh.a3c.realm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Record extends RealmObject {


    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total_score")
    @Expose
    private long totalScore;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("matches_played")
    @Expose
    private long matchesPlayed;
    @SerializedName("country")
    @Expose
    private String country;

    private boolean isFavourite;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(long matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
