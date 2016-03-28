package com.thejoker.yts;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by thejoker on 3/26/2016.
 */
@RealmClass
public class FavoriteListRealm extends RealmObject {
    @PrimaryKey
    private int realmMovieId;
    private String realmMovieTitle;
    private String realmThumbnailUrl;
    private int realmMovieYear;

    public int getRealmMovieYear() {
        return realmMovieYear;
    }

    public void setRealmMovieYear(int realmMovieYear) {
        this.realmMovieYear = realmMovieYear;
    }



    public  FavoriteListRealm(){}

    public int getRealmMovieId() {
        return realmMovieId;
    }

    public void setRealmMovieId(int realmMovieId) {
        this.realmMovieId = realmMovieId;
    }

    public String getRealmMovieTitle() {
        return realmMovieTitle;
    }

    public void setRealmMovieTitle(String realmMovieTitle) {
        this.realmMovieTitle = realmMovieTitle;
    }

    public String getRealmThumbnailUrl() {
        return realmThumbnailUrl;
    }

    public void setRealmThumbnailUrl(String realmThumbnailUrl) {
        this.realmThumbnailUrl = realmThumbnailUrl;
    }




}
