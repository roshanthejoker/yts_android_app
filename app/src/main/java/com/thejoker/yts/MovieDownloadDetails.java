package com.thejoker.yts;


public class MovieDownloadDetails {
    private String downloadLink;
    private String quality;
    private String fileSize;
    public MovieDownloadDetails(){}


    public String getQuality() {

        return quality;
    }

    public void setQuality(String quality)
    {
        this.quality = quality;
    }

    public String getFileSize() {

        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {

        this.downloadLink = downloadLink;
    }


    public MovieDownloadDetails(String downloadLink,
                             String quality,
                             String fileSize){
        this.downloadLink = downloadLink;
        this.quality = quality;
        this.fileSize = fileSize;

    }

    @Override
    public String toString() {
        return  "Url "+downloadLink+
                "fileSize "+fileSize+
                "Quality "+quality;
    }
}
