package com.example.android.matchtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.R.attr.name;
import static com.example.android.matchtracker.R.id.TeamAName;
import static com.example.android.matchtracker.R.id.TeamBName;
import static com.example.android.matchtracker.R.id.reset;
import static com.example.android.matchtracker.R.id.timer;
import static java.lang.System.out;

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
            long min = (timeMili/1000)/60;
            long seconds = (timeMili/1000);
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
            long min = (timeMili/1000)/60;
            long seconds = (timeMili/1000);
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
            long min = (timeMili/1000)/60;
            long seconds = (timeMili/1000);
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
            long min = (timeMili/1000)/60;
            long seconds = (timeMili/1000);
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
        timer.setBase(SystemClock.elapsedRealtime());
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
        Toast.makeText(this, "access facebook hopefully", Toast.LENGTH_SHORT).show();
    }

    // Twitter posting
    public void twitterAccess(View view){
        Toast.makeText(this, "access twitter hopefully", Toast.LENGTH_SHORT).show();

        // Take screenshot
        Bitmap bitty = screenshot();
        saveBitmap(bitty);

        // Produce image uri
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/screenshot.png");
        Uri imageUri = Uri.fromFile(imagePath);

        TweetComposer.Builder builder = new TweetComposer.Builder(this).image(imageUri);
        builder.show();
    }

    // Take screenshot of current activity
    public Bitmap screenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    // save screenshot to pictures on phone
    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

}
