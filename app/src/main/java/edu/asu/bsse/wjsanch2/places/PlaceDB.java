package edu.asu.bsse.wjsanch2.lab_7_android;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

//import com.example.ser423Lab.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

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
 * @Version November 17, 2021
 */

public class PlaceDB extends SQLiteOpenHelper {
    private static final boolean debugon = true;
    private static final int DATABASE_VERSION = 3;
    private static final String dbName = "places";
    private String dbPath;
    private SQLiteDatabase placesDatabase;
    private final Context context;

    public PlaceDB(Context context){
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        // place the database in the files directory. Could also place it in the databases directory
        // with dbPath = context.getDatabasePath("dbName"+".db").getPath();
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"db path is: "+
                context.getDatabasePath(dbName));
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

//    public void createDB() {
//      this.getReadableDatabase();
//      try {
//        copyDB();
//      } catch (IOException e) {
//        android.util.Log.w(this.getClass().getSimpleName(),
//                "createDB Error copying database " + e.getMessage());
//      }
//    }

    /**
     * Does the database exist and has it been initialized? This method determines whether
     * the database needs to be copied to the data/data/pkgName/files directory by
     * checking whether the file exists. If it does it checks to see whether the db is
     * uninitialized or whether it has the course table.
     * @return false if the database file needs to be copied from the assets directory, true
     * otherwise.
     */
    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean placeTabExists = false;
        try{
            String path = dbPath + dbName + ".db";
            debug("PlacesDatabase --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("PlacesDatabase --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='places';", null);
                    if(tabChk == null){
                        debug("PlacesDatabase --> checkDB","check for place table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("PlacesDatabase --> checkDB","check for place table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : tabChk.getString(0))));
                        placeTabExists = !tabChk.isAfterLast();
                    }
                    if(placeTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM places", null);
                        c.moveToFirst();
                        while(!c.isAfterLast()) {
                            String placeName = c.getString(0);
                            String placeDescription = c.getString(1);
                            debug("PlaceDatabase --> checkDB","Place table has PlaceName: "+
                                    placeName+"\tPlace Description: "+placeDescription);
                            c.moveToNext();
                        }
                        placeTabExists = true;
                        c.close();
                    }
                    if (tabChk != null) {
                        tabChk.close();
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("PlacesDatabase->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return placeTabExists;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("PlacesDatabase --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.places);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("PlacesDatabase --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            placesDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("PlacesDatabase --> openDB", "opened db at path: " + placesDatabase.getPath());
        }else{
            try {
                this.copyDB();
                placesDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return placesDatabase;
    }

    @Override
    public synchronized void close() {
        if(placesDatabase != null)
            placesDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }

}
