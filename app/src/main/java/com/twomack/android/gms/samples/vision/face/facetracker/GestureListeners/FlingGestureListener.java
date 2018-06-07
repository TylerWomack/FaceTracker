package com.twomack.android.gms.samples.vision.face.facetracker.GestureListeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.twomack.android.gms.samples.vision.face.facetracker.Flinger;
import com.twomack.android.gms.samples.vision.face.facetracker.Treasure;

public class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {

    private Treasure imageView;
    private Flinger fling;

    public FlingGestureListener(Treasure imageView, Flinger fling) {
        this.imageView = imageView;
        this.fling = fling;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        fling.simpleXFling(imageView, velocityX);
        fling.simpleYFling(imageView, velocityY);
        return true;
    }
}