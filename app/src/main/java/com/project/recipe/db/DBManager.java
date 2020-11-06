package com.project.recipe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.project.recipe.models.Drink;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*database modified for Search function*/
public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Recipes";
    public static final String TABLE_USER = "Users";
    public static final String RECIPE_TABLE_NAME ="Recipe" ;


    private HashMap hp;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
//create databse at first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(

                "create table " + RECIPE_TABLE_NAME+ "(id TEXT,title TEXT,instructions TEXT,imagePath TEXT,isAlcoholic TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Recipe");
        onCreate(db);
    }



//insert data function
    public boolean insertRecipeData(String id, String title, String instructions,String alcoholic,String path) {

        String sql = "INSERT INTO " + RECIPE_TABLE_NAME + " (id,title,instructions,imagePath,isAlcoholic) VALUES(?,?,?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement insertStmt = db.compileStatement(sql);

            insertStmt.bindString(1, id);
            insertStmt.bindString(2, title);
            insertStmt.bindString(3, instructions);
            insertStmt.bindString(4, path);
            insertStmt.bindString(5,alcoholic);

            insertStmt.executeInsert();
            db.close();

        return true;
    }

    public boolean updateRecipeData(String id, String title,String instructions, String alcoholic,String path) {


        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("instructions",instructions);
        cv.put("imagePath",path);
        cv.put("isAlcoholic",alcoholic);

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update(RECIPE_TABLE_NAME, cv,  "id=\"" + id + "\"", null)>0;


    }

//collect data from the list
    public List<Drink> getRecipeData() {

        List<Drink> lstDrinks = new ArrayList<Drink>();
        Drink drink=null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+RECIPE_TABLE_NAME,null);

        while(res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String title = res.getString(1);
            String instructions = res.getString(2);
            String path = res.getString(3);
            String alcoholic=res.getString(4);
            //Uri uri = Uri.fromFile(new File(path));


           drink = new Drink(id,title,instructions,path.toString(),alcoholic);
            lstDrinks.add(drink);
        }
        return lstDrinks;
    }

    public boolean isRecipeExists(String id)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+RECIPE_TABLE_NAME+" where id = "+id,null);

        while(res.moveToNext()) {
          return true;
        }
        return false;
    }
    public void deleteRecipe(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+RECIPE_TABLE_NAME+" WHERE id="+id);

    }


    //data deletion added
    public void deleteDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+RECIPE_TABLE_NAME);

    }






}
