package com.example.computerquiz.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.computerquiz.model.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by xitij on 06-03-2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DATABASE_WRAPPER";
    private  String DB_PATH = "/data/data/com.example.computerquiz/databases/";
    private static String DB_NAME = "quiz.db";
    private SQLiteDatabase myDataBase = null;
    private Context myContext;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME,null,1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if(dbExist){
//      Log.v("log_tag", "database does exist");
        }else{
//            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        myDataBase = this.getWritableDatabase();

      //  String wher = "role_name_" + " = ? AND " + "main_line_type_" + " = ?";
       // String[] FIELDS = { "cast_matches_" };
        Cursor cursor =   myDataBase.query("Category", null, null, null, null, null, null);
        String castMatchesString = null;
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {

                Category category = new Category();
                category._id = cursor.getInt(cursor.getColumnIndex("_id"));
                category.category_name = cursor.getString(cursor.getColumnIndex("name"));
                categories.add(category);

            } while (cursor.moveToNext());
        }

        return categories;

    }


    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase(){

        File folder = new File(DB_PATH);
        if(!folder.exists()){
            folder.mkdir();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;

    }
    @Override
    public synchronized void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}