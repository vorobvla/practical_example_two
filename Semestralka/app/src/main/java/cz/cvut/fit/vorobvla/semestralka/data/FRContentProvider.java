package cz.cvut.fit.vorobvla.semestralka.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;

/**
 * Created by vova on 4/7/15.
 */
public class FRContentProvider extends ContentProvider {

    private FROpenHelper openHelper;

    public static final String AUTHORITY = "cz.cvut.fit.vorobvla.fr";
    private static final String ARTICLES_PATH = "articles";
    private static final String FEEDS_PATH = "feeds";
    public static final Uri URI_ARTICLES = Uri.parse("content://" + AUTHORITY + "/" + ARTICLES_PATH);
    public static final Uri URI_FEEDS = Uri.parse("content://" + AUTHORITY + "/" + FEEDS_PATH);
    private static final int CODE_ARTICLE_LIST = 0;
    private static final int CODE_ARTICLE_ITEM = 1;
    private static final int CODE_FEED_LIST = 2;
    private static final int CODE_FEED_ITEM = 3;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, ARTICLES_PATH, CODE_ARTICLE_LIST);
        matcher.addURI(AUTHORITY, ARTICLES_PATH + "/#", CODE_ARTICLE_ITEM);
        matcher.addURI(AUTHORITY, FEEDS_PATH, CODE_FEED_LIST);
        matcher.addURI(AUTHORITY, FEEDS_PATH + "/#", CODE_FEED_ITEM);
    }


    private class IllegalUriException extends IllegalArgumentException{
        IllegalUriException(Uri uri){
            super("Illegal URI: \'" + uri + "\'");
        }
    };

    @Override
    public boolean onCreate() {
        openHelper = new FROpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (matcher.match(uri)){
            case CODE_ARTICLE_LIST:
                queryBuilder.setTables(ARTICLE_TABLE);
                break;
            case CODE_ARTICLE_ITEM:
                queryBuilder.setTables(ARTICLE_TABLE);
                queryBuilder.appendWhere(ATTR_ID + "=" + uri.getLastPathSegment());
                break;
            case CODE_FEED_LIST:
                queryBuilder.setTables(FEED_TABLE);
                break;
            case CODE_FEED_ITEM:
                queryBuilder.setTables(FEED_TABLE);
                queryBuilder.appendWhere(ATTR_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalUriException(uri);
        }

        Cursor cursor = queryBuilder.query(openHelper.getWritableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.w("PROVIDER:", "GOT " + uri);
        Uri idUri;
        switch (matcher.match(uri)){
            case CODE_ARTICLE_LIST:
                idUri = uri.parse(ARTICLES_PATH + "/" +
                        openHelper.getWritableDatabase().insert(ARTICLE_TABLE, null, values));
                break;
            case CODE_FEED_LIST:
                idUri = uri.parse(FEEDS_PATH + "/" +
                    openHelper.getWritableDatabase().insert(FEED_TABLE, null, values));
                break;
            default:
                throw new IllegalUriException(uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return idUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted = 0;
        switch (matcher.match(uri)){
            case CODE_ARTICLE_LIST:
                deleted = openHelper.getWritableDatabase()
                        .delete(ARTICLE_TABLE, selection, selectionArgs);
                break;
            case CODE_ARTICLE_ITEM:
                deleted = openHelper.getWritableDatabase()
                        .delete(ARTICLE_TABLE, ATTR_ID +
                                "=" + uri.getLastPathSegment(), null);
                break;
            case CODE_FEED_LIST:
                deleted = openHelper.getWritableDatabase()
                        .delete(FEED_TABLE, selection, selectionArgs);
                break;
            case CODE_FEED_ITEM:
                deleted = openHelper.getWritableDatabase()
                        .delete(FEED_TABLE, ATTR_ID +
                                "=" + uri.getLastPathSegment(), null);
                break;
            default:
                throw new IllegalUriException(uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    // maybe not correct
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated =  delete(uri, selection, selectionArgs);
        insert(uri,values);
        return updated;
    }
}
