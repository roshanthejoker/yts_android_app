package com.thejoker.yts;


public class MovieCastDetails {
    private String name;
    private String imageUrl;
    private String characterName;
    private String imdbId;
    
    public MovieCastDetails(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    @Override
    public String toString() {
        return "Name "+name+
                "imdbId"+imdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public MovieCastDetails(String name,String imageUrl,String characterName,String imdbId){
        this.name=name;
        this.imageUrl=imageUrl;
        this.characterName=characterName;
        this.imdbId=imdbId;
    }
}
