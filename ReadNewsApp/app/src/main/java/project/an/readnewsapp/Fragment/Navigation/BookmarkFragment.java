package project.an.readnewsapp.Fragment.Navigation;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import project.an.readnewsapp.Adapter.BookmarkAdapter;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.Service.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment {

    private RecyclerView bookmarkList;
    private LinearLayout bookmarkEmptyLayout;
    private ScrollView bookmarkNotEmptyLayout;
    private DatabaseHelper databaseHelper;
    private BookmarkAdapter bookmarkAdapter;
    private List<NewsItem> bookmarks;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    private void getControl(View view){
        bookmarkList = view.findViewById(R.id.bookmarkList);
        bookmarkEmptyLayout = view.findViewById(R.id.bookmarkEmptyLayout);
        bookmarkNotEmptyLayout = view.findViewById(R.id.bookmarkNotEmptyLayout);
        databaseHelper = DatabaseHelper.getInstance(getContext());
        bookmarks = new ArrayList<>();
        checkDatabaseStatus();
    }
    // TODO: Rename and change types and number of parameters
    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getControl(view);
        checkDatabaseStatus();
        getBookMarkList();
        bookmarkList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkAdapter = new BookmarkAdapter(getContext(), bookmarks);
        bookmarkList.setAdapter(bookmarkAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        checkDatabaseStatus();
        getBookMarkList();
        bookmarkList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkAdapter = new BookmarkAdapter(getContext(), bookmarks);
        bookmarkList.setAdapter(bookmarkAdapter);
    }

    private void checkDatabaseStatus() {
        if (databaseHelper.isDatabaseEmpty()) {
            Log.i("Bookmark", "Không có danh sách");
            bookmarkNotEmptyLayout.setVisibility(View.GONE);
            bookmarkEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            Log.i("Bookmark", "Có danh sách");
            bookmarkNotEmptyLayout.setVisibility(View.VISIBLE);
            bookmarkEmptyLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("Range")
    private void getBookMarkList(){
        bookmarks.clear();
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            NewsItem newsItem = new NewsItem(
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("image_path")),
                    cursor.getString(cursor.getColumnIndex("pub_date")),
                    cursor.getString(cursor.getColumnIndex("link")),
                    cursor.getString(cursor.getColumnIndex("content"))
            );
            newsItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            bookmarks.add(newsItem);
        }
        cursor.close();
        if(bookmarks != null) Log.i("Bookmark", "Nhận được danh sách");
        else Log.i("Bookmark", "Chưa nhận được danh sách");
    }
}