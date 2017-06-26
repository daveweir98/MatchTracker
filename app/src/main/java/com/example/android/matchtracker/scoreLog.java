package com.example.android.matchtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by david on 31/05/17.
 */

public class scoreLog extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorelog);

        Intent intent = getIntent();
        ArrayList<String> scores = intent.getStringArrayListExtra("theScores");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, scores);

        ListView listview = (ListView) findViewById(R.id.scoreLogList);

        listview.setAdapter(itemsAdapter);
    }
}
