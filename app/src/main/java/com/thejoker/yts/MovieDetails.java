package com.thejoker.yts;

/**
 * Created by thejoker on 2/15/2016.
 */
public class MovieDetails {
    private int id ;
    private String title ;
    private String Summary ;
    private int year;
    private long rating;
    private String youtube_id;
    private String urlThumbnail;

    public MovieDetails(){

    }



    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieDetails(
             int id ,
            String title ,
            String Summary ,
             int year,
             long rating,
             String youtube_id,
             String urlThumbnail

    )
    {
        this.id = id;
        this.title = title;
        this.Summary = Summary;
        this.year = year;
        this.rating = rating;
        this.youtube_id = youtube_id;
        this.urlThumbnail = urlThumbnail;

    }
    @Override
    public String toString(){
        return "ID "+id+
                "Title "+title+
                "Summary "+Summary+
                "Year "+year+
                "Rating "+rating;

    }
}
