package project.an.readnewsapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.N;

import java.util.List;

import project.an.readnewsapp.Adapter.NewsListAdapter;
import project.an.readnewsapp.Fragment.Navigation.HomeFragment;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.RSSUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment {

    private static final String ARG_URL = "category_url";
    private static final String ARG_NAME_CAT = "category_name";
    private String categoryUrl;
    private String categoryName;
    private RecyclerView recyclerView;
    private ProgressBar progressBarNewsList;
    private NewsListAdapter adapter;
    private boolean isLoading = false;

    public static NewsListFragment newInstance(String url, String name) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_NAME_CAT, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryUrl = getArguments().getString(ARG_URL);
            categoryName = getArguments().getString(ARG_NAME_CAT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBarNewsList = view.findViewById(R.id.progressBarNewsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBarNewsList.setVisibility(View.VISIBLE);
        // Tải dữ liệu RSS
        new Thread(() -> {
            try {
                String rssData = RSSUtils.fetchRSS(categoryUrl);
                if(!rssData.isEmpty()) Log.i("Lấy Rss", "Đã lấy được RSS " + categoryName);
                else Log.i("Lấy Rss", "Chưa lấy được RSS "+categoryName);
                List<NewsItem> rssItems = RSSUtils.parseRSS(rssData);
                HomeFragment.newsList.addAll(rssItems);
                FragmentActivity activity = getActivity();
                if(activity!=null){
                    getActivity().runOnUiThread(() -> {
                        progressBarNewsList.setVisibility(View.GONE);
                        NewsListAdapter adapter = new NewsListAdapter(rssItems, getContext(), categoryName);
                        recyclerView.setAdapter(adapter);
                    });
                    Log.d("RSSFragment", "RSS Data: " + rssData);
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    // Ẩn ProgressBar nếu có lỗi
                    progressBarNewsList.setVisibility(View.GONE);
                });
            }
        }).start();

        return view;
    }


}