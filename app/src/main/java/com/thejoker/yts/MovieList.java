package com.thejoker.yts;




public class MovieList  {
    private int id;
    private String title;
    private int year;
    private long rating;
    private String urlThumbnail;

    public MovieList(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public MovieList(
             int id,
             String title,
             int year,
           long rating,
             String urlThumbnail
    ){
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.urlThumbnail = urlThumbnail;

    }

    @Override
    public String toString(){
        return "ID"+id+
                "Title"+title+
                "Year"+year+
                "Rating"+rating+
                "urlThumbnail"+urlThumbnail;

    }

}
