package com.news.ui.detailFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.news.R;
import com.news.Utils;

public class DetailFragment extends Fragment {
    private View v;
    private TextView desc, title, data, author, time;
    private Button btn_click_url;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        desc = v.findViewById(R.id.da_description);
        title = v.findViewById(R.id.da_title);
        imageView = v.findViewById(R.id.da_image);
        data = v.findViewById(R.id.tv_data);
        author = v.findViewById(R.id.tv_author);
        btn_click_url = v.findViewById(R.id.btn_url);
        time = v.findViewById(R.id.tv_time);
        init();
        return v;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        title.setText(getArguments().getString("title"));
        desc.setText(getArguments().getString("desc"));
        btn_click_url.setOnClickListener(view -> {
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("url")));
            startActivity(browserIntent);
        });
        if (getArguments().getString("author") != null)
            author.setText(" \u2022 " + "Автор : " + getArguments().getString("author"));
        else author.setText(" \u2022 " + "Автор : неизвестен");
        time.setText(" \u2022 " + " Время публикации " + Utils.DateToTimeFormat(getArguments().getString("data")));
        data.setText(" \u2022 " + " Дата публикации " + Utils.DateFormat(getArguments().getString("data")));
        Glide.with(getContext()).load(getArguments().getString("image")).into(imageView);
    }

}