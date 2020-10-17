package com.news.ui.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.news.R;
import com.news.Utils;
import com.news.ui.api.NewsApiClient;
import com.news.ui.detailFragment.DetailFragment;
import com.news.ui.home.recyclerview.Adapter;
import com.news.ui.model.Article;
import com.news.ui.model.News;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private Adapter adapter;
    private View root;
    private ConstraintLayout internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Article> articleArrayList;
    private ImageView internet_check_image;
    private Button btn_try_again;
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private boolean isLoading = false;
    int pageSize = 10;
    int page = 1;

    public static final String API_KEY = "a20bce6e5bce44daa29fca6eeb55b261";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchCurrentNews());
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        internet = root.findViewById(R.id.internet);
        internet_check_image = root.findViewById(R.id.image_dis);
        btn_try_again = root.findViewById(R.id.try_again);
        setUpAdapter();
        onLoadingSwipeRefresh();
        updateDown();
        return root;
    }

    private void updateDown() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        isLoading = true;
                        page++;
                        pageSize = +10;
                        NewsApiClient.getService().getNewsDown(Utils.getCountry(), API_KEY, page, pageSize).enqueue(new Callback<News>() {
                            @Override
                            public void onResponse(Call<News> call, Response<News> response) {
                                assert response.body() != null;
                                adapter.addArticle(response.body().getArticle());
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            }

                            @Override
                            public void onFailure(Call<News> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        };
        recyclerView.setOnScrollListener(scrollListener);
    }

    private void fetchCurrentNews() {
        internet.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        keywordDown();
    }

    private void onLoadingSwipeRefresh() {
        swipeRefreshLayout.post(
                () -> fetchCurrentNews()
        );
    }

    private void keywordDown() {
        NewsApiClient.getService().getNewsDown(Utils.getCountry(), API_KEY, page, pageSize).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    articleArrayList = response.body().getArticle();
                    adapter.addArticle(articleArrayList);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorMessage(
                            R.drawable.ic_enternet);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.ic_enternet);
            }
        });

    }

    private void showErrorMessage(int imageView) {
        recyclerView.setVisibility(View.GONE);
        if (internet.getVisibility() == View.GONE) {
            internet.setVisibility(View.VISIBLE);
        }
        internet_check_image.setImageResource(imageView);
        btn_try_again.setOnClickListener(v -> onLoadingSwipeRefresh());

    }

    private void setUpAdapter() {
        recyclerView = root.findViewById(R.id.fh_recycler_view);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

}