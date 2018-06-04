package com.example.myxlab.gdktry;

/**
 * Created by MyXLab on 14/9/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//1
public class DataManager {

    private Context context;
    private static final String StoredStringsKey = "com.raywenderlich.shoppinglist.storedstringskey";
    private static final String PreferencesLocation = "com.raywenderlich.shoppinglist";

    public DataManager(Context c){
        context = c;
    }
    //2
    public ArrayList<String> getStoredStrings(){
        SharedPreferences preferences = context.getSharedPreferences(PreferencesLocation, Context.MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet(StoredStringsKey, Collections.<String>emptySet());
        return new ArrayList<String>(stringSet);
    }
    //3
    public void setStoredStrings(ArrayList<String> strings){
        SharedPreferences preferences = context.getSharedPreferences(PreferencesLocation, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> stringSet = new HashSet<String>(strings);
        editor.putStringSet(StoredStringsKey, stringSet);
        editor.apply();
    }
}
