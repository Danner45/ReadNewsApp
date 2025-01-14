package project.an.readnewsapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import project.an.readnewsapp.R;

public class IntroActivity extends AppCompatActivity {

    ImageView introGif;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        introGif =  findViewById(R.id.introGif);
        btnStart = findViewById(R.id.btnStart);
        Glide.with(this)
                .asGif()
                .load(R.drawable.intro)
                .into(introGif);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}