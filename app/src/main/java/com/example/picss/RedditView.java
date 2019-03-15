package com.example.picss;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picss.model.Feed;
import com.example.picss.model.children.Children;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RedditView extends AppCompatActivity {

    Toolbar toolbar;
    String after="";
    String option="hot";
    private final String TAG="Main Activity";
    String head;


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RedditViewAdapter redditViewAdapter;
    List<Children> child=new ArrayList<>();
    private boolean loading=true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal=0;
    private int visibleThreshold=5;
    ProgressBar spinkit;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        option=item.getTitle().toString();
        after ="";
        previousTotal = 0;
        this.loading = true;
        child.clear();
        redditViewAdapter.notifyDataSetChanged();
        getSubreddit();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        redditViewAdapter=new RedditViewAdapter(this,child);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(redditViewAdapter);
        spinkit=findViewById(R.id.spinkit);


        head=getIntent().getStringExtra("head");
        setTitle(head);

        child.clear();
        redditViewAdapter.notifyDataSetChanged();

        setUpToolbar();
        getSubreddit();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount  = linearLayoutManager.getChildCount();
                totalItemCount  = linearLayoutManager.getItemCount();
                firstVisibleItem  = linearLayoutManager.findFirstVisibleItemPosition();

                if(loading) {
                    if (totalItemCount  > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount ;
                    }
                }

                if(!loading && (totalItemCount -visibleItemCount)<=(firstVisibleItem + visibleThreshold))
                {
                    //Log.d(TAG, "url: End reached");

                    getSubreddit();
                    loading=true;
                }
            }

        });

    }

    private void getSubreddit(){
        //choose="getsubreddit";
        spinkit.setVisibility(View.VISIBLE);
        Call<Feed> feed=RedditAPI.getRedditService().getSubreddit(head,option,after);

        feed.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                Feed feed=response.body();
                after=feed.getData().getAfter();
                //recyclerView.setAdapter(new CustomAdapter(MainActivity.this,feed.getData().getChildren()));
                child.addAll(feed.getData().getChildren());
                redditViewAdapter.notifyDataSetChanged();
                Log.d(TAG, "onResponse: "+after);
                spinkit.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                spinkit.setVisibility(View.GONE);
                Toast.makeText(RedditView.this, "Error: "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
