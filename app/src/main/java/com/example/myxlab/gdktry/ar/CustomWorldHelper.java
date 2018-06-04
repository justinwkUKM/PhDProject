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

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.example.myxlab.gdktry.Google.DBHandler;
import com.example.myxlab.gdktry.Google.POI;
import com.example.myxlab.gdktry.Google.TrackGPS;
import com.example.myxlab.gdktry.R;

import java.util.List;

@SuppressLint("SdCardPath")
public class CustomWorldHelper {
    public static final int LIST_TYPE_EXAMPLE_1 = 1;

    public static World sharedWorld;

    ///////////
    //private TrackGPS gps;
    double longitude;
    double latitude;


    public static World generateObjects(Context context) {
        if (sharedWorld != null) {
            return sharedWorld;
        }
        sharedWorld = new World(context);

        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        sharedWorld.setDefaultImage(R.drawable.beyondar_default_unknow_icon);

        //original code4
        // User position (you can change it using the GPS listeners form Android
        // API)

        TrackGPS trackGPS = new TrackGPS(context);
        // check if GPS enabled
        if(trackGPS.canGetLocation()){

            double latitude = trackGPS.getLatitude();
            double longitude = trackGPS.getLongitude();

            // \n is for new line
            Log.e("Location is","Lat: " + latitude + "  Long: "+longitude);

            sharedWorld.setGeoPosition(latitude, longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            trackGPS.showSettingsAlert();
            sharedWorld.setGeoPosition(2.918542d, 101.772076d);
        }

        DBHandler dbHandler = new DBHandler(context, null);
        List<POI> poIs = dbHandler.getPOIs();

        for (int i = 0; i < poIs.size(); i++){
            Log.e(poIs.get(i).getPlace_name(),"E:"+poIs.get(i).getPlace_vicinity()+" P:"+poIs.get(i).getLat());
        }

        // Create an object with an image in the app resources.
        /*GeoObject go1 = new GeoObject(1l);
        go1.setGeoPosition(41.90523339794433d, 2.565036406654116d);
        go1.setImageResource(R.drawable.image1);
        go1.setName("Creature 1");

        // Is it also possible to load the image asynchronously form internet
        GeoObject go2 = new GeoObject(2l);
        go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
        //go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
        go1.setImageResource(R.drawable.image5);
        go2.setName("Online image");

        // Also possible to get images from the SDcard
        GeoObject go3 = new GeoObject(3l);
        go3.setGeoPosition(41.90550959641445d, 2.565873388087619d);
        //go3.setImageUri("/sdcard/someImageInYourSDcard.jpeg");
        go1.setImageResource(R.drawable.image5);
        go3.setName("IronMan from sdcard");

        // And the same goes for the app assets
        GeoObject go4 = new GeoObject(4l);
        go4.setGeoPosition(41.90518862002349d, 2.565662767707665d);
        go1.setImageResource(R.drawable.image5);
        //go4.setImageUri("assets://creature_7.png");
        go4.setName("Image from assets"); */


        //Type1
        GeoObject a = new GeoObject(9l);
        a.setGeoPosition(poIs.get(0).getLat(), poIs.get(0).getLng());
        a.setImageResource(R.drawable.image1);
        a.setName(poIs.get(0).getPlace_name());
        a.getDistanceFromUser();

        GeoObject b = new GeoObject(9l);
        b.setGeoPosition(poIs.get(1).getLat(), poIs.get(1).getLng());
        b.setImageResource(R.drawable.image2);
        b.setName(poIs.get(1).getPlace_name());
        b.getDistanceFromUser();

        GeoObject c = new GeoObject(9l);
        c.setGeoPosition(poIs.get(2).getLat(), poIs.get(2).getLng());
        c.setImageResource(R.drawable.image3);
        c.setName(poIs.get(2).getPlace_name());
        c.getDistanceFromUser();

        GeoObject d = new GeoObject(9l);
        d.setGeoPosition(poIs.get(3).getLat(), poIs.get(3).getLng());
        d.setImageResource(R.drawable.image5);
        d.setName(poIs.get(3).getPlace_name());
        d.getDistanceFromUser();

        GeoObject e = new GeoObject(9l);
        e.setGeoPosition(poIs.get(4).getLat(), poIs.get(4).getLng());
        e.setImageResource(R.drawable.image4);
        e.setName(poIs.get(4).getPlace_name());
        e.getDistanceFromUser();

        GeoObject f = new GeoObject(9l);
        f.setGeoPosition(poIs.get(5).getLat(), poIs.get(5).getLng());
        f.setImageResource(R.drawable.image2);
        f.setName(poIs.get(5).getPlace_name());
        f.getDistanceFromUser();

        GeoObject g = new GeoObject(9l);
        g.setGeoPosition(poIs.get(6).getLat(), poIs.get(6).getLng());
        g.setImageResource(R.drawable.image3);
        g.setName(poIs.get(6).getPlace_name());
        g.getDistanceFromUser();

        GeoObject h = new GeoObject(9l);
        h.setGeoPosition(poIs.get(7).getLat(), poIs.get(7).getLng());
        h.setImageResource(R.drawable.image1);
        h.setName(poIs.get(7).getPlace_name());
        h.getDistanceFromUser();



        // Add the GeoObjects to the world
        sharedWorld.addBeyondarObject(a);
       /* sharedWorld.addBeyondarObject(go2, LIST_TYPE_EXAMPLE_1);*/
        sharedWorld.addBeyondarObject(e);
        sharedWorld.addBeyondarObject(f);
        sharedWorld.addBeyondarObject(g);
        sharedWorld.addBeyondarObject(b);
        sharedWorld.addBeyondarObject(c);
        sharedWorld.addBeyondarObject(d);
        sharedWorld.addBeyondarObject(h);


        return sharedWorld;
    }
}
/*

            //Locations
            Parking Block C= 2.918698, 101.772264
            Block A = 2.918380d, 101.771647d
            Block B = 2.918696d, 101.771718d
            Block C = 2.918306d, 101.772027d
            Block D = 2.918113d, 101.771930d
            Block E = 2.918204d, 101.771469d
            Block F = 2.917844d, 101.771535d
            Block G = 2.917908d, 101.771036d
            Block H = 2.917528d, 101.771143d
            Block I = 2.918380d, 101.771647d

*/
