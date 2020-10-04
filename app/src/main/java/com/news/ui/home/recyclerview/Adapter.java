package com.news.ui.home.recyclerview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.news.R;
import com.news.ui.model.Article;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Article> list;

    public Adapter(List<Article> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind((Article) list.get(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private ImageView imageView;
        private NavController navController;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            navController = Navigation.findNavController((Activity) itemView.getContext(), R.id.nav_host_fragment);
            title = itemView.findViewById(R.id.item_tv_title);
            description = itemView.findViewById(R.id.item_tv_description);
            imageView = itemView.findViewById(R.id.item_image);
        }

        public void onBind(Article article) {
            title.setText(article.getTitle());
            description.setText(article.getDescription());
            setUpImage(article);
            itemView.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putString("title", article.getTitle());
                args.putString("desc", article.getDescription());
                args.putString("data", article.getPublishedAt());
                args.putString("url", article.getUrl());
                args.putString("author", article.getAuthor());
                args.putString("image", article.getUrlToImage());
                navController.navigate(R.id.detailFragment,args);
            });
        }

        private void setUpImage(Article article) {
            final ProgressBar progressBar;
            progressBar = itemView.findViewById(R.id.progress_bar);
            RequestOptions requestOptions = new RequestOptions();
            Glide.with(itemView)
                    .load(article.getUrlToImage())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        }
    }

}


