package com.example.picss.model.children;

public class Data {

    private String url;
    private int created_utc;
    private String thumbnail;
    private String subreddit;
    private String domain;




    public String getUrl()
    {
        return url;
    }


    public Data(String url, int created_utc, String thumbnail, String subreddit, String domain) {
        this.url = url;
        this.created_utc = created_utc;
        this.thumbnail = thumbnail;
        this.subreddit = subreddit;
        this.domain=domain;
    }

    public String getSubreddit() {
        return subreddit;
    }
    public String getThumbnail() { return thumbnail; }
    public String getDomain() { return domain; }
}
