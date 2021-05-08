package com.android.cloud_project.bloodhelper.models;

import android.util.Log;
import java.util.List;
import java.util.HashMap;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class DataModel {

    private HashMap<String, String> getPlaceOnMap(JSONObject jsonObject)
    {
        HashMap<String, String> placemap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity= "--NA--";
        String latitude= "";
        String longitude="";
        String reference="";

        Log.d("DataParser","jsonobject ="+jsonObject.toString());


        try {
            if (!jsonObject.isNull("name")) {
                placeName = jsonObject.getString("name");
            }
            latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng");

            if (!jsonObject.isNull("vicinity")) {
                vicinity = jsonObject.getString("vicinity");
            }
            reference = jsonObject.getString("reference");

            placemap.put("place_name", placeName);
            placemap.put("vicinity", vicinity);
            placemap.put("lat", latitude);
            placemap.put("lng", longitude);
            placemap.put("reference", reference);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return placemap;

    }
    private List<HashMap<String, String>> grabPlacesData(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlaceOnMap((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public List<HashMap<String, String>> parseData(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return grabPlacesData(jsonArray);
    }
}