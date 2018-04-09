package com.mad.group24_hw04;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by swati on 2/11/2017.
 */

public class QuestionAdapter extends ArrayAdapter<Question>{
    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        super(context, 0, questions);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Question question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item, parent, false);
        }
        // Lookup view for data population
        TextView ques = (TextView) convertView.findViewById(R.id.textViewQues);
        TextView yAns = (TextView) convertView.findViewById(R.id.textViewYrAns);
        TextView corAns = (TextView) convertView.findViewById(R.id.textViewCorAns);
        // Populate the data into the template view using the data object
        ques.setText(question.getText());
        corAns.setText(question.getChoice().get(Integer.parseInt(question.getAnswer())-1));

        if(question!=null && question.getSelectedAns()!=null && !question.getSelectedAns().equalsIgnoreCase("0")) {
            yAns.setText(question.getSelectedAns());
        }
        else{
            yAns.setText("No answer selected");
        }
        yAns.setBackgroundResource((R.color.red));

        // Return the completed view to render on screen
        return convertView;
    }

}
