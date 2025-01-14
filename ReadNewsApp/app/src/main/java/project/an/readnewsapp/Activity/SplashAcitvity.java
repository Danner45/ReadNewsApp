package project.an.readnewsapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import project.an.readnewsapp.R;

public class SplashAcitvity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_acitvity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(SplashAcitvity.this, MainActivity.class);
                    // Người dùng đã đăng nhập, chuyển đến MainActivity
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashAcitvity.this, IntroActivity.class);
                    // Người dùng chưa đăng nhập, chuyển đến IntroActivity
                    startActivity(intent);
                    finish();
                }
                finish();
            }
        }, 2000);
    }
}