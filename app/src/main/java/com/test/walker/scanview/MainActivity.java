package com.test.walker.scanview;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View viewById = findViewById(R.id.sv);

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(viewById,"sweepAngle",0,360);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(100);
        objectAnimator.start();
    }
}
