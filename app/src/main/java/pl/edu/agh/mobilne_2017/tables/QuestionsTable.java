package pl.edu.agh.mobilne_2017.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class QuestionsTable {
    public static final String KEY_ROWID = "_id";
    public static final String CATEGORY = "category";
    public static final String CONTENT = "content";
    public static final String TYPE = "type";

    private static final String LOG_TAG = " QuestionsTable";
    public static final String SQLITE_TABLE = "Questions";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                    CATEGORY +" text, " +
                    CONTENT + " text, " +
                    TYPE + " text "+
                    " );";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }
}
