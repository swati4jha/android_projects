package com.mad.group24_hw06;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class AppsUtil {

        static public class AppsJSONParser{
            static ArrayList<App> parseApps(String in) throws JSONException {
                ArrayList<App> appsList = new ArrayList<>();
                JSONObject root = new JSONObject(in);
                Iterator<String> keys = root.keys();
                JSONArray appsJSONArray = ((JSONArray)(((JSONObject)root.get("feed")).get("entry")));

                for(int i=0;i<appsJSONArray.length();i++){
                    JSONObject appJSONObject = appsJSONArray.getJSONObject(i);
                    App app = App.createApp(appJSONObject);
                    appsList.add(app);
                }
                return appsList;
            }
    }
}
