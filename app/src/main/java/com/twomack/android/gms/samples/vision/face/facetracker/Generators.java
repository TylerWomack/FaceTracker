package com.twomack.android.gms.samples.vision.face.facetracker;

import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class Generators {

    Flinger fling;
    Handler handler;
    FaceTrackerActivity mainActivity;
    Random random;
    final static int medusaReactionTime = 700;
    final static int deathReactionTime = 900;
    final static int kingDisplayTime = 3000;
    boolean active = false;
    boolean canAttack = true;
    boolean enemyPresent = false;
    int yourDamage = 5;
    int diamondZoneSize = 0;
    int heartZoneSize = 0;
    int barrelZoneSize = 0;
    int treasureDelay = 500;
    int treasureDisappearTime = 5000;
    int treasureMultiplier = 1;
    int maxSpeed = 4000;
    boolean shieldUsed = false;
    boolean invincible = false;
    boolean hourglassAvailable = false;
    final int endTime = 1;
    Vibrator vibrator;


    int currentLevel;
    ArrayList<String> items;



    MutableLiveData<Integer> treasure = new MutableLiveData<>();
    MutableLiveData<Integer> hearts = new MutableLiveData<>();


    public Generators(Flinger fling, FaceTrackerActivity mainActivity, int level, int hearts, int treasure, final ArrayList<String> items) {
        this.fling = fling;
        handler = new Handler();
        this.mainActivity = mainActivity;
        random = new Random();
        setDamageListener();

        vibrator = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);


        this.currentLevel = level;
        this.treasure.setValue(treasure);
        this.hearts.setValue(hearts);
        this.items = items;



        if (currentLevel > 1){
            diamondZoneSize = 6;
        }

        if (currentLevel > 2){
            heartZoneSize = 3;
        }

        if (currentLevel > 3){
            barrelZoneSize = 6;
        }


        if(items != null){
            if (items.contains("diamond")) {

                diamondZoneSize = Math.max(diamondZoneSize * 2, 6);
            }

            if (items.contains("heart")) {
                heartZoneSize = Math.max(heartZoneSize * 2, 3);
            }

            if (items.contains("sword")) {
                yourDamage = yourDamage * 2;
            }

            if (items.contains("shield")) {

                mainActivity.findViewById(R.id.shield).setVisibility(View.VISIBLE);
                mainActivity.findViewById(R.id.shield).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!shieldUsed)
                            useShield();
                    }
                });
            }

            if (items.contains("hourglass")){
                hourglassAvailable = true;
                mainActivity.findViewById(R.id.mainHourGlass).setVisibility(View.VISIBLE);
                mainActivity.findViewById(R.id.mainHourGlass).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(hourglassAvailable)
                            useHourglass();
                    }
                });
            }
        }
    }

    public void activateGenerator(){
        active = true;
    }

    private void deactivateGenerator(){
        active = false;
    }

    public void startTreasureGenerator() {

        if (!active)
            return;

        final Runnable r = new Runnable() {
            public void run() {
                createNewTreasure();
                startTreasureGenerator();
            }
        };
        handler.postDelayed(r, treasureDelay);
        if (mainActivity.getTreasureList().size() > 100){
            mainActivity.getMainLayout().removeView(mainActivity.getTreasureList().get(0));
            mainActivity.getTreasureList().remove(mainActivity.getTreasureList().get(0));
        }
    }

    public MutableLiveData<Integer> getTreasureData() {
        return treasure;
    }

    public MutableLiveData<Integer> getHeartsData() {
        return hearts;
    }

    public void startCharacterGenerator(){

        if (!active)
            return;


        int upperbound = 15000;
        int lowerbound = 2000;
        Random rand = new Random();
        int random_integer = rand.nextInt(upperbound-lowerbound) + lowerbound;


        final Runnable r = new Runnable() {
            public void run() {

                startRandomCharacterEvent();
                startCharacterGenerator();
            }
        };
        handler.postDelayed(r, random_integer);
    }

    private void startRandomCharacterEvent(){

        if (!active)
            return;

        if (currentLevel == 1){
            medusaEvent();
        }else if (currentLevel == 2){
            if (random.nextBoolean()){
                medusaEvent();
            }else{
                kingEvent();
            }
        }else if (currentLevel > 2){
            int randomInt = random.nextInt(3);
            if (randomInt == 0)
                medusaEvent();
            if (randomInt == 1)
                kingEvent();
            if (randomInt == 2)
                deathEvent();
        }
    }

    private void medusaDamageSpecialEffects(){
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(200); // for 500 ms
        }

        final Runnable r = new Runnable() {
            public void run() {
                mainActivity.findViewById(R.id.explosion).setVisibility(View.INVISIBLE);
            }
        };
        handler.postDelayed(r, 500);
        mainActivity.findViewById(R.id.explosion).setVisibility(View.VISIBLE);
    }


    public void startLevelTimer(){

        if (!active)
            return;

        final Runnable r = new Runnable() {
            public void run() {
                int secondsRemaining = mainActivity.getSecondsRemaining();
                if (secondsRemaining == endTime){
                    endLevel();
                }
                mainActivity.setSecondsRemaining(secondsRemaining - 1);
                startLevelTimer();
            }
        };
        handler.postDelayed(r, 1000);
    }

    private void endLevel(){
        deactivateGenerator();
        Intent intent = new Intent(mainActivity, LevelEndActivity.class);
        intent.putExtra("treasure", treasure.getValue());
        intent.putExtra("heartsLeft", hearts.getValue());
        intent.putExtra("level", currentLevel);
        intent.putStringArrayListExtra("items", items);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    private void medusaConsequence(){
        if (invincible)
            return;
        removeHeart();
        medusaDamageSpecialEffects();
    }

    private void deathConsequence(){
        removeHeart();
        medusaDamageSpecialEffects();
    }

    private void medusaEvent(){

        if (!active)
            return;

        addMedusaToScreen();

        final Runnable x = new Runnable() {
            public void run() {
                float score = mainActivity.getEyesScore();
                if (score > .75 || score == -2.0 || score == 0.0){
                    medusaConsequence();
                }
            }
        };
        handler.postDelayed(x, medusaReactionTime);

        final Runnable r = new Runnable() {
            public void run() {
                removeMedusa();
            }
        };
        handler.postDelayed(r, medusaReactionTime);
    }

    private void kingEvent(){

        if (!active)
            return;

        addKing();

        final Runnable r = new Runnable() {
            public void run() {
                removeKing();
            }
        };
        handler.postDelayed(r, kingDisplayTime);
    }

    private void deathEvent(){

        if (!active)
            return;

        addDeathToScreen();

        final Runnable x = new Runnable() {
            public void run() {
                float score = mainActivity.getSmilingProbability();
                if (score < .25 || score == -1.0 || score == 0.0){
                    deathConsequence();
                }
            }
        };
        handler.postDelayed(x, deathReactionTime);

        final Runnable r = new Runnable() {
            public void run() {
                removeDeath();
            }
        };
        handler.postDelayed(r, deathReactionTime);
    }

    private void removeMedusa(){
        mainActivity.setBackground(currentLevel);
        //mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.jungle));
        enemyPresent = false;
    }

    private void removeDeath(){
        mainActivity.setBackground(currentLevel);
        //mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.jungle));
        enemyPresent = false;
    }


    private void addMedusaToScreen(){
        mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.medusa));
        enemyPresent = true;
    }

    private void addDeathToScreen(){
        mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.death));
        enemyPresent = true;
    }

    private void addKing(){
        mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.king));
        heartZoneSize = heartZoneSize * 2;
        diamondZoneSize = diamondZoneSize * 2;
        treasureDelay = treasureDelay/2;
        treasureMultiplier = 2;
    }

    private void removeKing(){
        mainActivity.setBackground(currentLevel);
        //mainActivity.getBackground().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.jungle));
        heartZoneSize = heartZoneSize / 2;
        diamondZoneSize = diamondZoneSize / 2;
        treasureDelay = treasureDelay * 2;
        treasureMultiplier = 1;
    }

    private void damageEnemy(){
        ProgressBar progressBar = mainActivity.findViewById(R.id.enemyHealth);
        if (progressBar.getProgress() > 4){
            progressBar.setProgress(progressBar.getProgress() - yourDamage);
            yourDamageSpecialEffects();
        }else {
            victory();
        }
    }

    private void yourDamageSpecialEffects(){
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(200);
        }

        final Runnable r = new Runnable() {
            public void run() {
                mainActivity.findViewById(R.id.explosion2).setVisibility(View.INVISIBLE);
                mainActivity.findViewById(R.id.explosion3).setVisibility(View.INVISIBLE);
                mainActivity.findViewById(R.id.explosion4).setVisibility(View.INVISIBLE);
            }
        };
        handler.postDelayed(r, 50);
        mainActivity.findViewById(R.id.explosion2).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.explosion3).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.explosion4).setVisibility(View.VISIBLE);
    }

    private void useShield(){
        mainActivity.findViewById(R.id.shield).setVisibility(View.INVISIBLE);
        shieldUsed = true;
        mainActivity.getBackground().setColorFilter(R.color.blue);

        final Runnable r = new Runnable() {
            public void run() {
                invincible = false;
                mainActivity.getBackground().setColorFilter(null);
            }
        };
        handler.postDelayed(r, 15000);
        invincible = true;

    }

    private void setDamageListener(){
        mainActivity.findViewById(R.id.topLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canAttack && enemyPresent){
                    damageEnemy();
                }
            }
        });
    }

    private void removeHeart(){

        if (invincible)
            return;

        if (hearts.getValue() == null)
            return;

        if (hearts.getValue() > 1){
            hearts.setValue(hearts.getValue() - 1);
        }else{
            gameOver();
        }
    }

    private void addHeart(){
        if (hearts.getValue() == null)
            return;
        hearts.setValue(hearts.getValue() + 1);
    }

    private void gameOver(){
        deactivateGenerator();
        Intent intent = new Intent(mainActivity, GameOverActivity.class);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    private void freezeTime(){
        deactivateGenerator();
    }

    private void unfreezeTime(){
        activateGenerator();
        startCharacterGenerator();
        startLevelTimer();
        startTreasureGenerator();

    }

    private void useHourglass(){
        hourglassAvailable = false;
        mainActivity.findViewById(R.id.mainHourGlass).setVisibility(View.INVISIBLE);

        final Runnable r = new Runnable() {
            public void run() {
                unfreezeTime();
            }
        };
        handler.postDelayed(r, 5000);
        freezeTime();
    }

    private void victory(){
        Intent intent = new Intent(mainActivity, VictoryActivity.class);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    private void createNewTreasure(){
        float x = Math.min(mainActivity.screenWidth * random.nextFloat(), mainActivity.screenWidth - 100);
        float y;

        if (random.nextBoolean()){
            y = 0;
        }else {
            y = mainActivity.screenHeight;
        }


        createNewTreasure(x, y);
    }

    private void createNewTreasure(float x, float y){

        float velocityY;
        float velocityX = 0;

        float ySpeed;

        if (y > 0){
            ySpeed = -maxSpeed;
        }else {
            ySpeed = maxSpeed;
        }


        velocityY = ySpeed * random.nextFloat();


        Treasure newTreasure = new Treasure(mainActivity);
        mainActivity.getTreasureList().add(newTreasure);
        setType(newTreasure);
        addTreasureToScreen(newTreasure, x, y);

        newTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treasureClicked(v);
            }
        });
        if (newTreasure.getType() != 3){
            setInitialMotion(newTreasure, velocityY, velocityX);
            startTreasureTimer(newTreasure);
        }else {
            flingHeart(newTreasure, velocityY);
        }
    }

    private void createNewTreasureFromBarrel(float x, float y){

        float velocityY;
        float velocityX;

        float ySpeed;
        float xSpeed;

        if (y > 0){
            ySpeed = -maxSpeed;
        }else {
            ySpeed = maxSpeed;
        }

        if(random.nextBoolean()){
            xSpeed = maxSpeed;
        }else {
            xSpeed = -maxSpeed;
        }


        velocityY = (ySpeed * random.nextFloat()/3);
        velocityX = (xSpeed * random.nextFloat()/3);

        Treasure newTreasure = new Treasure(mainActivity);
        mainActivity.getTreasureList().add(newTreasure);


        int randomInt = random.nextInt(10);

        if (randomInt < 4){
            setType(newTreasure, 1);
        }else if(randomInt < 9){
            setType(newTreasure, 2);
        }else{
            setType(newTreasure, 3);
        }

        addTreasureToScreen(newTreasure, x, y);

        newTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treasureClicked(v);
            }
        });

            setInitialMotion(newTreasure, velocityY, velocityX);
            startTreasureTimer(newTreasure);
    }

    private void treasureClicked(View v){

        Treasure treasure = (Treasure) v;

        if (treasure.getType() != 4){
            addToTreasureScore(treasure);
            if (treasure.getType() == 3){
                addHeart();
            }
            mainActivity.getTreasureList().remove(treasure);
            mainActivity.getMainLayout().removeView(v);
        }else{
            barrelStrike(treasure);
        }
    }

    private void damageSpecialEffect(Treasure treasure){
        final ImageView imageView = new ImageView(mainActivity);
        imageView.setImageResource(R.drawable.explosion);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(120, 120);

        final ConstraintLayout view = (ConstraintLayout) mainActivity.findViewById(R.id.topLayout);


        view.addView(imageView, -1, lp);
        imageView.setX(treasure.getX());
        imageView.setY(treasure.getY());

        final Runnable translucent = new Runnable() {
            public void run() {
                view.removeView(imageView);
            }
        };
        handler.postDelayed(translucent,  100);
    }

    private void barrelStrike(Treasure treasure){

        if (treasure.getTimesStruck() < 8){
            treasure.setTimesStruck(treasure.getTimesStruck() + 1);
            damageSpecialEffect(treasure);


            if (treasure.getTimesStruck() == 1 || treasure.getTimesStruck() == 2) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(20);
                }
            }

            if (treasure.getTimesStruck() == 2 || treasure.getTimesStruck() == 3) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(20);
                }
                treasure.setColorFilter(R.color.lightestGrey);
            }

            if (treasure.getTimesStruck() == 4 || treasure.getTimesStruck() == 5) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(30);
                }
                treasure.setColorFilter(R.color.black);
            }

            if (treasure.getTimesStruck() == 6 || treasure.getTimesStruck() == 7) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(30);
                }
            }

        }else {
            breakBarrel(treasure);
        }
    }

    private void breakBarrel(Treasure treasure){

        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());
        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());
        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());
        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());
        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());
        createNewTreasureFromBarrel(treasure.getX(), treasure.getY());

        mainActivity.getTreasureList().remove(treasure);
        mainActivity.getMainLayout().removeView(treasure);
    }

    private void startTreasureTimer(final Treasure treasure){

        final Runnable translucent = new Runnable() {
            public void run() {

                if (!active)
                    return;

                treasure.setColorFilter(R.color.translucentWhite);
                treasure.setImageAlpha(200);
            }
        };
        handler.postDelayed(translucent,  (treasureDisappearTime - treasureDisappearTime/5));


        final Runnable r = new Runnable() {
            public void run() {

                if (!active)
                    return;

                mainActivity.getTreasureList().remove(treasure);
                mainActivity.getMainLayout().removeView(treasure);
            }
        };
        handler.postDelayed(r, treasureDisappearTime);




    }

    private void addToTreasureScore(Treasure treasure){

        if (this.treasure.getValue() == null)
            return;

        if (treasure.getType() == 1 || treasure.getType() == 2)
        showScoreAdded((treasure.getValue() * treasureMultiplier), treasure.getX(), treasure.getY());

        this.treasure.setValue(this.treasure.getValue() + (treasure.getValue() * treasureMultiplier));
    }

    private void showScoreAdded(int value, float x, float y){
        final TextView textView = new TextView(mainActivity);


        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(120, 120);

        ConstraintLayout view = (ConstraintLayout) mainActivity.findViewById(R.id.topLayout);

        textView.setText("+ " + String.valueOf(value));
        textView.setTextColor(mainActivity.getResources().getColor(R.color.golden));
        view.addView(textView, -1, lp);

        textView.setX(x);
        textView.setY(y);


        final Runnable r = new Runnable() {
            public void run() {
                mainActivity.getMainLayout().removeView(textView);
            }
        };
        handler.postDelayed(r, 300);


    }

    private void setType(Treasure newTreasure){
        setType(newTreasure, -1);
    }

    private void setType(Treasure newTreasure, int type){
        int randomInt = 0;

        if (type == -1) {
            randomInt = random.nextInt(100);
        }else if (type == 1){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.coin));
            newTreasure.setValue(1);
            newTreasure.setType(1);
            return;
        }else if (type == 2){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.diamondtransparent));
            newTreasure.setValue(5);
            newTreasure.setType(2);
            return;
        }else if (type == 3){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.hearticon));
            newTreasure.setValue(0);
            newTreasure.setType(3);
            return;
        }else if (type == 4){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.barreltransparent));
            newTreasure.setValue(0);
            newTreasure.setType(4);
            return;
        }


        if (randomInt > 100 - heartZoneSize){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.hearticon));
            newTreasure.setValue(0);
            newTreasure.setType(3);
        }else if (randomInt > 100 - heartZoneSize - diamondZoneSize){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.diamondtransparent));
            newTreasure.setValue(5);
            newTreasure.setType(2);
        }else if (randomInt > 100 - heartZoneSize - diamondZoneSize - barrelZoneSize){
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.barreltransparent));
            newTreasure.setValue(0);
            newTreasure.setType(4);
        }else{
            newTreasure.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.coin));
            newTreasure.setValue(1);
            newTreasure.setType(1);
        }
    }

    private void addTreasureToScreen(Treasure newTreasure, float x, float y){

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(120, 120);


        if (newTreasure.type == 1)
            lp = new ConstraintLayout.LayoutParams(100, 100);

        if (newTreasure.type == 2)
            lp = new ConstraintLayout.LayoutParams(160, 160);

        if (newTreasure.type == 4)
            lp = new ConstraintLayout.LayoutParams(200, 200);

        ConstraintLayout view = (ConstraintLayout) mainActivity.findViewById(R.id.topLayout);
        view.addView(newTreasure, -1, lp);

        newTreasure.setX(x);
        newTreasure.setY(y);
    }

    private void setInitialMotion(Treasure newTreasure, float velocityY, float velocityX){
        if (velocityX != 0)
            fling.simpleXFling(newTreasure, velocityX);

        if (velocityY != 0)
            fling.simpleYFling(newTreasure, velocityY);
    }

    private void flingHeart(Treasure newTreasure, float velocityY){
        fling.flingHeart(newTreasure, velocityY/3);
    }
}
