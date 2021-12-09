package edu.asu.bsse.wjsanch2.lab_7_android;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.asu.bsse.wjsanch2.lab_3_android.R;

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

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener {

    private List<HashMap<String,String>> populate;

    private PlaceLibrary dataCollection = new PlaceLibrary();
    private EditText name;
    private EditText description;
    private EditText category;
    private EditText addressTitle;
    private EditText addressStreet;
    private EditText elevation;
    private EditText latitude;
    private EditText longitude;
    private ListView placeView;
    private String[] places;
    private String[] columnNames;
    private int[] columnNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeView = findViewById(R.id.PlaceListView);

//        Intent intent = getIntent();
//        dataCollection = intent.getSerializableExtra("PlaceDescriptions")!=null ? (PlaceLibrary)intent.getSerializableExtra("PlaceDescriptions") :
//                new PlaceLibrary(this);
//        places = dataCollection.getNames();

        try {
            getDatafromDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        places = dataCollection.getNames();


        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, populate, R.layout.placelist, columnNames, columnNum);
        placeView.setAdapter(sa);
        placeView.setOnItemClickListener(this);
    }

    private void prepareAdapter() {
        columnNames = this.getResources().getStringArray(R.array.columnTitle);
        columnNum = new int[] {R.id.nameInfo};
        this.places = dataCollection.getNames();
        Arrays.sort(this.places);
        populate = new ArrayList<>();
        HashMap<String,String> titles = new HashMap<>();
        titles.put("Name","Places");
        populate.add(titles);
        for (String pdName : places) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Name", pdName);
            Log.w(this.getClass().getSimpleName(), pdName + " has  name " +
                    dataCollection.get(pdName).getName());
            populate.add(map);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.add:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> add");
                this.newPlaceWindow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] placeNames = dataCollection.getNames();
        Arrays.sort(placeNames);
        if(position > 0 && position <= placeNames.length) {
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + placeNames[position-1]);
            Intent displayPd = new Intent(this, PDActivity.class);
            displayPd.putExtra("PlaceDescriptions", dataCollection);
            displayPd.putExtra("selected", placeNames[position-1]);
            this.startActivityForResult(displayPd, 1);
        }
    }

    public void log(String message) {
        android.util.Log.d(this.getClass().getSimpleName(), message);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataCollection = (data != null && data.getSerializableExtra("Places") != null) ? (PlaceLibrary) data.getSerializableExtra("Places") : dataCollection;
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, populate, R.layout.placelist, columnNames, columnNum);
        placeView.setAdapter(sa);
        placeView.setOnItemClickListener(this);
    }

    private void newPlaceWindow() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New Place");

        LinearLayout arrangement = new LinearLayout(this);
        arrangement.setOrientation(LinearLayout.VERTICAL);

        this.name = new EditText(this);
            name.setHint("Enter Name");
            arrangement.addView(name);
            name.setInputType(InputType.TYPE_CLASS_TEXT);

        this.description = new EditText(this);
            description.setHint("Enter Description");
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            arrangement.addView(description);

        this.category = new EditText(this);
            category.setHint("Enter Category");
            category.setInputType(InputType.TYPE_CLASS_TEXT);
            arrangement.addView(category);

        this.addressTitle = new EditText(this);
            addressTitle.setHint("Enter Address Title");
            addressTitle.setInputType(InputType.TYPE_CLASS_TEXT);
            arrangement.addView(addressTitle);

        this.addressStreet = new EditText(this);
            addressStreet.setHint("Enter Address Street");
            addressStreet.setInputType(InputType.TYPE_CLASS_TEXT);
            arrangement.addView(addressStreet);

        this.elevation = new EditText(this);
            elevation.setHint("Enter Elevation");
            elevation.setInputType(InputType.TYPE_CLASS_NUMBER);
            arrangement.addView(elevation);

        this.latitude = new EditText(this);
            latitude.setHint("Enter Latitude");
            latitude.setInputType(InputType.TYPE_CLASS_NUMBER);
            arrangement.addView(latitude);

        this.longitude = new EditText(this);
            longitude.setHint("Enter Longitude");
            longitude.setInputType(InputType.TYPE_CLASS_NUMBER);
            arrangement.addView(longitude);

        alert.setView(arrangement);
        alert.setNegativeButton("Cancel", this);
        alert.setPositiveButton("Add Place", this);
        alert.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+ (which==DialogInterface.BUTTON_POSITIVE));
        if(which == DialogInterface.BUTTON_POSITIVE) {
            String nameInput = name.getText().toString().equals("") ? "blank" : name.getText().toString();
            String descriptionInput = description.getText().toString().equals("") ? "blank" : description.getText().toString();
            String categoryInput = category.getText().toString().equals("") ? "blank" : category.getText().toString();
            String addressTitleInput = addressTitle.getText().toString().equals("") ? "blank" : addressTitle.getText().toString();
            String addressStreetInput = addressStreet.getText().toString().equals("") ? "blank" : addressStreet.getText().toString();
            String elevationInput = elevation.getText().toString().equals("") ? "0" : elevation.getText().toString();
            String latitudeInput = latitude.getText().toString().equals("") ? "0" : latitude.getText().toString();
            String longitudeInput = longitude.getText().toString().equals("") ? "0" : longitude.getText().toString();

            PlaceDescription place_description = new PlaceDescription(
                    nameInput, descriptionInput, categoryInput, addressTitleInput, addressStreetInput,
                    Integer.parseInt(elevationInput), Double.parseDouble(latitudeInput), Double.parseDouble(longitudeInput)
            );

            try {
                addPlace(place_description);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            prepareAdapter();
            SimpleAdapter adapter = new SimpleAdapter(this, populate, R.layout.placelist, columnNames, columnNum);
            placeView.setAdapter(adapter);
        }
    }

    private void addPlace(PlaceDescription place) throws SQLException {
        PlaceDB placeDatabase = new PlaceDB(this);
        SQLiteDatabase placeDB = placeDatabase.openDB();
        String insert = "insert into places values (" + place.toSQL() + ");";
        placeDB.execSQL(insert);
        log("Added place: " + place.getName());
        getDatafromDB();
    }

    private void getDatafromDB() throws SQLException {
        PlaceDB placeData = new PlaceDB(this);
        SQLiteDatabase placeDatabase = placeData.openDB();
        Cursor pointer = placeDatabase.rawQuery("select * from places", new String[]{});
        dataCollection.empty();

        while(pointer.moveToNext()){
            try {
                PlaceDescription place = new PlaceDescription();
                place.setName(pointer.getString(0));
                place.setDescription(pointer.getString(1));
                place.setCategory(pointer.getString(2));
                place.setAddressTitle(pointer.getString(3));
                place.setAddressStreet(pointer.getString(4));
                place.setElevation(pointer.getInt(5));
                place.setLatitude(pointer.getDouble(6));
                place.setLongitude(pointer.getDouble(7));
                dataCollection.add(place);
            } catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),ex.getMessage());
            }
        }
        pointer.close();
    }
}
