package com.twomack.android.gms.samples.vision.face.facetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twomack.android.gms.samples.vision.face.facetracker.R;

import java.util.ArrayList;

public class LevelEndActivity extends Activity {

    int treasure;
    int heartsLeft;
    ArrayList<String> items;
    int nextLevel;
    int maxLevel = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_end);

        treasure = getIntent().getIntExtra("treasure", 0);
        heartsLeft = getIntent().getIntExtra("heartsLeft", 5);
        nextLevel = getIntent().getIntExtra("level", 1);
        nextLevel = nextLevel + 1;

        if (nextLevel > maxLevel)
            nextLevel = maxLevel;

        items = new ArrayList<>();

        ArrayList<String> returnedItems = getIntent().getStringArrayListExtra("items");
        if (returnedItems != null)
            items = returnedItems;

        if (items != null){
            if (items.contains("sword"))
                findViewById(R.id.sword).setVisibility(View.INVISIBLE);
            if (items.contains("shield"))
                findViewById(R.id.shield).setVisibility(View.INVISIBLE);
            if (items.contains("heart"))
                findViewById(R.id.heart).setVisibility(View.INVISIBLE);
            if (items.contains("diamond"))
                findViewById(R.id.diamond).setVisibility(View.INVISIBLE);
        }

        TextView treasureCollected = findViewById(R.id.treasureCollected);
        treasureCollected.setText(Integer.toString(treasure));

        TextView heartsView = findViewById(R.id.heartsLeft);
        heartsView.setText(Integer.toString(heartsLeft));

        ImageView sword = findViewById(R.id.sword);
        sword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDialog(150, "sword");
            }
        });

        ImageView shield = findViewById(R.id.shield);
        shield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDialog(150, "shield");
            }
        });

        ImageView diamond = findViewById(R.id.diamond);
        diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDialog(70, "diamond");
            }
        });

        ImageView heart = findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDialog(50, "heart");
            }
        });
    }

    public void itemDialog(final int price, final String itemName){

        String description = "";
        switch (itemName) {
            case "sword":
                description = this.getResources().getString(R.string.sword_description);
                break;
            case "shield": description = this.getResources().getString(R.string.shield_description);
                break;
            case "diamond": description =  this.getResources().getString(R.string.diamond_description);
                break;
            case "heart": description = this.getResources().getString(R.string.heart_description);
                break;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(description);
        builder.setPositiveButton(R.string.buy_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                buyItem(price, itemName);
            }
        });

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void buyItem(int price, final String itemName){
        if (treasure > price){
            treasure = treasure - price;
            addItem(itemName);
        }else{
            Toast.makeText(this, "Sorry, you don't have enough treasure!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(String itemName){
        switch (itemName) {
            case "sword": items.add("sword");
            findViewById(R.id.sword).setVisibility(View.INVISIBLE);
                break;
            case "shield": items.add("shield");
            findViewById(R.id.shield).setVisibility(View.INVISIBLE);
                break;
            case "diamond": items.add("diamond");
                findViewById(R.id.diamond).setVisibility(View.INVISIBLE);
                break;
            case "heart": items.add("heart");
                findViewById(R.id.heart).setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void nextLevel(View v){
        Intent intent = new Intent(this, FaceTrackerActivity.class);
        intent.putExtra("treasure", treasure);
        intent.putExtra("heartsLeft", heartsLeft);
        intent.putStringArrayListExtra("items", items);
        intent.putExtra("level", nextLevel);

        startActivity(intent);
    }
}
