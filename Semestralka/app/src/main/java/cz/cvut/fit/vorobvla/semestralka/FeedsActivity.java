package cz.cvut.fit.vorobvla.semestralka;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FetcherException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Feed;
import cz.cvut.fit.vorobvla.semestralka.data.FROpenHelper;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;

public class FeedsActivity extends ActionBarActivity {

    private static String LIST_FRAG_TAG = "list frag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        getFragmentManager().beginTransaction()
                .add(R.id.container, new FeedsFragment(), LIST_FRAG_TAG)
        //        .add(R.id.container, new AddFeedFragment(), ADD_FRAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_feed) {
            new DialogFragment(){
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final View rootView = getLayoutInflater().inflate(R.layout.add_feed_dialog, null);
                    builder.setView(rootView);
                    builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                new AsyncTask<String, Boolean, Boolean>(){
                                    @Override
                                    protected Boolean doInBackground(String... params) {
                                        try {
                                            ((FeedsFragment)getActivity().getFragmentManager()
                                                    .findFragmentByTag(LIST_FRAG_TAG))
                                                    .addFeed(params[0]);
                                            return true;
                                        } catch (Exception ex){
                                            Log.w("ADD FEED EX:", ex.toString());
                                            return false;
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {
                                        if (!aBoolean){
                                            Toast.makeText(getActivity(),
                                                    getString(R.string.addFeedFailureWithUrl),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute(new String[]{
                                        ((EditText)rootView.findViewById(R.id.urlFeed))
                                                .getText().toString()}).get(1000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    return builder.create();
                }
            }.show(getFragmentManager(), getResources().getString(R.string.addFeed));

            return true;
        }

     /*   if(id == R.id.remove_feed){

        }*/

        return super.onOptionsItemSelected(item);
    }
}
