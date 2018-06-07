package com.twomack.android.gms.samples.vision.face.facetracker;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;

public class Flinger {

    static final float friction = .7f;

    //same as a normal flingX(), except it doesn't splinter into new stars when it hits the walls, and there are no checks for interactions with other objects
    public void simpleXFling(final Treasure object, final float velocityX) {
        final FlingAnimation flingX = new FlingAnimation(object, DynamicAnimation.X);
        flingX.setStartVelocity(velocityX)
                .setFriction(friction)
                .start();
    }

    //same as a normal flingY(), except it doesn't splinter into new stars when it hits the walls, and there are no checks for interactions with other objects
    public void simpleYFling(final Treasure object, final float velocityY) {
        final FlingAnimation flingY = new FlingAnimation(object, DynamicAnimation.Y);
        flingY.setStartVelocity(velocityY)
                .setFriction(friction)
                .start();
    }

    public void flingHeart(final Treasure object, final float velocityY){
        final FlingAnimation flingY = new FlingAnimation(object, DynamicAnimation.Y);
        flingY.setStartVelocity(velocityY)
                .setFriction(.001f)
                .start();
    }
}
