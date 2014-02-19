package com.example.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.jsonparsing.R;

public class SingleMovieActivity extends Activity{
	// JSON node keys
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String title = in.getStringExtra(TAG_TITLE);
        String description = in.getStringExtra(TAG_DESCRIPTION);
        
        // Displaying all values on the screen
        TextView lbltTitle = (TextView) findViewById(R.id.title_label);
        TextView lblDescription = (TextView) findViewById(R.id.description_label);
        
        lbltTitle.setText(title);
        lblDescription.setText(description);
    }

}
