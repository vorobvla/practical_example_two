package cz.cvut.fit.vorobvla.semestralka;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Feed;
import cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider;
import cz.cvut.fit.vorobvla.semestralka.data.FROpenHelper;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;
import static cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final int FEED_LOADER = 0;
    private CursorAdapter adapter;



    public FeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feeds, container, false);
        SQLiteDatabase db = new FROpenHelper(getActivity()).getWritableDatabase();

  //      Log.w("------>", db.query(FROpenHelper.FEED_TABLE,null,null,null,null,null,null).toString());
//        Log.w("------>", db.query(FROpenHelper.FEED_TABLE,new String[]{FROpenHelper.KEY_ID},null,null,null,null,null).toString());

        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item,
                db.query(FEED_TABLE,null,null,null,null,null,null),
                new String[]{ATTR_FEED_NAME, ATTR_FEED_URL},
                new int[]{R.id.title, R.id.text}, 0);
               // CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        getLoaderManager().initLoader(FEED_LOADER, null, this);
        setListAdapter(adapter);
        return rootView;
    }

    public void addFeed(final String feedUrl){
        Feed feed = new Feed(feedUrl);
        ContentValues values = new ContentValues();
        //add feed to set
        values.put(ATTR_FEED_NAME, feed.getName());
        values.put(ATTR_FEED_URL, feedUrl);
        getActivity().getContentResolver().insert(URI_FEEDS, values);
    }


    public void removeSelectedFeeds(){
        //TODO
    }

    public void removeFeed(long id){
        getActivity().getContentResolver()
                .delete(Uri.withAppendedPath(URI_FEEDS, String.valueOf(id)), null, null);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
               case FEED_LOADER :
                   return new CursorLoader(getActivity(), FRContentProvider.URI_FEEDS,
                           new String[]{ATTR_ID, ATTR_FEED_NAME, ATTR_FEED_URL}, null, null, null);
               default:
                   return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case FEED_LOADER:
                adapter.swapCursor(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case FEED_LOADER:
                adapter.swapCursor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        removeFeed(getListAdapter().getItemId(position));
    }
}
