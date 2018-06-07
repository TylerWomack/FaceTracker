package com.twomack.android.gms.samples.vision.face.facetracker;

import android.os.Bundle;
import android.app.Activity;

import com.twomack.android.gms.samples.vision.face.facetracker.R;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
    }


    @Override
    public void onBackPressed()
    {
        finish();
        finishAffinity();
    }
}
