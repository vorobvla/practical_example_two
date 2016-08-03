package cz.cvut.fit.vorobvla.semestralka;


import android.app.AlarmManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Date;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;

import static cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider.*;

import cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider;
import cz.cvut.fit.vorobvla.semestralka.data.FROpenHelper;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;
import cz.cvut.fit.vorobvla.semestralka.refresh.UpdateFeedsIntentService;
import cz.cvut.fit.vorobvla.semestralka.refresh.WakeToRefreshFeedsReciever;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final int ARTICLE_LOADER = 0;
    public final int FEED_LOADER = 1;

    private View rootView;
    private static String SCROLL_POS = "scroll pos";
    private SimpleCursorAdapter adapter;
    private ProgressDialog progressDialog = null;


    public ArticlesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.w("LIST FRAG", "ON CREATE");


        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        if (savedInstanceState != null){
            rootView.setScrollY(savedInstanceState.getInt(SCROLL_POS));
        }
        SQLiteDatabase db = new FROpenHelper(getActivity()).getWritableDatabase();

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item,
                db.query(ARTICLE_TABLE, null,null,null,null,null,null),
                new String[]{ATTR_ARTICLE_TITLE, ATTR_ARTICLE_TEXT},
                new int[]{R.id.title, R.id.text},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
        getLoaderManager().initLoader(FEED_LOADER, null, this);
        setListAdapter(adapter);

        //setup reciever for UI progress bar to show feed updating
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getResources().getString(R.string.refreshingFeeds));
        progressDialog.setIndeterminate(true);
        getActivity().registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        boolean refreshRunning = intent.getBooleanExtra(
                                UpdateFeedsIntentService.REFRESH_PROGRESS_EXTRA_REFRESH_RUNNING,
                                false);
                        if (refreshRunning){
                            //progressDialog.setProgress(3000);
                            progressDialog.show();
                        } else {
                            if (progressDialog != null){
                                progressDialog.cancel();
                            }
                        }
                    }
                },
                new IntentFilter(UpdateFeedsIntentService.REFRESH_PROGRESS)
        );

        //setup alarm for feed refreshing
        ((AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE))
                .setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        //TODO: set interval to 2 hours
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES / 30,
                        PendingIntent.getBroadcast(
                                getActivity(), 0,
                                new Intent(this.getActivity(), WakeToRefreshFeedsReciever.class),
                                PendingIntent.FLAG_CANCEL_CURRENT
                        )
                );

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArticlesListActivity.noArticle = false;
        Cursor cursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(URI_ARTICLES,
                        String.valueOf(getListAdapter().getItemId(position))),
                new String[]{ATTR_ARTICLE_TITLE, ATTR_ARTICLE_TEXT, ATTR_ARTICLE_POSTED_BY,
                        ATTR_ARTICLE_URL, ATTR_ARTICLE_DATE, ATTR_ARTICLE_AUTHOR},
                null, null, null);
        cursor.moveToFirst();
        Log.w("----->", cursor.getColumnNames().length + "");
        Article article = new Article(
                cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_TITLE)),
                cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_TEXT)),
                new Date(cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_DATE))),
                cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_POSTED_BY)),
                cursor.getString(cursor.getColumnIndex(ATTR_ARTICLE_URL))
        );

        if (!(ArticlesListActivity.tabletUI)){
            // not tablet UI
            DetailsActivity.start(getActivity(), article, DetailsActivity.class);
        } else {
            // tablet UI
            DetailsActivity.start(getActivity(), article, getActivity().getClass());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POS, rootView.getScrollY());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ARTICLE_LOADER:
                return new CursorLoader(getActivity(), URI_ARTICLES,
                        new String[]{ATTR_ID, ATTR_ARTICLE_TITLE, ATTR_ARTICLE_TEXT},
                        null, null, null);
            case FEED_LOADER:
                return new CursorLoader(getActivity(), FRContentProvider.URI_FEEDS,
                        new String[]{ATTR_ID, ATTR_FEED_NAME, ATTR_FEED_URL}, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case ARTICLE_LOADER:
                adapter.swapCursor(data);
                break;
            case FEED_LOADER:
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case ARTICLE_LOADER:
                adapter.swapCursor(null);
                break;
            case FEED_LOADER:
                break;
            default:
                break;
        }
    }

    public void refresh(){
        Intent refreshIntent = new Intent(this.getActivity(), UpdateFeedsIntentService.class);
        getActivity().startService(refreshIntent);
        /*
        purge();
        Cursor cursor =
            getActivity().getContentResolver().query(URI_FEEDS, new String[]{ATTR_FEED_URL}, null, null, null);
        int col = cursor.getColumnIndex(ATTR_FEED_URL);
        while (cursor.moveToNext()){
            List<SyndEntry> entries = Feed.getEntriesFromUrl(
                cursor.getString(col)
            );
            for (SyndEntry ent : entries){
                ContentValues values = new ContentValues();
                Article article = Feed.getArticleFromEntry(ent);
                if (article != null) {
                    values.put(ATTR_ARTICLE_TITLE, article.getTitle());
                    values.put(ATTR_ARTICLE_URL, article.getLink());
                    values.put(ATTR_ARTICLE_DATE, String.valueOf(article.getDate()));
                    values.put(ATTR_ARTICLE_POSTED_BY, article.getPostedBy());
                    values.put(ATTR_ARTICLE_AUTHOR, article.getAuthor());
                    values.put(ATTR_ARTICLE_TEXT, article.getText());
                    getActivity().getContentResolver().insert(URI_ARTICLES, values);
                }
            }
        }
        */
    }

    public void purge(){
        getActivity().getContentResolver().delete(URI_ARTICLES, null, null);
    }
}
