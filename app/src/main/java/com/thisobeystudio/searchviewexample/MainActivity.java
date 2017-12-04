package com.thisobeystudio.searchviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thisobeystudio.searchviewexample.db.DemoContract;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.app_name));

        DemoContract.setDBData(this);

        findViewById(R.id.simple_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        SimpleSearchViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.custom_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        CustomSearchViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.db_custom_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        DBCustomSearchViewActivity.class);
                startActivity(intent);
            }
        });

    }

}
