package com.application.ravnandroidclient.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class GiphyModel {
    int id;
    String type;
    String title;
    String src;
    String author;
    Date date;
    int viewCount;
    int viewOrder;

    public GiphyModel(JSONObject object) throws JSONException {
        id = (int)object.get("id");
        type = (String)object.get("type");
        title = (String)object.get("title");
        src = (String)object.get("src");
        author = (String)object.get("author");
        date = new Date((long)object.get("date"));
        viewCount = (int)object.get("viewCount");
        viewOrder = (int)object.get("viewOrder");
    }


    public ArrayList<GiphyModel> createGipjhyList(JSONArray array) throws JSONException{
        ArrayList<GiphyModel> giphyList = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            giphyList.add(new GiphyModel((JSONObject) array.get(i)));
        }
        return giphyList;
    }


}
