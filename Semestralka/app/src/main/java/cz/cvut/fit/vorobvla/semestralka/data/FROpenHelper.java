package cz.cvut.fit.vorobvla.semestralka.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;

/**
 * Created by vova on 4/5/15.
 */
public class FROpenHelper extends SQLiteOpenHelper {


    public FROpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("ON CREATE HELPER", " ------------------");

        db.execSQL("CREATE TABLE " + ARTICLE_TABLE + "("
                + ATTR_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATTR_ARTICLE_TITLE + " TEXT, "
                + ATTR_ARTICLE_TEXT + " TEXT, "
                + ATTR_ARTICLE_AUTHOR + " TEXT, "
                + ATTR_ARTICLE_DATE + " DATE, "
                + ATTR_ARTICLE_POSTED_BY + " TEXT, "
                + ATTR_ARTICLE_URL + " TEXT"
                + ");");
        db.execSQL("CREATE TABLE " + FEED_TABLE + " ("
                + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATTR_FEED_NAME + " TEXT, "
                + ATTR_FEED_URL + " TEXT"
                + ");");
        Log.w("DB", db.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FEED_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_TABLE);
        onCreate(db);
    }
}
