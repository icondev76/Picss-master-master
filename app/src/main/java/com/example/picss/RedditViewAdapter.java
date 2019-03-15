package com.example.picss;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.picss.model.children.Children;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class RedditViewAdapter
        extends RecyclerView.Adapter<RedditViewAdapter.CustomViewHolder> {
    private Context context;
    private List<Children> children;

    public RedditViewAdapter(Context context, List<Children> children) {
        this.context = context;
        this.children = children;
    }

    @NonNull
    @Override
    public RedditViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RedditViewAdapter.CustomViewHolder holder, int position) {

        final Children childrenlist = children.get(position);
        final String url = childrenlist.getData().getUrl();

            //holder.starview.setImageResource(R.drawable.ic_star);
            //holder.textView.setText(childrenlist.getData().getSubreddit());

            Glide
                    .with(context)
                    .load(url)
                    .apply(fitCenterTransform()
                            .placeholder(R.color.colorPrimaryDark)
                    )
                    .error(
                            Glide
                                    .with(context)
                                    .load(childrenlist.getData().getThumbnail())
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        //}

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
            }
        });

        /*holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head=holder.textView.getText().toString();
                Intent intent= new Intent(context,RedditView.class);
                intent.putExtra("head",head);
                context.startActivity(intent);
            }
        });*/
   }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        //TextView textView;
        //ImageView starview;
        ProgressBar progressBar;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            //starview = itemView.findViewById(R.id.starview);
            //textView = itemView.findViewById(R.id.textview);
            progressBar=itemView.findViewById(R.id.spinkit_photo);

        }
    }
}
