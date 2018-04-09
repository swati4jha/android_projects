package swati.example.com.homework09_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by swati on 4/21/2017.
 */

public class ListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public ListAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.item_people, values);
            this.context = context;
            this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.item_people, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textViewPeople);
            textView.setText(values.get(position));
        return rowView;
    }
}
