package cz.cvut.fit.vorobvla.semestralka;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;

/**
 * Created by vova on 4/5/15.
 */
public abstract class FRArrayAdapter<T> extends ArrayAdapter<T> {

    protected class ViewHolder {
        public TextView title;
        public TextView text;

    }

    public abstract void setHolder(ViewHolder holder, T obj);

    protected T[] data;
    protected Context context;

    public FRArrayAdapter(Context context, T[] data) {
        super(context, R.layout.list_item, data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item, null, true);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setHolder(holder, data[position]);
        return convertView;
    }
}
