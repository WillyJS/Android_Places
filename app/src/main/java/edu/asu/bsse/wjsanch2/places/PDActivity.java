package edu.asu.bsse.wjsanch2.lab_7_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
 * @Version October 30, 2021
 */
public class PDActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private String pickedPlace;
    private PlaceLibrary placeCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdactivity);
        TextView name = findViewById(R.id.nameInfo);
        TextView description = findViewById(R.id.descriptionInfo);
        TextView category = findViewById(R.id.categoryInfo);
        TextView addressTitle = findViewById(R.id.address_title_info);
        TextView addressStreet = findViewById(R.id.address_street_info);
        TextView elevation = findViewById(R.id.elevationInfo);
        TextView latitude = findViewById(R.id.latitudeInfo);
        TextView longitude = findViewById(R.id.longitudeInfo);

        Intent intent = getIntent();
        placeCollection = intent.getSerializableExtra("PlaceDescriptions")!=null ? (PlaceLibrary)intent.getSerializableExtra("PlaceDescriptions") : new PlaceLibrary();
        pickedPlace = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";

        PlaceDescription place = placeCollection.get(pickedPlace);
        name.setText(place.getName());
        description.setText(place.getDescription());
        category.setText(place.getCategory());
        addressTitle.setText(place.getAddressTitle());
        addressStreet.setText(place.getAddressStreet());
        elevation.setText("" + place.getElevation());
        latitude.setText(Double.toString(place.getLatitude()));
        longitude.setText(Double.toString(place.getLongitude()));

        // set up a back button to return to the view main activity / view
        try {
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception action bar: "+ex.getLocalizedMessage());
        }
        setTitle("Place Description");
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected() id: "+item.getItemId()
                +" title "+item.getTitle());
        switch (item.getItemId()) {
            // the user selected the up/home button (left arrow at left of action bar)
            case android.R.id.home:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> home");
                Intent i = new Intent();
                i.putExtra("Places", placeCollection);
                this.setResult(RESULT_OK,i);
                finish();
                return true;
            // the user selected the action (garbage can) to remove the student
            case R.id.remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removePlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // show an alert view for the user to confirm removing the selected student
    private void removePlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove place "+this.pickedPlace + "?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove Place", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newPlaceWindow method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            placeCollection.remove(this.pickedPlace);
            Intent i = new Intent();
            i.putExtra("Places", placeCollection);
            this.setResult(RESULT_OK,i);
            finish();
        }
    }
}
