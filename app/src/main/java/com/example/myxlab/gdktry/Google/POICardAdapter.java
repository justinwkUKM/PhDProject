package com.example.myxlab.gdktry.Google;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

/**
 * Created by MyXLab on 17/1/2018.
 */

public class POICardAdapter extends CardScrollAdapter {
    private List<POI> mCards;
    private Context context;

    public POICardAdapter(Context context, List<POI> mCards) {
        this.context = context;
        this.mCards = mCards;
    }

    @Override
    public int getPosition(Object item) {
        return mCards.indexOf(item);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int position) {
        return mCards.get(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = new Card(context);

        POI mc = mCards.get(position);

        // Card text
        if (mc.getPlace_name() != null)
            card.setText(mc.getPlace_name() + "\nRating:" + mc.getPlace_rating()+"/5");
        // Card text
        if (mc.getPlace_vicinity() != null) {
            if (mc.isPlace_open()) {
                card.setFootnote("OPEN NOW \n" + mc.getPlace_vicinity());
            } else {
                card.setFootnote(mc.getPlace_vicinity());
            }
        }

        // Set image layout
        if (mc.getImgLayout() != null)
            card.setImageLayout(mc.getImgLayout());

        // loop and set card images
        for(int img : mc.getImages()){
            card.addImage(img);
        }

       /*// Card text
        if (mc.getPlace_rating() != 0)
            card.setSubheading(mc.getPlace_rating()+"");*/





        return card.getView();
    }



}
