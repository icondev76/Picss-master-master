 package com.example.picss;


 import android.os.Bundle;
 import android.support.annotation.NonNull;
 import android.support.design.widget.NavigationView;
 import android.support.v4.view.GravityCompat;
 import android.support.v4.widget.DrawerLayout;
 import android.support.v7.app.ActionBarDrawerToggle;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.support.v7.widget.Toolbar;
 import android.util.Log;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.animation.AnimationUtils;
 import android.view.animation.LayoutAnimationController;
 import android.widget.ProgressBar;
 import android.widget.Toast;

 import com.example.picss.model.Feed;
 import com.example.picss.model.children.Children;
 import com.stone.vega.library.VegaLayoutManager;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.LinkedHashSet;
 import java.util.List;
 import java.util.Set;

 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.Response;

 public class MainActivity extends AppCompatActivity {
     String after="";
     String option="hot";
     String subreddits;
     private final String TAG="Main Activity";
     Toolbar toolbar;

     RecyclerView recyclerView;
     LinearLayoutManager linearLayoutManager;
     int resId=R.anim.layout_animation;
     CustomAdapter customAdapter;
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
         toolbar.setSubtitle(option);
         child.clear();
         customAdapter.notifyDataSetChanged();
         getData();
         return super.onOptionsItemSelected(item);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        customAdapter=new CustomAdapter(this,child);
        recyclerView.setLayoutManager(linearLayoutManager);

        //LayoutAnimationController animationController= AnimationUtils.loadLayoutAnimation(this,resId);
        //recyclerView.setLayoutAnimation(animationController);
        recyclerView.setAdapter(customAdapter);
        spinkit=findViewById(R.id.spinkit);

        setUpToolbar();
        getsubreddits();
        getData();


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
                   getData();
                   loading=true;
               }
            }

        });
    }

     private void getData(){

        spinkit.setVisibility(View.VISIBLE);
         Call<Feed> feed=RedditAPI.getRedditService().getFeed(subreddits,option,after);

         feed.enqueue(new Callback<Feed>() {
             @Override
             public void onResponse(Call<Feed> call, Response<Feed> response) {

                 Feed feed=response.body();
                 after=feed.getData().getAfter();
                 //recyclerView.setAdapter(new CustomAdapter(MainActivity.this,feed.getData().getChildren()));
                 child.addAll(feed.getData().getChildren());

                 Set<Children> unique = new LinkedHashSet<Children>(child);
                 child.clear();
                 child.addAll(unique);

                 customAdapter.notifyDataSetChanged();
                 //Log.d(TAG, "onResponse: "+after);
                 spinkit.setVisibility(View.GONE);
             }

             @Override
             public void onFailure(Call<Feed> call, Throwable t) {
                 spinkit.setVisibility(View.GONE);
                 Toast.makeText(MainActivity.this, "Error: "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
             }
         });
     }

     private void setUpToolbar(){
         toolbar=findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         toolbar.setSubtitle(option);
     }

     private void getsubreddits(){

            BufferedReader bufferedReader = null;
         try {
             bufferedReader = new BufferedReader(new InputStreamReader(this.getAssets().open("subs.txt"), "UTF-8"));
             subreddits = bufferedReader.readLine();

         } catch (IOException e) {
             e.printStackTrace();
         }

         finally {
             if(bufferedReader != null){
                 try {
                     bufferedReader.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
}
