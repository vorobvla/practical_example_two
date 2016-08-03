package cz.cvut.fit.vorobvla.semestralka;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Feed;
import cz.cvut.fit.vorobvla.semestralka.data.FROpenHelper;
import static cz.cvut.fit.vorobvla.semestralka.data.Constants.*;

/**
 * Created by vova on 4/5/15.
 */
public class AddFeedFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = getActivity().getLayoutInflater().inflate(R.layout.add_feed_dialog, null);
        builder.setView(rootView);
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AsyncTask<String, Boolean, Feed>(){
                    @Override
                    protected Feed doInBackground(String... params) {
                        try{
                            return new Feed(params[0]);
                        } catch (Exception ex){
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Feed feed) {
                        if (feed != null) {
                            //Feed.addFeed(feed);
                            ContentValues insertFeed = new ContentValues();
                            insertFeed.put(ATTR_FEED_NAME, feed.getName());
                            insertFeed.put(ATTR_FEED_URL, feed.getUrl());
                            SQLiteDatabase db = new FROpenHelper(getActivity()).getWritableDatabase();
                            db.insert(FEED_TABLE, null, insertFeed);
                            db.close();
                        } else {
                            Toast.makeText(getActivity(), getResources()
                                    .getString(R.string.addFeedFailureWithUrl), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(((EditText)rootView.findViewById(R.id.urlFeed)).getText().toString());
                dialog.dismiss();
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
}
