package project.an.readnewsapp.Activity;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import java.util.concurrent.TimeUnit;

import project.an.readnewsapp.Fragment.Navigation.BookmarkFragment;
import project.an.readnewsapp.Fragment.Navigation.CategoriesFragment;
import project.an.readnewsapp.Fragment.Navigation.HomeFragment;
import project.an.readnewsapp.Fragment.Navigation.ProfileFragment;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.Service.DatabaseHelper;
import project.an.readnewsapp.Service.NewsCheckWorker;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "CHANNEL_1";
    ImageView homeNav, categoriesNav, bookmarkNav, profileNav;
    LinearLayout homeNavLayout, categoriesNavLayout, bookmarkNavLayout, profileNavLayout;
    DatabaseHelper databaseHelper;

    private void getControl(){
        homeNav = findViewById(R.id.homeNav);
        categoriesNav = findViewById(R.id.categoriesNav);
        bookmarkNav = findViewById(R.id.bookmarkNav);
        profileNav = findViewById(R.id.profileNav);
        homeNavLayout = findViewById(R.id.homeNavLayout);
        categoriesNavLayout = findViewById(R.id.categoriesNavLayout);
        bookmarkNavLayout = findViewById(R.id.bookmarkNavLayout);
        profileNavLayout = findViewById(R.id.profileNavLayout);
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getControl();
        homeNav.setImageResource(R.drawable.icon_nav_home_choose);
        replaceFragment(new HomeFragment());
        homeNavLayout.setOnClickListener(homeClick);
        categoriesNavLayout.setOnClickListener(categoriesClick);
        bookmarkNavLayout.setOnClickListener(bookmarkClick);
        profileNavLayout.setOnClickListener(profileClick);
        sendNotification();
        PeriodicWorkRequest notificationWork =
                new PeriodicWorkRequest.Builder(NewsCheckWorker.class, 15, TimeUnit.MINUTES)
                        .build();

        // Đăng ký công việc với WorkManager
        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDefaultIcon();
            homeNav.setImageResource(R.drawable.icon_nav_home_choose);
            replaceFragment(new HomeFragment());
        }
    };
    View.OnClickListener categoriesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDefaultIcon();
            categoriesNav.setImageResource(R.drawable.icon_nav__categories_choose);
            replaceFragment(new CategoriesFragment());
        }
    };
    View.OnClickListener bookmarkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDefaultIcon();
            bookmarkNav.setImageResource(R.drawable.icon_nav_bookmark_choose);
            replaceFragment(new BookmarkFragment());
        }
    };
    View.OnClickListener profileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDefaultIcon();
            profileNav.setImageResource(R.drawable.icon_nav_profile_choose);
            replaceFragment(new ProfileFragment());
        }
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (existingFragment != null) {
            // Fragment đã tồn tại, hiển thị lại
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, existingFragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();
        } else {
            // Tạo mới Fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setDefaultIcon(){
        homeNav.setImageResource(R.drawable.icon_nav_home);
        categoriesNav.setImageResource(R.drawable.icon_nav_categories);
        bookmarkNav.setImageResource(R.drawable.icon_bookmark);
        profileNav.setImageResource(R.drawable.icon_nav_profile);
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Atech notification")
                .setContentText("Welcome to Atech")
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        notificationManager.notify(1, builder.build());
    }

}