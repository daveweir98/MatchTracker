package com.example.android.matchtracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.example.android.matchtracker.R.id.TeamBName;

public class InProgressActivity extends AppCompatActivity {
    // Name and score variables
    String nameA;
    int pointsA = 0;
    int goalsA = 0;
    String nameB;
    int pointsB = 0;
    int goalsB = 0;

    // For score log
    ArrayList<String> scores = new ArrayList<>();

    // To check whether or not to subtract
    boolean minusToggle = false;

    // Timer variables
    Chronometer timer;
    long lastStop;
    boolean useStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprogress);

        timer = (Chronometer) findViewById(R.id.timer);

        TextView teamA = (TextView) findViewById(R.id.TeamAName);
        String AName = getIntent().getStringExtra("teamAName");
        nameA = AName.toUpperCase();
        teamA.setText(AName);

        TextView teamB = (TextView) findViewById(TeamBName);
        String BName = getIntent().getStringExtra("teamBName");
        nameB = BName.toUpperCase();
        teamB.setText(BName);

        Twitter.initialize(this);


    }
    // Team A score updated on screen
    public void displayForTeamA() {
        TextView totalView = (TextView) findViewById(R.id.team_a_total);
        totalView.setText(String.valueOf((goalsA * 3) + pointsA));
        TextView goalsView = (TextView) findViewById(R.id.team_a_goals);
        goalsView.setText(String.valueOf(goalsA));
        TextView pointsView = (TextView) findViewById(R.id.team_a_points);
        pointsView.setText(String.valueOf(pointsA));
    }
    // Add/subtract goal to TeamA
    public void goalA(View view){
        long timeMili = (SystemClock.elapsedRealtime() - timer.getBase());
        if(minusToggle == true){
            goalsA -= 1;
            displayForTeamA();
        }
        else {
            goalsA += 1;
            displayForTeamA();
            // Calculate time scored, and add to score log
            long min = (timeMili / (1000*60) % 60);
            long seconds = ((timeMili/1000) % 60);
            scores.add(nameA + ": Goal at " + (String.format("%02d",min) + ":" + String.format("%02d",seconds)));
        }
    }
    // Add/subtract point to TeamA
    public void pointA(View view){
        long timeMili = (SystemClock.elapsedRealtime() - timer.getBase());
        if(minusToggle == true){
            pointsA -= 1;
            displayForTeamA();
        }
        else {
            pointsA += 1;
            displayForTeamA();
            // Calculate time scored, and add to score log
            long min = (timeMili / (1000*60) % 60);
            long seconds = ((timeMili/1000) % 60);
            scores.add(nameA + ": Point at " + (String.format("%02d",min) + ":" + String.format("%02d",seconds)));
        }
    }
    // Team B score updated on screen
    public void displayForTeamB(){
        TextView totalView = (TextView) findViewById(R.id.team_b_total);
        totalView.setText(String.valueOf((goalsB * 3) + pointsB));
        TextView goalsView = (TextView) findViewById(R.id.team_b_goals);
        goalsView.setText(String.valueOf(goalsB));
        TextView pointsView = (TextView) findViewById(R.id.team_b_points);
        pointsView.setText(String.valueOf(pointsB));
    }
    // Add/subtract goal to TeamB
    public void goalB(View view){
        long timeMili = (SystemClock.elapsedRealtime() - timer.getBase());
        if(minusToggle == true){
            goalsB -= 1;
            displayForTeamB();
        }
        else {
            goalsB += 1;
            displayForTeamB();
            // Calculate time scored, and add to score log
            long min = (timeMili / (1000*60) % 60);
            long seconds = ((timeMili/1000) % 60);
            scores.add(nameB + ": Goal at " + (String.format("%02d",min) + ":" + String.format("%02d",seconds)));
        }
    }
    // Add/subtract point to TeamB
    public void pointB(View view){
        long timeMili = (SystemClock.elapsedRealtime() - timer.getBase());
        if(minusToggle == true){
            pointsB -= 1;
            displayForTeamB();
        }
        else {
            pointsB += 1;
            displayForTeamB();
            // Calculate time scored, and add to score log
            long min = (timeMili / (1000*60) % 60);
            long seconds = ((timeMili/1000) % 60);
            scores.add(nameB + ": Point at " + (String.format("%02d",min) + ":" + String.format("%02d",seconds)));
        }
    }
    // Reset scores and score log
    public void reset(View view){
        goalsA = 0;
        pointsA = 0;
        displayForTeamA();

        goalsB = 0;
        pointsB = 0;
        displayForTeamB();

        scores = new ArrayList<>();
        
    }
    // Turn buttons into subtraction buttons/reset buttons
    public void minus(View view){
        minusToggle = minusToggle ^ true;
        if(minusToggle == true) {
            Button goalA = (Button) findViewById(R.id.goalButtonA);
            goalA.setText("- GOAL");
            Button pointA = (Button) findViewById(R.id.pointButtonA);
            pointA.setText("- POINT");
            Button goalB = (Button) findViewById(R.id.goalButtonB);
            goalB.setText("- GOAL");
            Button pointB = (Button) findViewById(R.id.pointButtonB);
            pointB.setText("- POINT");
        }
        else{
            Button goalA = (Button) findViewById(R.id.goalButtonA);
            goalA.setText("GOAL");
            Button pointA = (Button) findViewById(R.id.pointButtonA);
            pointA.setText("POINT");
            Button goalB = (Button) findViewById(R.id.goalButtonB);
            goalB.setText("GOAL");
            Button pointB = (Button) findViewById(R.id.pointButtonB);
            pointB.setText("POINT");
        }
    }
    // Timer start/resume if already started
    public void timerStart(View view){
        if(useStart) {
            useStart = false;
            if (lastStop == 0) {
                timer.setBase(SystemClock.elapsedRealtime());
            } else {
                long gapAtPause = (SystemClock.elapsedRealtime() - lastStop);
                timer.setBase(timer.getBase() + gapAtPause);
            }
            timer.start();
        }
    }
    // Pause timer, lastStop used for resume
    public void timerPause(View view){
        timer.stop();
        lastStop = SystemClock.elapsedRealtime();
        useStart = true;
    }

    // Reset timer
    public void timerStop(View view){
        timer.stop();
        lastStop = 0;
        useStart = true;
    }

    // Move to score log activity
    public void accessScores(View view){
        Intent scoreIntent = new Intent(InProgressActivity.this, scoreLog.class);
        scoreIntent.putStringArrayListExtra("theScores", scores);
        startActivity(scoreIntent);
    }

    // Facebook posting
    public void facebookAccess(View view){
        //Toast.makeText(this, "access facebook hopefully", Toast.LENGTH_SHORT).show();

        ShareDialog sd = new ShareDialog(this);

        // Take screenshot
        Bitmap bitty = screenshot();

        // Share photo to facebook
        SharePhoto photo = new SharePhoto.Builder().setBitmap(bitty).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        sd.show(this, content);


    }

    // Twitter posting
    public void twitterAccess(View view){
        //Toast.makeText(this, "access twitter hopefully", Toast.LENGTH_SHORT).show();

        // Take screenshot
        Bitmap bitty = screenshot();
        saveBitmap(bitty);

        // Produce image uri
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"screenshot");
        Uri imageUri = Uri.fromFile(imagePath);

        TweetComposer.Builder builder = new TweetComposer.Builder(this).image(imageUri);
        builder.show();
    }

    // Take screenshot of current activity
    public Bitmap screenshot() {

        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    // save screenshot to pictures on phone
    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"screenshot");
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Throwable e) {
            //Log.e("Not Good", e.getMessage(), e);
        }
    }
}
