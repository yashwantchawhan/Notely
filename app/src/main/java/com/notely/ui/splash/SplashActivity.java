package com.notely.ui.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.notely.R;
import com.notely.ui.add.AddNoteActivity;
import com.notely.ui.list.ListNotesActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, ListNotesActivity.class);
            startActivity(intent,
                    ActivityOptions.makeCustomAnimation(this,
                            R.anim.slide_in_right_medium,
                            R.anim.slide_out_left_medium).toBundle()
            );
            // close this activity
            finish();
        }, SPLASH_TIME_OUT);
    }
}
