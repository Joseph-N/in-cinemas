package com.example.jsonparsing;

import com.example.jsonparsing.R;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
public class MainActivity extends ListActivity {
	
	private ProgressDialog pDialog;
	
	// url to get contacts as json	
	private static String url = "http://moviebuddy.info:4000/";    
    
    // json node names for movie buddy
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_POSTER = "avator_url";
    
    JSONArray movies = null;
    
    // Hashmap for list view
    ArrayList<HashMap<String, String>> movieList;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        movieList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();
        
		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
						// getting values from selected ListItem
						String title = ((TextView) view.findViewById(R.id.title))
								.getText().toString();
						String description = ((TextView) view.findViewById(R.id.description))
								.getText().toString();
		
						// Starting single contact activity
						Intent in = new Intent(getApplicationContext(),
								SingleMovieActivity.class);
						in.putExtra(TAG_TITLE, title);
						in.putExtra(TAG_DESCRIPTION, description);
						startActivity(in);

			}
		});
        
        // call async task to get json
        new GetMovies().execute();
       
    }
    
    // async task to get json by making http call
    private class GetMovies extends AsyncTask<Void, Void, Void>{
    	
    	@Override
    	protected void onPreExecute(){
    		super.onPreExecute();
    		//showing progress dialog
    		pDialog = new ProgressDialog(MainActivity.this);
    		pDialog.setMessage("Please Wait....");
    		pDialog.setCancelable(false);
    		pDialog.show();
    	}
    	
    	@Override
    	protected Void doInBackground(Void...arg0){
    		//create service handler class instance
    		ServiceHandler sh = new ServiceHandler();    		
    		
    		//make a request to url and getting response
    		String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
    		
    		Log.d("Response: ", "> " + jsonStr);
    		
    		if(jsonStr != null){
    			try{
    				JSONObject jsonObj = new JSONObject(jsonStr);
    				
    				
    	    		//getting JSON Array node
    	    		movies = jsonObj.getJSONArray(TAG_MOVIES);
    	    		
    	    		//looping through all contacts
    	    		for(int i = 0; i < movies.length(); i++){
    	    			JSONObject c = movies.getJSONObject(i);
    	    			  	    			
    	    			String id = c.getString(TAG_ID);
    	    			String title = c.getString(TAG_TITLE);
                        String description = c.getString(TAG_DESCRIPTION);

                        
//                        String address = c.getString(TAG_ADDRESS);
//                        String gender = c.getString(TAG_GENDER);
                        
                        //phone node is JSON OBJECT
//                        JSONObject phone = c.getJSONObject(TAG_PHONE);
//                        String mobile = phone.getString(TAG_PHONE_MOBILE);
//                        String home = phone.getString(TAG_PHONE_HOME);
//                        String office = phone.getString(TAG_PHONE_OFFICE);
                        
                        // tmp hashmap for single contact
                        HashMap<String, String> movie = new HashMap<String, String>();
                        
                        // adding each child not to HashMap key => value
                        movie.put(TAG_ID, id);
                        movie.put(TAG_TITLE, title);
                        movie.put(TAG_DESCRIPTION, description);
//                        contact.put(TAG_PHONE_MOBILE, mobile);
                        
                        //adding contact to contact list
                        movieList.add(movie);           
    	    			
    	    		}
    			} catch(JSONException e) {
    				e.printStackTrace();    				
    			} 
    		}else {
    				Log.e("ServiceHandler", "Couldn't get any data from the url");  				
    		}
    		
    		return null;
    		
    		}
    	
    	@Override
    	protected void onPostExecute(Void result){
    		super.onPostExecute(result);
    		
    		//Dismiss the progress dialog
    		if(pDialog.isShowing())
    			pDialog.dismiss();
    		
    		// update parse json into list view
    		ListAdapter adapter = new SimpleAdapter(
    				MainActivity.this, movieList,
    				R.layout.list_item, new String[] { TAG_TITLE, TAG_DESCRIPTION },
    				new int[] { R.id.title, R.id.description });
    		
    		setListAdapter(adapter);
    	}
    		
    		
  	}
}
