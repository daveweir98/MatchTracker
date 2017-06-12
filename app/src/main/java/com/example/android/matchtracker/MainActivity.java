package com.example.android.matchtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.id.edit;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by david on 30/05/17.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
