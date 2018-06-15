package com.twomack.android.gms.samples.vision.face.facetracker;

import android.content.Context;
import android.widget.ImageView;

public class Treasure extends android.support.v7.widget.AppCompatImageView {

    double xSpeed;
    double ySpeed;
    int value;
    int timesStruck;



    //type 1 - coin 2 - diamond 3 - heart 4 - barrel
    int type;

    public Treasure(Context context) {
        super(context);
        this.value = value;
        timesStruck = 0;
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

    public int getTimesStruck() {
        return timesStruck;
    }

    public void setTimesStruck(int timesStruck) {
        this.timesStruck = timesStruck;
    }


}
