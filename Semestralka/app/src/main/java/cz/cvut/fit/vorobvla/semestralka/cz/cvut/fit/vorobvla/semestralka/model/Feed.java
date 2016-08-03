package cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.text.Html;
import android.util.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntryImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * Created by vova on 4/4/15.
 */
public class Feed {
    private String name;
    private String url;
    private SyndFeed feed;
/*    private static HashSet<Feed> all;

    static {
        all = new HashSet<>();
    }*/

    public Feed(String url){
        this.url = url;
    //    this.name = url;
     //   Log.w("FEED", "asd");
        this.feed = retrieveFeed(url);
        this.name = feed.getTitle();
     //   print();
      //  Log.w("FEED", feed.toString());
       // addFeed(this);
    }

  /*  public SyndFeed getMostRecentNews( final String feedUrl )
    {
        try
        {
            return retrieveFeed( feedUrl );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }*/

/*    public void print() throws FetcherException, FeedException, IOException{
       for (Object o : retrieveFeed(url).getEntries()){
       }
    }*/

    public static List getEntriesFromUrl(String url){
        return retrieveFeed(url).getEntries();
    }

 /*   public static ContentValues entryToContentValues(SyndEntry entry){
        if (entry.getTitle() == null){
            return null;
        }
        ContentValues values = new ContentValues();
        values.put();
    }*/

    public List getUpdatedEntries() {
        return retrieveFeed(url).getEntries();
    }

    public static Article getArticleFromEntry(SyndEntry entry){
        if (entry.getTitle() == null){
            return null;
        }if (entry.getPublishedDate() == null){
            return null;
        }
        String content;
        if (entry.getContents() == null){
            content = "";
        } else {
            content = "";
            for (Object sc : entry.getContents()){
                content += Html.fromHtml(((SyndContent) sc).getValue()).toString();
            }
        }
        String contributors = "";
        for (Object o : entry.getContributors()){
            contributors += o.toString();
        }
        return new Article(entry.getTitle(),
                content,
                entry.getPublishedDate(),
                entry.getAuthor(),
                contributors,// todo: contributors?
                entry.getLink());
    }


    //a bit modified copy-paste from rome documentation
    private static SyndFeed retrieveFeed(final String feedUrl)
    {
        FeedFetcher feedFetcher = new HttpURLFeedFetcher();
        try {
            return feedFetcher.retrieveFeed(new URL(feedUrl));
        } catch (Exception e) {
            Log.w("RUNTIME EXCEPTION: ", e.toString());
            throw new RuntimeException();
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }


/*    public static void addFeed(Feed feed) throws RuntimeException{
        if (!all.add(feed)){
            throw new RuntimeException("Failure while adding feed");
        };
    }

    public static void removeFeed(Feed feed)throws RuntimeException{
        if (!all.remove(feed)){
            throw new RuntimeException("Failure while removing feed");
        };
    }*/

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Feed)) {
            return false;
        };
        if (((Feed)o).getUrl().equals(url)){
            return true;
        }
        return false;
    }

    public static Feed[] getAllArray(){
        return null;
//        return Arrays.copyOf(all.toArray(), all.size(), Feed[].class);
    }
}
