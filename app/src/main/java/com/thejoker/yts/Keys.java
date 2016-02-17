package com.thejoker.yts;

public interface Keys {

    interface EndPointMovieDetails{

        String KEYS_MOVIE = "movie";
        String KEYS_ID = "id";
        String KEYS_TITLE_LONG = "title_long";
        String KEYS_RATING = "rating";
        String KEYS_DESCRIPTION = "description_full";
        String KEY_YOUTUBE_ID = "yt_trailer_code";
        String KEYS_COVER = "small_cover_image";
        String KEYS_TORRENTS = "torrents";
    }
    interface EndPointMovieList{
        String KEYS_MOVIES = "movies";
        String KEYS_ID = "id";
        String KEYS_TITLE = "title_long";
        String KEYS_RATING ="rating";
        String KEYS_YEAR = "year";
        String KEYS_THUMBNAIL = "medium_cover_image";
    }
}
