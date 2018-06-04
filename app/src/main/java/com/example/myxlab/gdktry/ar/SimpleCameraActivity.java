/*
 * Copyright (C) 2014 BeyondAR
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
package com.example.myxlab.gdktry.ar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.example.myxlab.gdktry.Google.CardActivitySingle;
import com.example.myxlab.gdktry.Google.CardActivityTest;
import com.example.myxlab.gdktry.Google.DBHandler;
import com.example.myxlab.gdktry.Google.GoogleData;
import com.example.myxlab.gdktry.Google.POI;
import com.example.myxlab.gdktry.Google.TrackGPS;
import com.example.myxlab.gdktry.R;
import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleCameraActivity extends FragmentActivity implements OnTouchBeyondarViewListener,
        OnClickBeyondarObjectListener {

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private ArrayList<POI> poiArrayList;

    private GestureDetector mGestureDetector;
    private int push = 15;
    TextView tvPush;
    ImageView pointer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.simple_camera);

        poiArrayList = new ArrayList<POI>();
        mGestureDetector = createGestureDetector(this);

        tvPush = (TextView) findViewById(R.id.pushValue);
        tvPush.setText("" + push);
        pointer = (ImageView) findViewById(R.id.pointer);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        mBeyondarFragment.setMaxDistanceToRender(1000);
        mBeyondarFragment.setDistanceFactor(5000);
        mBeyondarFragment.setPullCloserDistance(10);
        mBeyondarFragment.setPushAwayDistance(push);

        DBHandler dbHandler = new DBHandler(this, null);
        List<POI> poIs = dbHandler.getPOIs();
        if (!poIs.isEmpty()){
           dbHandler.delAllData();
        }
        String url = getResources().getString(R.string.api_url);
        Log.e("This is the final URL", url);
        getGoogleData(url);

    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    /*int mWidth= getResources().getDisplayMetrics().widthPixels;
                    int mHeight= getResources().getDisplayMetrics().heightPixels;
					Log.e("onClick"," W-"+mWidth+" H-"+mHeight);
					Log.e("Center"," W/2-"+mWidth/2+" H/2-"+mHeight/2);*/
                    Point point = getCenterPointOfView(pointer);
                    Log.e("point", "view point x,y (" + point.x + ", " + point.y + ")");
                    // set listener for the geoObjects
                    mBeyondarFragment.setOnTouchBeyondarViewListener(SimpleCameraActivity.this);
                    mBeyondarFragment.setOnClickBeyondarObjectListener(SimpleCameraActivity.this);
                    ontappp();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    Log.e("onClick", "TWO_TAP");
                    openOptionsMenu();
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe

                    //Toast.makeText(getApplicationContext(), "SWIPE_RIGHT", Toast.LENGTH_SHORT).show();
                    push += 5;
                    mBeyondarFragment.setPushAwayDistance(push);
                    Log.e("onClick", "SWIPE_RIGHT " + push);
                    tvPush.setText("" + push);
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    //Toast.makeText(getApplicationContext(), "SWIPE_LEFT", Toast.LENGTH_SHORT).show();
                    push -= 5;
                    mBeyondarFragment.setPushAwayDistance(push);
                    Log.e("onClick", "SWIPE_LEFT " + push);
                    tvPush.setText("" + push);
                    return true;
                }
                return false;
            }
        });

        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
                Log.e("onClick", "onFingerCountChanged");
                Log.e("onFingerCountChanged", "Pre:" + previousCount + " Cur:" + currentCount);
            }
        });

        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling

                Log.e("onScroll", "displacement:" + displacement + " delta:" + delta + " velocity:" + velocity);
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
                Intent i = new Intent(SimpleCameraActivity.this, CardActivityTest.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cards", poiArrayList);
                i.putExtras(bundle);
                startActivity(i);
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

    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    private Point getCenterPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }

    @Override
    public void onTouchBeyondarView(MotionEvent event, BeyondarGLSurfaceView beyondarView) {

        Point point = getCenterPointOfView(pointer);
        Log.e("pointerr", "view point x,y (" + point.x + ", " + point.y + ")");

        float x = point.x;
        float y = point.y;

        ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

        // This method call is better to don't do it in the UI thread!
        beyondarView.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);

        String textEvent = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                textEvent = "Event type ACTION_DOWN: ";
                break;
            case MotionEvent.ACTION_UP:
                textEvent = "Event type ACTION_UP: ";
                break;
            case MotionEvent.ACTION_MOVE:
                textEvent = "Event type ACTION_MOVE: ";
                break;
            default:
                break;
        }

        Iterator<BeyondarObject> iterator = geoObjects.iterator();
        while (iterator.hasNext()) {
            BeyondarObject geoObject = iterator.next();
            textEvent = textEvent + " " + geoObject.getName();

        }
        tvPush.setText("Event: " + textEvent);
    }

    public void ontappp() {
        BeyondarGLSurfaceView beyondarGLSurfaceView = mBeyondarFragment.getGLSurfaceView();
        Point point = getCenterPointOfView(pointer);
        Log.e("pointerr", "view point x,y (" + point.x + ", " + point.y + ")");

        float x = point.x;
        float y = point.y;

        ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();


        // This method call is better to don't do it in the UI thread!
        beyondarGLSurfaceView.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);

        String textEvent = "";


        Iterator<BeyondarObject> iterator = geoObjects.iterator();
        while (iterator.hasNext()) {
            BeyondarObject geoObject = iterator.next();
            int dis = (int) geoObject.getDistanceFromUser();
            textEvent = textEvent + " " + geoObject.getName() + " - " +
                   dis +"m Away";


        }
        tvPush.setText("Event: " + textEvent);
        onClickBeyondarObject(geoObjects);
    }


    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            String geoName = beyondarObjects.get(0).getName();
            DBHandler dbHandler = new DBHandler(getApplicationContext(), null);
            POI geoObj = dbHandler.getPOI(geoName);
            Log.e(geoObj.getPlace_name(), geoObj.getPlace_vicinity());

            Intent i = new Intent(SimpleCameraActivity.this, CardActivitySingle.class);
            i.putExtra("geo", geoObj); // sending our object
            startActivity(i);
        }
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
                        DBHandler dbHandler = new DBHandler(getApplicationContext(), null);

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String place = data.getString("name");

                                String place_id = data.getString("place_id");
                                String place_vicinity = data.getString("vicinity");

                                double place_rating = 0;
                                if (data.has("rating")) {
                                    place_rating = data.getDouble("rating");
                                }
                                boolean place_open = false;
                                if (data.has("opening_hours")) {
                                    place_open = data.getJSONObject("opening_hours").getBoolean("open_now");
                                }

                                double lat = data.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                double lng = data.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                                poi = new POI(place, place_id, place_vicinity, place_rating,lat,lng, place_open, Card.ImageLayout.FULL, new int[]{});
                                poiArrayList.add(poi);
                                Log.e("place", place.toString());
                                dbHandler.addPOI(poi);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response!=null){
                            // We create the world and fill it ...
                            mWorld = CustomWorldHelper.generateObjects(SimpleCameraActivity.this);
                            // ... and send it to the fragment
                            mBeyondarFragment.setWorld(mWorld);

                            // We also can see the Frames per seconds
                            mBeyondarFragment.showFPS(true);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.e("Error", error.getMessage());
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPush.setText("");
    }
}
