package com.mymorningroutine.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	private static String DATABASE_PATH;

	 private SQLiteDatabase myDataBase; 
	 
	// Database Name
	private static final String DATABASE_NAME = "MyRoutines.db";

	private static Context myContext;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DatabaseHandler.myContext = context;
		DatabaseHandler.DATABASE_PATH = "/data/data/" + myContext.getPackageName() + "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	 /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        
    	boolean dbExist = checkDataBase();
    	Log.v("createDatabase()", "Does the database exist? " + dbExist);
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we a"re gonna be able to overwrite that database with our database.
        	this.getWritableDatabase();

			try {

    			copyDataBase();

    		} catch (IOException e) {

        		throw new Error("Error copying database");

        	}
    	}
 
    }
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 		Cursor c = null;
    	try{
    		String myPath = DATABASE_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		 
			c = checkDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

    		if (c.moveToFirst()) {
    		    while ( !c.isAfterLast() ) {
    		    	Log.v("getTables",  "Table Name=> "+c.getString(0));
    		        c.moveToNext();
    		    }
    		}
    	}catch(SQLiteException e){
    		Log.v("checkDatabase()", "Could not open the database " + DATABASE_NAME);
    	}
 		
			if(c !=null)
			c.close();
		
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DATABASE_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
