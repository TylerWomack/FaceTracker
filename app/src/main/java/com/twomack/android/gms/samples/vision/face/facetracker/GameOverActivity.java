package com.twomack.android.gms.samples.vision.face.facetracker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;

import com.twomack.android.gms.samples.vision.face.facetracker.R;

public class GameOverActivity extends Activity {

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.young_love);
        mPlayer.start();
    }


    @Override
    public void onBackPressed()
    {
        finish();
        finishAffinity();
        mPlayer.stop();
    }

    @Override
    public void onPause(){
        super.onPause();
        mPlayer.stop();
    }


}
