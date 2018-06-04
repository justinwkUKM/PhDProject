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

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.example.myxlab.gdktry.R;

import java.util.ArrayList;
import java.util.Iterator;

public class ChangeGeoObjectImagesOnTouchActivity extends FragmentActivity implements OnTouchBeyondarViewListener,
		OnClickBeyondarObjectListener {

	private BeyondarFragmentSupport mBeyondarFragment;
	private World mWorld;
	ImageView imgView;

	private TextView mLabelText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		loadViewFromXML();

		// We create the world and fill it
		mWorld = CustomWorldHelper.generateObjects(this);

		mBeyondarFragment.setWorld(mWorld);
		mBeyondarFragment.showFPS(true);

		// set listener for the geoObjects
		mBeyondarFragment.setOnTouchBeyondarViewListener(this);
		mBeyondarFragment.setOnClickBeyondarObjectListener(this);

		mLabelText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Point point = getCenterPointOfView(imgView);
				Log.d("point", "view point x,y (" + point.x + ", " + point.y + ")");
			}
		});

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

		float x = event.getX();
		float y = event.getY();

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
		mLabelText.setText("Event: " + textEvent);
	}

	private void loadViewFromXML() {
		setContentView(R.layout.camera_with_text);
		mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
				R.id.beyondarFragment);

		mLabelText = (TextView) findViewById(R.id.labelText);
		imgView = (ImageView) findViewById(R.id.imgView);
	}

	@Override
	public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
		if (beyondarObjects.size() > 0) {
			beyondarObjects.get(0).setImageResource(R.drawable.app_icon);
		}
	}

}
