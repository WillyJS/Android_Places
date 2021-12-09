package edu.asu.bsse.wjsanch2.lab_7_android;

import org.json.JSONObject;

import java.io.Serializable;

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
 * @Author William Sanchez mailto:wjsanch2@asu.edu
 * @Version November 19, 2021
 */

public class PlaceDescription implements Serializable {

    private String name;
    private String description;
    private String category;
    private String addressTitle;
    private String addressStreet;
    private int elevation;
    private double latitude;
    private double longitude;


    public PlaceDescription() {
        this.name = "";
        this.description = "";
        this.category = "";
        this.addressTitle = "";
        this.addressStreet = "";
        this.elevation = 0;
        this.latitude = 0;
        this.longitude = 0;
    }

    public PlaceDescription(String name, String description, String category, String addressTitle, String addressStreet, int elevation, double latitude, double longitude)
    {
        this.name  = name;
        this.description = description;
        this.category = category;
        this.addressTitle = addressTitle;
        this.addressStreet = addressStreet;
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PlaceDescription(String jsonStr) {
        try{
            JSONObject jo = new JSONObject(jsonStr);
            this.name = jo.getString("name");
            this.description = jo.getString("description");
            this.category = jo.getString("category");
            this.addressTitle = jo.getString("address-title");
            this.addressStreet = jo.getString("address-street");
            this.elevation = jo.getInt("elevation");
            this.latitude = jo.getDouble("latitude");
            this.longitude = jo.getDouble("longitude");
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"error creating / updating json file");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String address) {
        this.addressStreet = addressStreet;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toJsonString(){
        String result = "";
        try{
            JSONObject jsonData = new JSONObject();
            jsonData.put("name", this.name);
            jsonData.put("description", this.description);
            jsonData.put("category", this.category);
            jsonData.put("address-title", this.addressTitle);
            jsonData.put("address-street", addressStreet);
            jsonData.put("elevation", this.elevation);
            jsonData.put("latitude", this.latitude);
            jsonData.put("longitude", this.longitude);

            result = jsonData.toString();

        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error creating / updating json file");
        }
        return result;
    }
    public String toString() {
        String result = "Name : " + this.name + "\nDescription: " + this.description + "\nCategory: " + this.category + "\nAddress line 1: " + this.addressTitle +
                "\nAddress line 2: " + this.addressStreet + "\nElevation: " + this.elevation + "\nLatitude: " + this.latitude + "\nLongitude: " + this.longitude;

        return result;
    }

    public String toSQL() {
        String result = "";

        result += "\"" + this.getName() + "\",";
        result += "\"" + this.getDescription() + "\",";
        result += "\"" + this.getCategory() + "\",";
        result += "\"" + this.getAddressTitle() + "\",";
        result += "\"" + this.getAddressStreet() + "\",";
        result += this.getElevation() + ",";
        result += this.getLatitude() + ",";
        result += this.getLongitude() + "";

        return result;
    }

}

