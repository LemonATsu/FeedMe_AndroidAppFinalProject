package prototype.feedme.cat.prototype.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AT on 2014/12/28.
 */
public class DBHelper extends SQLiteOpenHelper{
    public final static int  DB_VERSION = 2;
    public final static String DB_NAME = "feedme.db";
    public final static String TABLE_NAME = "FeedMe_Log";
    public final static String COL_SCOR_VEG = "VEG";
    public final static String COL_SCOR_PROTEIN = "PROTEIN";
    public final static String COL_SCOR_STARCH = "STARCH";
    public final static String COL_SCOR_OIL = "OIL";
    public final static String COL_SCOR_DAI = "DAIRY";
    public final static String COL_DATE = "DATE";
    public final static String COL_SCOR_TOT = "SCORE";

    public final static String COL_FOOD_NAME = "FOOD_NAME_STRING";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       /* final String SQL = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "( " +
                "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " VARCHAR(30), " +
                COL_SCOR_VEG + " REAL, " +
                COL_SCOR_PROTEIN + " REAL, " +
                COL_SCOR_STARCH + " REAL, " +
                COL_SCOR_OIL + " REAL, " +
                COL_SCOR_DAI + " REAL " +
                ")";*/
        final String SQL = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "( " +
                "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " VARCHAR(30), " +
                COL_FOOD_NAME + " VARCHAR(120), " +
                COL_SCOR_TOT + " REAL " +
                ")";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //刪除舊有的資料表
        onCreate(db);
    }


}
