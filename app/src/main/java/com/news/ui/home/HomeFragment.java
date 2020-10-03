package com.news.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private Adapter adapter;
    private View root;
    private RecyclerView recyclerView;
    private ConstraintLayout internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Article> articleArrayList = new ArrayList<>();
    private ImageView internet_check_image;
    private Button btn_try_again;

    public static final String API_KEY = "a20bce6e5bce44daa29fca6eeb55b261";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchCurrentNews(""));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        internet = root.findViewById(R.id.internet);
        internet_check_image = root.findViewById(R.id.image_dis);
        btn_try_again = root.findViewById(R.id.try_again);
        setUpAdapter();
        onLoadingSwipeRefresh("");
        return root;
    }

    private void fetchCurrentNews(String keyword) {
        internet.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        keywordDown();
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(
                () -> fetchCurrentNews(keyword)
        );

    }

    private void keywordDown() {
        NewsApiClient.getService().getNews(Utils.getCountry(), API_KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleArrayList = response.body().getArticle();
                    adapter = new Adapter(articleArrayList);
                    recyclerView.setAdapter(adapter);
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
        if (internet.getVisibility() == View.GONE) {
            internet.setVisibility(View.VISIBLE);
        }
        internet_check_image.setImageResource(imageView);
        btn_try_again.setOnClickListener(v -> onLoadingSwipeRefresh(""));

    }

    private void setUpAdapter() {
        recyclerView = root.findViewById(R.id.fh_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

}