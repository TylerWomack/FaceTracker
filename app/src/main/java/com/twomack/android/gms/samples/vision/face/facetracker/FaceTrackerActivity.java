/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twomack.android.gms.samples.vision.face.facetracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.twomack.android.gms.samples.vision.face.facetracker.ui.camera.CameraSourcePreview;
import com.twomack.android.gms.samples.vision.face.facetracker.ui.camera.GraphicOverlay;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Activity for the face tracker app.  This app detects faces with the rear facing camera, and draws
 * overlay graphics to indicate the position, size, and ID of each face.
 */
public final class FaceTrackerActivity extends AppCompatActivity {
    private static final String TAG = "FaceTracker";

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    float leftEyeOpen;
    float rightEyeOpen;
    float smilingProbability;
    public ArrayList<Treasure> treasureList;
    Flinger fling;
    Generators generator;
    float screenWidth;
    float screenHeight;

    MediaPlayer mPlayer = null;
    MediaPlayer mPlayer2 = null;
    MediaPlayer mPlayer3;
    MediaPlayer mPlayer4;
    MediaPlayer mPlayer5;
    MediaPlayer mPlayer6;
    MediaPlayer mPlayer7;
    MediaPlayer mPlayer8;
    MediaPlayer mPlayer9;
    MediaPlayer mPlayer10;

    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        treasureList = new ArrayList<>();



        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
            startGame();
        } else {
            requestCameraPermission();
        }
    }

    public void startGame(){

        int level = getIntent().getIntExtra("level", 1);

        setBackground(level);
        playMusic();

        if (level == 1)
            giveInstructions(R.string.level_one_instructions);
        if (level == 2)
            giveInstructions(R.string.level_two_instructions);
        if (level == 3){
            giveInstructions(R.string.how_to_win);
            givePlainInstructions(R.string.level_three_instructions);
        }
        if (level == 4)
            giveInstructions(R.string.how_to_win);
    }

    public void playMusic(){

        stopMusic();

        Random random = new Random();
        int randomInt = random.nextInt(10);

                if (randomInt == 0) {
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.love);
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playMusic();
                            }
                        });
                        mPlayer.start();
                }
                if (randomInt == 1) {
                    mPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.battle);
                    mPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer2.start();
                }
                if (randomInt == 2) {
                    //mPlayer3.reset();
                    mPlayer3 = MediaPlayer.create(getApplicationContext(), R.raw.boss);
                    mPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer3.start();
                }
                if (randomInt == 3) {
                    //mPlayer4.reset();
                    mPlayer4 = MediaPlayer.create(getApplicationContext(), R.raw.defeat);
                    mPlayer4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer4.start();
                }
                if (randomInt == 4) {
                    //mPlayer5.reset();
                    mPlayer5 = MediaPlayer.create(getApplicationContext(), R.raw.title);
                    mPlayer5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer5.start();
                }
                if (randomInt == 5) {
                    //mPlayer6.reset();
                    mPlayer6 = MediaPlayer.create(getApplicationContext(), R.raw.map_theme);
                    mPlayer6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer6.start();
                }
                if (randomInt == 6) {
                    //mPlayer7.reset();
                    mPlayer7 = MediaPlayer.create(getApplicationContext(), R.raw.rolling);
                    mPlayer7.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer7.start();
                }
                if (randomInt == 7) {
                    //mPlayer8.reset();
                    mPlayer8 = MediaPlayer.create(getApplicationContext(), R.raw.trace_route);
                    mPlayer8.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer8.start();
                }
                if (randomInt == 8) {
                    //mPlayer9.reset();
                    mPlayer9 = MediaPlayer.create(getApplicationContext(), R.raw.tricks);
                    mPlayer9.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer9.start();
                }
                if (randomInt == 9) {
                    mPlayer10 = MediaPlayer.create(getApplicationContext(), R.raw.victory);
                    //mPlayer10.reset();
                    mPlayer10.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusic();
                        }
                    });
                    mPlayer10.start();
                }
    }

    public void stopMusic(){

        if (mPlayer != null){
            mPlayer.stop();
        }

        if (mPlayer2 != null){
            mPlayer2.stop();
        }
        if (mPlayer3 != null){
            mPlayer3.stop();
        }

        if (mPlayer4 != null){
            mPlayer4.stop();
        }
        if (mPlayer5 != null){
            mPlayer5.stop();
        }

        if (mPlayer6 != null){
            mPlayer6.stop();
        }

        if (mPlayer7 != null){
            mPlayer7.stop();
        }

        if (mPlayer8 != null){
            mPlayer8.stop();
        }

        if (mPlayer9 != null){
            mPlayer9.stop();
        }

        if (mPlayer10 != null){
            mPlayer10.stop();
        }
    }

    public void beginAction(){

        int level = getIntent().getIntExtra("level", 1);
        int hearts = getIntent().getIntExtra("heartsLeft", 5);
        int treasure = getIntent().getIntExtra("treasure", 0);
        ArrayList<String> items = getIntent().getStringArrayListExtra("items");

        findBorder();
        fling = new Flinger();
        generator = new Generators(fling, this, level, hearts, treasure, items);
        configureObservables();
        generator.activateGenerator();
        generator.startTreasureGenerator();
        generator.startCharacterGenerator();
        generator.startLevelTimer();
    }

    public void setBackground(int level){
        if (level == 1){
            ImageView background = findViewById(R.id.background);
            background.setImageDrawable(getResources().getDrawable(R.drawable.jungle));
        }

        if (level == 2){
            ImageView background = findViewById(R.id.background);
            background.setImageDrawable(getResources().getDrawable(R.drawable.pyramids));
        }
        if (level == 3){
            ImageView background = findViewById(R.id.background);
            background.setImageDrawable(getResources().getDrawable(R.drawable.crypt));
        }
        if (level == 4){
            ImageView background = findViewById(R.id.background);
            background.setImageDrawable(getResources().getDrawable(R.drawable.village));
        }
    }

    public void giveInstructions(final int messageId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //beginAction();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                beginAction();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void givePlainInstructions(final int messageId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId);
        builder.show();
    }

    public ConstraintLayout getMainLayout(){
        return findViewById(R.id.topLayout);
    }

    public void configureObservables(){
        generator.getHeartsData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                setHeartText(integer);
            }
        });

        generator.getTreasureData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                setTreasureScore(integer);
            }
        });
    }

    public int getSecondsRemaining(){
        TextView secondsText = findViewById(R.id.secondsRemaining);
        return Integer.valueOf((String) secondsText.getText());
    }

    public void setSecondsRemaining(int secondsRemaining){
        TextView secondsText = findViewById(R.id.secondsRemaining);
        secondsText.setText(String.valueOf(secondsRemaining));
    }

    public void setTreasureScore(int treasureScore){
        TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(treasureScore));
    }

    public float getEyesScore(){
        return leftEyeOpen + rightEyeOpen;
    }

    public float getSmilingProbability() { return smilingProbability; }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
                startGame();
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    public ArrayList<Treasure> getTreasureList() {
        return treasureList;
    }

    public ImageView getBackground(){
        return findViewById(R.id.background);
    }

    public int getHeartsLeft(){
        TextView heartTextView = findViewById(R.id.heartsLeft);
        String heartText = (String) heartTextView.getText();
        return Integer.valueOf(heartText);
    }

    public void setHeartText(int hearts){
        TextView heartsLeft = (TextView) findViewById(R.id.heartsLeft);
        heartsLeft.setText(Integer.toString(hearts));
    }

    public void findBorder(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        stopMusic();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            rightEyeOpen = face.getIsRightEyeOpenProbability();
            leftEyeOpen = face.getIsLeftEyeOpenProbability();
            smilingProbability = face.getIsSmilingProbability();
            //mFaceGraphic.updateFace(face);
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }


}
