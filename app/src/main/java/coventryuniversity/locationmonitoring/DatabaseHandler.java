package coventryuniversity.locationmonitoring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPAPSA on 25-05-2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String dbName = "UserDB";
    private static final String tableName = "UserDetails";
    private static final int dbVersion = 1;

    public DatabaseHandler(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+tableName+"(userId INTEGER PRIMARY KEY," +
                "name VARCHAR,phoneNumber VARCHAR, allowed INTEGER);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public void insertUserDetails(String name, String phoneNumber, int allowed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phoneNumber", phoneNumber);
        values.put("allowed", allowed);
        db.insert(tableName, null, values);
        db.close();
    }

    public UserDetails getUserDetails(String name){
        UserDetails userDetails=new UserDetails();
        String selectQuery="SELECT name, phoneNumber, allowed FROM UserDetails " +
                "WHERE name='"+name+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            userDetails.Name=c.getString(0);
            userDetails.PhoneNumber=c.getString(1);
            userDetails.isEligible=c.getString(2).equals("1");
        }
        c.close();
        db.close();
        return userDetails;

    }

    public List<String> getUserNameList(){
        List<String> list = new ArrayList<String>();
        String selectQuery="SELECT name FROM UserDetails";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                list.add(c.getString(0));
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return list;

    }

    public List<UserDetails> getUserDetailsList(){
        List<UserDetails> list = new ArrayList<UserDetails>();
        String selectQuery="SELECT name, phoneNumber, allowed FROM UserDetails";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                UserDetails userDetails=new UserDetails();
                userDetails.Name=c.getString(0);
                userDetails.PhoneNumber=c.getString(1);
                userDetails.isEligible=c.getString(2).equals("1");
                list.add(userDetails);
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return list;

    }

    public void updateUserPermission(String name,String phoneNumber, int allowed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("allowed", allowed);
        int val=db.update(tableName, values, "name='"+name+"'", null);

        db.close();
    }


}
