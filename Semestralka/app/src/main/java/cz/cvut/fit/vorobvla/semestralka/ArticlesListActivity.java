package cz.cvut.fit.vorobvla.semestralka;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class ArticlesListActivity extends ActionBarActivity {

    protected static Boolean tabletUI;
    protected static Boolean noArticle;
    private static String WAS_TABLET_UI = "was_tablet";

    static {
        noArticle = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.w("DB::::", "ListActivity.onCreate()");

        //test if tablet layout is needed
        //Log.w("DBG::::", "Side " + getResources().getConfiguration().smallestScreenWidthDp);
        tabletUI = ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            && (getResources().getConfiguration().smallestScreenWidthDp > 390));

        if (!tabletUI) {
            //ordinary UI
            Log.w("UI::::", "NORMAL");
            setContentView(R.layout.activity_list);
            if ((savedInstanceState == null) || (savedInstanceState.getBoolean(WAS_TABLET_UI))) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new ArticlesListFragment(), getResources().getString(R.string.LIST_FRAG_TAG))
                        .commit();
            }
        } else {
            //tablet UI
            //Log.w("UI::::", "TABLET");
            setContentView(R.layout.activity_list_tablet);
        /*    if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new ArticlesListFragment())
                      //  .add(R.id.container, new DetailsFragment())
                        .commit();
            }*/
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_list, menu);

        /*
        if (tabletUI){
            getMenuInflater().inflate(R.menu.menu_tablet, menu);
            if (noArticle) {
                menu.removeItem(R.id.share_item);
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_list, menu);
        }*/

        getMenuInflater().inflate(R.menu.menu_list, menu);
        if ((!tabletUI) || (noArticle) ){
            menu.removeItem(R.id.share_item);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.share_item :
                if (noArticle){
                    return false;
                }
                ((DetailsFragment)getFragmentManager().findFragmentById(R.id.articleDetailsFragmentTabletUI))
                        .shareArticle();
                return true;
            case R.id.configure_feeds:
                startActivity(new Intent(this, FeedsActivity.class));
                return true;
            case R.id.refresh_feeds:
            //    Log.w("ASdasd", "Asdasd");
            /*    new AsyncTask<Void, Integer, Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        ((ArticlesListFragment)getFragmentManager().findFragmentByTag(LIST_FRAG_TAG))
                                .refresh();
                        return null;
                    }
                }.execute();*/
                ((ArticlesListFragment)getFragmentManager().findFragmentByTag(getResources()
                        .getString(R.string.LIST_FRAG_TAG)))
                        .refresh();
                return true;
            case R.id.purge:
                ((ArticlesListFragment)getFragmentManager().findFragmentByTag(getResources()
                        .getString(R.string.LIST_FRAG_TAG)))
                        .purge();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_TABLET_UI, tabletUI);
    }
}
