package com.example.myxlab.gdktry.Google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myxlab.gdktry.R;
import com.example.myxlab.gdktry.ar.SimpleCameraActivity;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleData extends Activity {
    private ArrayList<POI> poiArrayList;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_data);

        poiArrayList = new ArrayList<POI>();
        mGestureDetector = createGestureDetector(this);

        Button bt = (Button) findViewById(R.id.btGetData);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= getResources().getString(R.string.api_url);
                Log.e("This is the final URL", url);
                getGoogleData(url);
            }
        });

        bt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!poiArrayList.isEmpty()){
                    for (int i = 0; i<poiArrayList.size(); i++){
                        Log.e("Data", poiArrayList.get(i).getPlace_name()+" "+
                                poiArrayList.get(i).getPlace_id()+" "+
                                poiArrayList.get(i).getPlace_vicinity()+" "+
                                poiArrayList.get(i).getPlace_rating()+" "+
                                poiArrayList.get(i).isPlace_open());
                    }
                }

                return true;
            }
        });
    }

    private void getGoogleData(String url) {
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response

                        POI poi;
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i =0; i< jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String place = data.getString("name");
                                Log.e("place",place.toString());
                                String place_id = data.getString("place_id");
                                String place_vicinity = data.getString("vicinity");
                                double place_rating = 0;
                                if (data.has("rating")){
                                    place_rating = data.getDouble("rating");
                                }
                                boolean place_open = false;
                                if (data.has("opening_hours")) {
                                    place_open = data.getJSONObject("opening_hours").getBoolean("open_now");
                                }

                                //poi = new POI(place,place_id,place_vicinity,place_rating,place_open, Card.ImageLayout.FULL, new int[] {});
                                //poiArrayList.add(poi);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.e("Error",error.getMessage());
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }


    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    Log.e("onClick","TAP");
                    String url= getResources().getString(R.string.api_url);
                    Log.e("This is the final URL", url);
                    getGoogleData(url);
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    Log.e("onClick","TWO_TAP");
                    if (!poiArrayList.isEmpty()){
                        for (int i = 0; i<poiArrayList.size(); i++){
                            Log.e("Data", poiArrayList.get(i).getPlace_name()+" "+
                                    poiArrayList.get(i).getPlace_id()+" "+
                                    poiArrayList.get(i).getPlace_vicinity()+" "+
                                    poiArrayList.get(i).getPlace_rating()+" "+
                                    poiArrayList.get(i).isPlace_open());
                        }
                    }
                    //openOptionsMenu();
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe

                    //Toast.makeText(getApplicationContext(), "SWIPE_RIGHT", Toast.LENGTH_SHORT).show();
                    Log.e("onClick","SWIPE_RIGHT");
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Intent i = new Intent(GoogleData.this, CardActivityTest.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cards",poiArrayList);
                    i.putExtras(bundle);
                    startActivity(i);
                    Log.e("onClick","SWIPE_LEFT");
                    return true;
                }
                return false;
            }
        });

        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
                Log.e("onClick","onFingerCountChanged");
                Log.e("onFingerCountChanged","Pre:"+previousCount+ " Cur:"+currentCount);
            }
        });

        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling

                Log.e("onScroll","displacement:"+displacement+ " delta:"+delta +" velocity:"+velocity);
                return true;
            }
        });
        return gestureDetector;
    }

    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                // Stop the service which will unpublish the live card.
                //stopService(new Intent(this, LiveCardService.class));
                return true;
            case R.id.action_start:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // Nothing else to do, finish the Activity.
        menu.close();
    }

}
