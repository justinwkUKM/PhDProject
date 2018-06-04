package com.example.myxlab.gdktry.Google;

import android.app.Activity;

/**
 * Created by MyXLab on 17/1/2018.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.myxlab.gdktry.R;
import com.example.myxlab.gdktry.ar.SimpleCameraActivity;
import com.google.android.glass.app.Card.ImageLayout;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

public class CardActivityTest extends Activity {

    private List<POI> mCards;
    private CardScrollView mCardScrollView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        prepareMovieCards();

        mCardScrollView = new CardScrollView(this);
        POICardAdapter adapter = new POICardAdapter(context, mCards);
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setupClickListener();
        setContentView(mCardScrollView);


    }

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.SUCCESS);

                String s = mCards.get(position).getPlace_name();
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

                DBHandler dbHandler = new DBHandler(getApplicationContext(), null);
                POI geoObj = dbHandler.getPOI(s);
                Log.e(geoObj.getPlace_name(), geoObj.getPlace_vicinity());

                Intent i = new Intent(CardActivityTest.this, CardActivitySingle.class);
                i.putExtra("geo", geoObj); // sending our object
                startActivity(i);
            }
        });
    }

    private void prepareMovieCards() {
        mCards = new ArrayList<POI>();
        Bundle bundle = getIntent().getExtras();
        mCards = (List<POI>) bundle.getSerializable("cards");

    }
}
