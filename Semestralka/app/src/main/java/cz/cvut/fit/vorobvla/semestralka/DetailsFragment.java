package cz.cvut.fit.vorobvla.semestralka;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;



/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private View rootView;
    private static String SCROLL_POS = "scroll pos";


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        if (savedInstanceState != null){
            rootView.setScrollY(savedInstanceState.getInt(SCROLL_POS));
        }
        if (ArticlesListActivity.noArticle){
            return rootView;
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(intent.getStringExtra(DetailsActivity.EXTRA_TITLE));
        ((TextView) rootView.findViewById(R.id.dateNauthor)).setText(
                new SimpleDateFormat(getResources().getString(R.string.details_date_fmt))
                        .format(intent.getSerializableExtra(DetailsActivity.EXTRA_DATE))
                        + getResources().getString(R.string.by)
                        + intent.getStringExtra(DetailsActivity.EXTRA_AUTHOR));
        ((TextView) rootView.findViewById(R.id.postedBy)).setText(getResources().getString(R.string.details_posted_by)
                + intent.getStringExtra(DetailsActivity.EXTRA_POSTED_BY));
        ((TextView) rootView.findViewById(R.id.text)).setText(intent.getStringExtra(DetailsActivity.EXTRA_TEXT));
        return rootView;
    }

    protected void shareArticle(){
        if (ArticlesListActivity.noArticle){
            return;
        }
        Intent articleIntent = getActivity().getIntent();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, articleIntent.getExtras().getString(DetailsActivity.EXTRA_TITLE)
                + getResources().getString(R.string.by) + articleIntent.getExtras().getString(DetailsActivity.EXTRA_AUTHOR));
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, articleIntent.getExtras().getString(DetailsActivity.EXTRA_TITLE)
                + getResources().getString(R.string.by) + articleIntent.getExtras().getString(DetailsActivity.EXTRA_AUTHOR));
        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                articleIntent.getExtras().getString(DetailsActivity.EXTRA_TITLE) + "\n"
                        + new SimpleDateFormat(getResources().getString(R.string.details_date_fmt))
                        .format(articleIntent.getExtras().getSerializable(DetailsActivity.EXTRA_DATE))
                        + getResources().getString(R.string.by)
                        + articleIntent.getExtras().getString(DetailsActivity.EXTRA_AUTHOR) + "\n"
                        + getResources().getString(R.string.details_posted_by)
                        + articleIntent.getExtras().getString(DetailsActivity.EXTRA_POSTED_BY) + "\n"
                        + articleIntent.getExtras().getString(DetailsActivity.EXTRA_TEXT)
        );
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.details_share_with)));
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POS, rootView.getScrollY());
    }
}
