package com.example.myxlab.gdktry.Google;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyXLab on 17/8/2017.
 */

public class CardActivitySingle extends Activity {


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        POI model = (POI) getIntent().getSerializableExtra("geo");
        String s = "";
        if (model.isPlace_open()){
            s = "Open Now";
        }else {
            s = "Closed ";
        }

        View view2 = new CardBuilder(context, CardBuilder.Layout.AUTHOR)
                .setText(model.getPlace_name())
                .setIcon(R.drawable.ic_glass_logo)
                .setHeading(model.getPlace_vicinity())
                .setSubheading("Ticket Price RM 15 / Free Wifi")
                .setFootnote("Rating:"+model.getPlace_rating()+"/5")
                .setTimestamp(s)
                .getView();
        setContentView(view2);
    }


}
