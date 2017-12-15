package com.mymorningroutine.server;

import java.io.IOException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BDDbu {

	protected SQLiteDatabase bdd;

	protected DatabaseHandler maBaseSQLite;

	public BDDbu(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new DatabaseHandler(context);

		try {

			maBaseSQLite.createDataBase();

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			maBaseSQLite.openDataBase();

		}catch(SQLException sqle){

			throw sqle;

		}
	}

	public void open(){
		//on ouvre la BDD en écriture
		if(bdd == null) {
			bdd = maBaseSQLite.getWritableDatabase();
		}
	}

	public void close(){
		//on ferme l'accès à la BDD
		if(bdd.isOpen()) {
			bdd.close();
		}
	}
}
