package pl.edu.agh.mobilne_2017.tables;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CheckboxAnswersTable {
    public static final String KEY_ROWID = "_id";
    public static final String QUESTION_ID = "question_id";
    public static final String CONTENT = "content";
    public static final String CORRECT = "correct";

    private static final String LOG_TAG = "CheckboxAnswersTable";
    public static final String SQLITE_TABLE = "CheckboxAnswers";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                    CONTENT + " text, " +
                    QUESTION_ID + " integer, " +
                    CORRECT + "boolean " +
                    " );";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }
}
