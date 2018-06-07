package com.twomack.android.gms.samples.vision.face.facetracker;

import android.content.Context;
import android.widget.ImageView;

public class Treasure extends android.support.v7.widget.AppCompatImageView {

    double xSpeed;
    double ySpeed;
    int value;



    //type 1 - coin 2 - diamond 3 - heart
    int type;

    public Treasure(Context context) {
        super(context);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
