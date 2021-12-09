package edu.asu.bsse.wjsanch2.lab_7_android;

import java.io.Serializable;
import java.util.Hashtable;

/*
 * Copyright © 2021 William Sanchez. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: An app to demonstrate a ListView with a SimpleAdapter
 * The simple adapter is not so simple, but it does allow a list with multiple row components
 * to be constructed from a pre-defined adapter. Both activities of this example create and
 * manipulate a listView using a simple adapter
 *
 * Dr Baron & grading staff of SER423 of Arizona State University have the right to build and evaluate the software
 * package for the purpose of determining grade and program assessment.
 *
 * @Author William Sanchez mailto:wjsanch2@asu.edu & Tim Lindquist Tim.Lindquist@asu.edu
 * @Version November 19, 2021
 */

public class PlaceLibrary implements Serializable {

    private Hashtable<String, PlaceDescription> placeList;

//    public PlaceLibrary(Activity parent) {
//        list = new Hashtable<>();
//        try {
//            this.resetFromJsonFile(parent);
//        } catch (Exception e) {
//            log("Error resetting place descriptions from json file" + e.getMessage());
//        }
//    }

    public PlaceLibrary() { placeList = new Hashtable<>(); }

    public void log(String message) {
        android.util.Log.d(this.getClass().getSimpleName(), message);
    }

//    public boolean resetFromJsonFile(Activity parent) {
//        boolean ret = true;
//        try {
//            list.clear();
//            InputStream is = parent.getApplicationContext().getResources().openRawResource(R.raw.place_locations);
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            // note that the json is in a multiple lines of input so need to read line-by-line
//            StringBuffer sb = new StringBuffer();
//            while (br.ready()) {
//                sb.append(br.readLine());
//            }
//            String placesJsonStr = sb.toString();
//            JSONObject placesJson = new JSONObject(new JSONTokener(placesJsonStr));
//            Iterator<String> it = placesJson.keys();
//            while (it.hasNext()) {
//                String pName = it.next();
//                JSONObject place = placesJson.optJSONObject(pName);
//                log(pName + " json is: " + place.toString());
//                if (place != null) {
//                    PlaceDescription pd = new PlaceDescription(place.toString());
//                    list.put(pName, pd);
//                }
//            }
//        } catch (Exception ex) {
//            log("Exception reading json file: " + ex.getMessage());
//            ret = false;
//        }
//        return ret;
//    }

    public void add(PlaceDescription pd) {
        log("adding place: " + ((pd == null) ? "unknown" : pd.getName()));
        try {
            placeList.put(pd.getName(), pd);
        } catch (Exception ex) {
        }
    }

//    public boolean add(PlaceDescription pd) {
//        boolean ret = true;
//        log("adding placeDescription: " + ((pd == null) ? "unknown" : pd.getName()));
//        try {
//            list.put(pd.getName(), pd);
//        } catch (Exception ex) {
//            ret = false;
//        }
//        return ret;
//    }

    public void remove(String pName) {
        log("removing place: " + pName);
        placeList.remove(pName);
    }

//    public boolean remove(String pName) {
//        log("removing placeDescription: " + pName);
//        return (list.remove(pName) != null);
//    }

    public String[] getNames() {
        String[] ret = {};
        log("getting " + placeList.size() + " place names");
        if (placeList.size() > 0) {
            ret = (String[]) (placeList.keySet()).toArray(new String[0]);
        }
        return ret;
    }

    public PlaceDescription get(String pName) {
        PlaceDescription ret = new PlaceDescription();
        PlaceDescription pd = placeList.get(pName);
        if (pd != null) {
            ret = pd;
        }
        return ret;
    }

    public void empty() {
        log("Clearing collection!");
        placeList.clear();
    }
}

