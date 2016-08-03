package cz.cvut.fit.vorobvla.semestralka.refresh;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

import java.util.List;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;
import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Feed;

import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_AUTHOR;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_DATE;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_POSTED_BY;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_TEXT;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_TITLE;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_ARTICLE_URL;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.ATTR_FEED_URL;
import static cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider.URI_ARTICLES;
import static cz.cvut.fit.vorobvla.semestralka.data.FRContentProvider.URI_FEEDS;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateFeedsIntentService extends IntentService {

    public static final String REFRESH_PROGRESS = "refresh_progress";
    public static final String REFRESH_PROGRESS_EXTRA_REFRESH_RUNNING = "refresh running";

    public UpdateFeedsIntentService() {
        super("UpdateFeedsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //broadcast refresh started
            Intent outputIntent = new Intent(REFRESH_PROGRESS);
            outputIntent.putExtra(REFRESH_PROGRESS_EXTRA_REFRESH_RUNNING, true);
            sendBroadcast(outputIntent);

            //purge articles
            getContentResolver().delete(URI_ARTICLES, null, null);
            //refresh feeds
            Cursor cursor =
                    getContentResolver().query(URI_FEEDS, new String[]{ATTR_FEED_URL}, null, null, null);
            int col = cursor.getColumnIndex(ATTR_FEED_URL);

            while (cursor.moveToNext()){
                List<SyndEntry> entries = Feed.getEntriesFromUrl(cursor.getString(col));
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
                        getContentResolver().insert(URI_ARTICLES, values);
                    }
                }
            }
            //finished to refresh. can sleep
            WakeLockHelper.release();

            //broadcast refresh finished
            outputIntent.putExtra(REFRESH_PROGRESS_EXTRA_REFRESH_RUNNING, false);
            sendBroadcast(outputIntent);
        }
    }

}
