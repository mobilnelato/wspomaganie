package pl.edu.agh.mobilne_2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.mobilne_2017.tables.CategoryTable;
import pl.edu.agh.mobilne_2017.tables.CheckboxAnswersTable;
import pl.edu.agh.mobilne_2017.tables.QuestionsTable;
import pl.edu.agh.mobilne_2017.tables.StringAnswersTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MobilneDb";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CategoryTable.onCreate(db);
        CheckboxAnswersTable.onCreate(db);
        StringAnswersTable.onCreate(db);
        QuestionsTable.onCreate(db);
    }

    //musi byc zadeklarowana, pomijam implementacje bo jest nieistotna
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing
    }


    public void createCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.KEY_NAME, name);
        Log.w("DatabaseHelper", "Creating category");
        db.insert(CategoryTable.SQLITE_TABLE, null, values);
    }


    public List<String> getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + CategoryTable.SQLITE_TABLE;
        Log.w("DatabaseHelper", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        List<String> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String cat = c.getString(c.getColumnIndex(CategoryTable.KEY_NAME));
                result.add(cat);
            } while (c.moveToNext());
        }
        return result;
    }

    //tak trywialne ze todo jutro
    //todo
    public void updateQuestion(Question question) {
        if (question.getType() == QuestionType.CLOSED) {

        } else if (question.getType() == QuestionType.OPEN) {

        }
    }


    public List<Question> getAllQuestions(String category) {
        List<Question> result = new ArrayList<>();


        return result;
    }


    public Question getQuestion(int id) {
        return null;
    }
}




