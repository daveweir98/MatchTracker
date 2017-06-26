package com.example.android.matchtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by david on 30/05/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check write permissions for external storage
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }

    public void startGame(View view){
        EditText teamA = (EditText) findViewById(R.id.TeamAName);
        String teamAName = teamA.getText().toString();

        EditText teamB = (EditText) findViewById(R.id.TeamBName);
        String teamBName = teamB.getText().toString();

        if (teamAName.matches("") || teamBName.matches("")){
            Toast.makeText(this, "Enter Team Names", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent startIntent = new Intent(MainActivity.this, InProgressActivity.class);
        startIntent.putExtra("teamAName", teamAName);
        startIntent.putExtra("teamBName", teamBName);
        startActivity(startIntent);
    }

}
