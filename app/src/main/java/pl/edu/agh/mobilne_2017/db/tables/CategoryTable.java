package pl.edu.agh.mobilne_2017.db.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryTable {

    public static final String KEY_NAME = "name";

    private static final String LOG_TAG = "CategoryTable ";
    public static final String SQLITE_TABLE = "Category";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_NAME + " text PRIMARY KEY" +
                    " );";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }
}
