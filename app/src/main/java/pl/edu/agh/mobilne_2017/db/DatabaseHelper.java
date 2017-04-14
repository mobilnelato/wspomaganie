package pl.edu.agh.mobilne_2017.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.mobilne_2017.db.tables.CategoryTable;
import pl.edu.agh.mobilne_2017.db.tables.CheckboxAnswersTable;
import pl.edu.agh.mobilne_2017.db.tables.QuestionsTable;
import pl.edu.agh.mobilne_2017.db.tables.StringAnswersTable;
import pl.edu.agh.mobilne_2017.model.ClosedQuestion;
import pl.edu.agh.mobilne_2017.model.OpenQuestion;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.model.QuestionType;

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

    int getNumberOfQuestions(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "select COUNT(*) from " + QuestionsTable.SQLITE_TABLE + " where " + QuestionsTable.CATEGORY + " like " + category;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public Map<String, Integer> getCategoriesWithQuestionNumbers() {
        List<String> categories = getAllCategories();
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            String cat = categories.get(i);
            result.put(cat, getNumberOfQuestions(cat));
        }
        return result;
    }

    public List<Question> getRandomQuestions(String category, int size) {
        List<Question> all = getAllQuestions(category);
        List<Question> result = new ArrayList<>();
        Random generator = new Random();
        while (size > 0) {
            int i = generator.nextInt(all.size());
            Question q = all.get(i);
            result.add(q);
            all.remove(q);
            size--;
        }
        return result;
    }

    private void deleteQuestion(int questionId, QuestionType type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QuestionsTable.SQLITE_TABLE, "id = ? ", new String[]{Integer.toString(questionId)});
        if (type == QuestionType.OPEN) {
            db.delete(StringAnswersTable.SQLITE_TABLE, StringAnswersTable.QUESTION_ID + " = ? ", new String[]{Integer.toString(questionId)});
        } else if (type == QuestionType.CLOSED) {
            db.delete(CheckboxAnswersTable.SQLITE_TABLE, CheckboxAnswersTable.QUESTION_ID + " = ? ", new String[]{Integer.toString(questionId)});
        }
    }

    private void createQuestion(Question q, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionsTable.CATEGORY, category);
        contentValues.put(QuestionsTable.CONTENT, q.getContent());
        contentValues.put(QuestionsTable.TYPE, q.getType().toString());
        long id = db.insert(QuestionsTable.SQLITE_TABLE, null, contentValues);

        if (q.getType() == QuestionType.OPEN) {
            OpenQuestion openQuestion = (OpenQuestion) q;
            ContentValues openAnswerValues = new ContentValues();
            openAnswerValues.put(StringAnswersTable.CONTENT, openQuestion.getStringAnswer());
            openAnswerValues.put(StringAnswersTable.QUESTION_ID, Long.toString(id));
            db.insert(StringAnswersTable.SQLITE_TABLE, null, contentValues);
        } else if (q.getType() == QuestionType.CLOSED) {
            ClosedQuestion closedQuestion = (ClosedQuestion) q;
            for (int i = 0; i < closedQuestion.getCheckboxes().length; i++) {
                ContentValues checkBoxValues = new ContentValues();
                checkBoxValues.put(CheckboxAnswersTable.CONTENT, closedQuestion.getContent());
                checkBoxValues.put(CheckboxAnswersTable.CORRECT, closedQuestion.getCheckboxes()[i]);
                checkBoxValues.put(CheckboxAnswersTable.QUESTION_ID, Long.toString(id));
                db.insert(CheckboxAnswersTable.SQLITE_TABLE, null, contentValues);
            }
        }

    }

    public void updateQuestion(Question question, String category) {
        deleteQuestion(question.getId(), question.getType());
        createQuestion(question, category);
    }


    public List<Question> getAllQuestions(String category) {
        List<Question> result = new ArrayList<>();
        //zbierzemy id a pozniej get Question

        return result;
    }


    public Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + QuestionsTable.SQLITE_TABLE + " where id = ";
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


}




