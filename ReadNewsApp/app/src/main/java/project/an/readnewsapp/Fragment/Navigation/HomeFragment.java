package project.an.readnewsapp.Fragment.Navigation;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.an.readnewsapp.Activity.MainActivity;
import project.an.readnewsapp.Fragment.RandomeTopicFragment;
import project.an.readnewsapp.Models.Categories;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.Adapter.CategoryViewPageAdapter;
import project.an.readnewsapp.Service.SharedViewModel;


public class HomeFragment extends Fragment {

    TextInputLayout inputSearchLayout;
    TextInputEditText inputSearch;
    ImageView micro;
    TabLayout tabCategories;
    ViewPager2 viewNewsList;
    List<Categories> categoriesList;
    CategoryViewPageAdapter adapter;
    OkHttpClient client;
    public static int RecordAudioRequestCode = 1;
    public static List<NewsItem> newsList;
    SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getControl(view);
        setupView();
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            int selectedTab = bundle.getInt("selectedTab", 0);
            Log.i("tag mới", String.valueOf(selectedTab));
            if (tabCategories != null) {
                Log.i("chọn tab", "Đã chọn tab:"+selectedTab);
                tabCategories.selectTab(tabCategories.getTabAt(selectedTab));
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setup();
    }

    private void getControl(View view){
        inputSearchLayout = view.findViewById(R.id.inputSearchLayout);
        inputSearch = view.findViewById(R.id.inputSearch);
        micro = view.findViewById(R.id.micro);
        tabCategories = view.findViewById(R.id.tabCategories);
        viewNewsList = view.findViewById(R.id.viewNewsList);
        newsList = new ArrayList<>();
        categoriesList = new ArrayList<>(Arrays.asList(
                new Categories("Random", null),
                new Categories("AI/ML", "https://machinelearningmastery.com/blog/feed/"),
                new Categories("Software", "https://dev.to/feed"),
                new Categories("Technology", "https://www.engadget.com/rss.xml"),
                new Categories("Security", "https://hackernoon.com/feed")
        ));
        setup();
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakNow(v);
            }
        });
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        inputSearch.setOnEditorActionListener(search);
    }

    private void setup(){
        adapter = new CategoryViewPageAdapter(this, categoriesList);
        viewNewsList.setAdapter(adapter);
    }

    private void setupView(){
        new TabLayoutMediator(tabCategories, viewNewsList, (tab, position) -> {
            View customTab = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null);
            TextView tabTitle = customTab.findViewById(R.id.tabTitle);
            tabTitle.setText(categoriesList.get(position).getTitle());
            tab.setCustomView(customTab);
        }).attach();
        tabCategories.addOnTabSelectedListener(tabSelectedListener);
        tabCategories.selectTab(tabCategories.getTabAt((-1)));
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            View customTab = tab.getCustomView();
            if (customTab != null){
                TextView tabTitle = customTab.findViewById(R.id.tabTitle);
                tabTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            View customTab = tab.getCustomView();
            if (customTab != null){
                TextView tabTitle = customTab.findViewById(R.id.tabTitle);
                tabTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.content_color));
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void speakNow(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "start listening...");
        startActivityForResult(intent, 111);
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.RECORD_AUDIO
            }, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RecordAudioRequestCode && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == RESULT_OK){
            inputSearch.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            String query = inputSearch.getText().toString();
            if (!query.isEmpty()) {
                sharedViewModel.setQuery(query);
            }
        }
    }


    TextView.OnEditorActionListener search = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String query = inputSearch.getText().toString();
            if (!query.isEmpty()) {
                sharedViewModel.setQuery(query);
            }
            else sharedViewModel.setQuery(null);
            return true;
        }
    };
}