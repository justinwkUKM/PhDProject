package com.example.myxlab.gdktry.Google;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "NEARBYPOI.db";
    private static final String TABLE_NAME_POI_DATA = "POI_DATA";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_POI_NAME = "place_name";
    private static final String COLUMN_POI_ID = "place_id";
    private static final String COLUMN_POI_VICINITY = "place_vicinity";
    private static final String COLUMN_POI_RATING = "place_rating";
    private static final String COLUMN_POI_LAT = "place_lat";
    private static final String COLUMN_POI_LNG = "place_lng";
    private static final String COLUMN_POI_IS_OPEN = "place_is_open";


    private static final String CREATE_TABLE_POI = "CREATE TABLE "+ TABLE_NAME_POI_DATA +" (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_POI_NAME + " TEXT, "+ COLUMN_POI_ID + " TEXT, "+ COLUMN_POI_VICINITY + " TEXT, "+ COLUMN_POI_LAT + " REAL, "+COLUMN_POI_LNG + " TEXT, "+COLUMN_POI_RATING + " REAL " + ")";


    public DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_POI);
           }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME_POI_DATA + "'");
        onCreate(sqLiteDatabase);
    }

    public List<POI> getPOIs(){
        List<POI> poiList = new LinkedList<POI>();
        String query = "Select * From " + TABLE_NAME_POI_DATA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        POI data;
        if (cursor.moveToFirst()){

            do {
                data = new POI();
                data.setId(cursor.getInt(0));
                data.setPlace_name(cursor.getString(1));
                data.setPlace_id(cursor.getString(2));
                data.setPlace_vicinity(cursor.getString(3));
                data.setLat(cursor.getDouble(4));
                data.setLng(cursor.getDouble(5));
                data.setPlace_rating(cursor.getDouble(6));

                poiList.add(data);
            }while (cursor.moveToNext());
        }
        db.close();
        return poiList;
    }

    public void addPOI(POI poi){

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_POI_NAME,poi.getPlace_name());
        contentValues.put(COLUMN_POI_ID,poi.getPlace_id());
        contentValues.put(COLUMN_POI_VICINITY,poi.getPlace_vicinity());
        contentValues.put(COLUMN_POI_LAT,poi.getLat());
        contentValues.put(COLUMN_POI_LNG,poi.getLng());
        contentValues.put(COLUMN_POI_RATING,poi.getPlace_rating());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME_POI_DATA, null, contentValues);

        db.close();
    }

    public void delPOIByName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_POI_DATA +" where "+ COLUMN_POI_NAME +"='"+name+"'");

    }

    public void delPOIByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_POI_DATA +" where "+ COLUMN_ID +"='"+id+"'");

    }

    public void delAllData(){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME_POI_DATA ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.execSQL(CREATE_TABLE_POI);
        db.close();
    }

    public POI getPOI(String name){
        String query = "SELECT * FROM " + TABLE_NAME_POI_DATA + " WHERE "+ COLUMN_POI_NAME + " LIKE \'" + name + "\' ORDER BY " + COLUMN_POI_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        POI data = null;
        if (cursor.moveToFirst()){
            data = new POI();
            data.setId(cursor.getInt(0));
            data.setPlace_name(cursor.getString(1));
            data.setPlace_id(cursor.getString(2));
            data.setPlace_vicinity(cursor.getString(3));
            data.setLat(cursor.getDouble(4));
            data.setLng(cursor.getDouble(5));
            data.setPlace_rating(cursor.getDouble(6));

        }
        db.close();
        cursor.close();
        return data;
    }

    /*public POI getPOIs(String code){
        String query = "SELECT * FROM " + TABLE_NAME_POI_DATA + " WHERE "+ COLUMN_CODE + " LIKE \'" + code + "\' ORDER BY " + COLUMN_POI_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        POI poi = null;
        if (cursor.moveToFirst()){
            poi = new POI();
            poi.setName(cursor.getString(1));
            poi.setLat(cursor.getString(2));
            poi.setLon(cursor.getString(3));
            poi.setCode(cursor.getString(4));
            poi.setType(cursor.getString(5));
            poi.setPhone(cursor.getString(6));
            poi.setEmail(cursor.getString(7));
            poi.setWebsite(cursor.getString(8));
        }
        db.close();
        cursor.close();
        return poi;
    }

    public List<POI> searchPOIs(String key){
        List<POI> POIList = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_NAME_POI_DATA + " WHERE "+ COLUMN_POI_NAME + " LIKE \'%" + key + "%\' ORDER BY " + COLUMN_POI_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        POI poi;
        if (cursor.moveToFirst()){

            do {
                poi = new POI();
                poi.setName(cursor.getString(1));
                poi.setLat(cursor.getString(2));
                poi.setLon(cursor.getString(3));
                poi.setCode(cursor.getString(4));
                poi.setType(cursor.getString(5));
                POIList.add(poi);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return POIList;
    }

    public List<POI> searchPOICat(String key, String type){
        List<POI> POIList = new LinkedList<>();
        String query;

        if (key.length() == 0){
            query = "SELECT * FROM " + TABLE_NAME_POI_DATA + " WHERE "+ COLUMN_TYPE + " = \'" + type + "\' ORDER BY " + COLUMN_POI_NAME;
        } else {
            query = "SELECT * FROM " + TABLE_NAME_POI_DATA + " WHERE "+ COLUMN_TYPE + " = \'" + type +  "\' AND " + COLUMN_POI_NAME + " LIKE \'%"+ key +"%\' ORDER BY " + COLUMN_POI_NAME;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        POI poi;
        if (cursor.moveToFirst()){

            do {
                poi = new POI();
                poi.setName(cursor.getString(1));
                poi.setLat(cursor.getString(2));
                poi.setLon(cursor.getString(3));
                poi.setCode(cursor.getString(4));
                poi.setType(cursor.getString(5));
                POIList.add(poi);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return POIList;
    }

    public void delAllData(){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME_POI_DATA ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.execSQL(CREATE_TABLE_POI);
        db.close();
    }

    public List<BusStop> getBusStopList(){
        List<BusStop> BusStopList = new LinkedList<>();
        String query = "Select * From " + TABLE_NAME_BUS_STOPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        BusStop poi;
        if (cursor.moveToFirst()){

            do {
                poi = new BusStop();
                poi.setName(cursor.getString(1));
                poi.setLat(cursor.getDouble(2));
                poi.setLon(cursor.getDouble(3));
                poi.setCode(cursor.getString(4));
                BusStopList.add(poi);
            }while (cursor.moveToNext());
        }
        db.close();
        return BusStopList;
    }

    public void addBusStop(BusStop poiData){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_POI_NAME,poiData.getName());
        contentValues.put(COLUMN_LAT,poiData.getLat());
        contentValues.put(COLUMN_LON,poiData.getLon());
        contentValues.put(COLUMN_CODE,poiData.getCode());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME_BUS_STOPS, null, contentValues);
        db.close();
    }

    public BusStop getBusStops(String code){
        String query = "SELECT * FROM " + TABLE_NAME_BUS_STOPS + " WHERE "+ COLUMN_CODE + " LIKE \'" + code + "\' ORDER BY " + COLUMN_POI_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        BusStop poi = null;
        if (cursor.moveToFirst()){
            poi = new BusStop();
            poi.setName(cursor.getString(1));
            poi.setLat(cursor.getDouble(2));
            poi.setLon(cursor.getDouble(3));
            poi.setCode(cursor.getString(4));

        }
        db.close();
        cursor.close();
        return poi;
    }

    public List<BusStop> searchBusStops(String key){
        List<BusStop> BusStopList = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_NAME_BUS_STOPS + " WHERE "+ COLUMN_POI_NAME + " LIKE \'%" + key + "%\' ORDER BY " + COLUMN_POI_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        BusStop poi;
        if (cursor.moveToFirst()){

            do {
                poi = new BusStop();
                poi.setName(cursor.getString(1));
                poi.setLat(cursor.getDouble(2));
                poi.setLon(cursor.getDouble(3));
                poi.setCode(cursor.getString(4));

                BusStopList.add(poi);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return BusStopList;
    }

    public List<BusStop> searchBusStopCat(String key, String code){
        List<BusStop> BusStopList = new LinkedList<>();
        String query;

        if (key.length() == 0){
            query = "SELECT * FROM " + TABLE_NAME_BUS_STOPS + " WHERE "+ COLUMN_CODE + " = \'" + code + "\' ORDER BY " + COLUMN_POI_NAME;
        } else {
            query = "SELECT * FROM " + TABLE_NAME_BUS_STOPS + " WHERE "+ COLUMN_CODE + " = \'" + code +  "\' AND " + COLUMN_POI_NAME + " LIKE \'%"+ key +"%\' ORDER BY " + COLUMN_POI_NAME;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        BusStop poi;
        if (cursor.moveToFirst()){

            do {
                poi = new BusStop();
                poi.setName(cursor.getString(1));
                poi.setLat(cursor.getDouble(2));
                poi.setLon(cursor.getDouble(3));
                poi.setCode(cursor.getString(4));

                BusStopList.add(poi);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return BusStopList;
    }

    public void delAllBusStopsData(){

        String query = "DROP TABLE IF EXISTS " + TABLE_NAME_BUS_STOPS ;;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.execSQL(CREATE_TABLE_BS);
        db.close();
    }
*/
}
