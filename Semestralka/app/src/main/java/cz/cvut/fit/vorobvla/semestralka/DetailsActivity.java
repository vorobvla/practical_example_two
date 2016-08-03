package cz.cvut.fit.vorobvla.semestralka;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;

import java.text.SimpleDateFormat;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;


public class DetailsActivity extends ActionBarActivity  {

    //protected Intent intent;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_AUTHOR = "author";
    public static final String EXTRA_POSTED_BY = "postedBy";
    public static final String EXTRA_TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                   // .add(R.id.container, new PlaceholderFragment())
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so ng
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_item) {
            ((DetailsFragment)getFragmentManager().findFragmentById(R.id.container)).shareArticle();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public static void start(Activity parent, Article article, Class toStart){
        Intent intent = new Intent(parent, toStart);
        intent.putExtra(EXTRA_TITLE, article.getTitle());
        intent.putExtra(EXTRA_DATE, article.getDate());
        intent.putExtra(EXTRA_AUTHOR, article.getAuthor());
        intent.putExtra(EXTRA_POSTED_BY, article.getPostedBy());
        intent.putExtra(EXTRA_TEXT, article.getText());
        parent.startActivity(intent);
    }



/*
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }*/

 /*   protected void shareArtcle(){
        if (ListActivity.noArticle){
            return;
        }
        Intent articleIntent = getIntent();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, articleIntent.getExtras().getString(EXTRA_TITLE)
        + getResources().getString(R.string.by) + articleIntent.getExtras().getString(EXTRA_AUTHOR));
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, articleIntent.getExtras().getString(EXTRA_TITLE)
                + getResources().getString(R.string.by) + articleIntent.getExtras().getString(EXTRA_AUTHOR));
        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                articleIntent.getExtras().getString(EXTRA_TITLE) + "\n"
                + new SimpleDateFormat(getResources().getString(R.string.details_date_fmt))
                .format(articleIntent.getExtras().getSerializable(EXTRA_DATE))
                + articleIntent.getExtras().getString(EXTRA_AUTHOR) + "\n"
                + getResources().getString(R.string.details_posted_by)
                + articleIntent.getExtras().getString(EXTRA_POSTED_BY) + "\n"
                + articleIntent.getExtras().getString(EXTRA_TEXT)
        );
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.details_share_with)));
    }
*/
}
