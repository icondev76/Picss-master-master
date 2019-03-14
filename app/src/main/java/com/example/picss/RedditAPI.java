package com.example.picss;

import com.example.picss.model.Feed;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RedditAPI {
    public static final String BASE_URL="https://www.reddit.com/r/";

    public static RedditService redditService=null;

    public static RedditService getRedditService()
    {
        if(redditService==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            redditService=retrofit.create(RedditService.class);
        }
        return redditService;
    }

    public interface RedditService{
        /*@GET
        Call<Feed> getFeed(@Url String url);*/


        @GET("{subreddits}/{option}.json")

        Call<Feed> getFeed(@Path("subreddits")String subreddits,
                @Path("option")String option,
                //@Query("limit") int limit),
                @Query("after") String after);

        @GET("{feedname}/{option}.json")

        Call<Feed> getSubreddit(@Path("feedname")String head,
                           //@Query("limit") int limit),
                           @Path("option") String option,
                            @Query("after") String after);
    }

}
