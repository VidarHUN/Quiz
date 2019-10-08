package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quiz.QuizContract.*;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Tényleges SQLiite adatbázis létrehozása
 */
public class QuizDbHelper extends SQLiteOpenHelper {
    /**
     * Adatbázis neve és verziója.
     * Kellenek, hogy a konstruktor superje is jól tudjon működni.
     */
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Adatbázis változója
     */
    private SQLiteDatabase db;
    private Context context;

    /**
     * Konstruktor
     *
     * @param context
     */
    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Itt férünk hozzá elsőnek az adatbázishoz
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        /**
         * Tábla megalkotására szánt SQL parancs
         */
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        /**
         * SQL kód futtatása
         */
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        /**
         * Tábla feltöltése
         */
        fillQuestionTable();
    }

    /**
     * Ha a későbbiekben változtatni kell a táblán, valamiért, akkor ezt kell meghívni.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Tábla törlése
         */
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        /**
         * Tábla újramegalkotása
         */
        onCreate(db);
    }

    /**
     * Tábla feltöltése adatokkal
     */
    private void fillQuestionTable() {
        String[] arr;
        /**
         * Adatbeolvasás és megfelelően szeparálva feltölteni kérdésekkel.
         */
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("questions.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                arr = mLine.split("\t");
                Question q = new Question(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]));
                addQuestion(q);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorok tényleges beszúrására használatos függvény.
     * @param question
     */
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    /**
     * Adatok kinyerése
     * @return
     */
    public ArrayList<Question> getAllquestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr((c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR))));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
