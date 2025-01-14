package project.an.readnewsapp.Fragment.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import project.an.readnewsapp.R;
import project.an.readnewsapp.Service.DatabaseHelper;
import project.an.readnewsapp.Service.NewsCheckWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private SeekBar seekBarTextSize;
    private TextView textSizeNumber, checkTextSize;
    private SharedPreferences sharedTextSize, sharedCheckNotify;
    private SwitchCompat switchNotification;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seekBarTextSize = view.findViewById(R.id.seekBarTextSize);
        textSizeNumber = view.findViewById(R.id.textSizeNumber);
        checkTextSize = view.findViewById(R.id.checkTextSize);
        switchNotification = view.findViewById(R.id.switchNotification);
        sharedTextSize = view.getContext().getSharedPreferences("ReadNews", Context.MODE_PRIVATE);
        sharedCheckNotify = view.getContext().getSharedPreferences("ReadNews", Context.MODE_PRIVATE);
        boolean isNotificationEnabled = sharedCheckNotify.getBoolean("notificationEnabled", true);
        switchNotification.setChecked(isNotificationEnabled);
        int textSize = sharedTextSize.getInt("textSize", 16);
        textSizeNumber.setText(String.valueOf(textSize));
        checkTextSize.setTextSize(textSize);
        seekBarTextSize.setProgress(textSize);
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newSize = progress;
                SharedPreferences.Editor editor = sharedTextSize.edit();
                textSizeNumber.setText(String.valueOf(newSize));
                checkTextSize.setTextSize(newSize);
                editor.putInt("textSize", newSize);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedTextSize.edit();
                editor.putBoolean("notificationEnabled", isChecked);
                editor.apply();
                if (isChecked){
                    registerNotification();
                }
                else unregisterNotification();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void registerNotification() {
        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(
                NewsCheckWorker.class, 15, TimeUnit.MINUTES
        ).build();
        WorkManager.getInstance(getContext()).enqueue(notificationWork);
    }

    private void unregisterNotification() {
        WorkManager.getInstance(getContext()).cancelAllWork();
    }
}