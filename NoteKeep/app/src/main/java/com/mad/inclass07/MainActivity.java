package com.mad.inclass07;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseDataManager dbManager = new DatabaseDataManager(MainActivity.this);
        notesList = new ArrayList<Note>(dbManager.getAllNotes());

        setNoteDetails(notesList);
        Spinner spinner = (Spinner) findViewById(R.id.spinner3);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Button add = (Button) findViewById(R.id.AddBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText3);
                String name = editText.getText().toString();
                Spinner spinner = (Spinner)findViewById(R.id.spinner3);
                String priority = spinner.getSelectedItem().toString();
                Log.d("demo",priority);
                if(name!=null && !name.isEmpty() && priority!=null){
                    if(priority.equalsIgnoreCase("Priority")){
                        priority = "Low";
                    }
                    Note note = new Note();
                    note.setSubject(name);
                    note.setPriority(priority);
                    note.setTime(new Date().toString());
                    note.setStatus("Pending");
                    DatabaseDataManager dbManager = new DatabaseDataManager(MainActivity.this);
                    dbManager.saveNote(note);
                    notesList = new ArrayList<Note>(dbManager.getAllNotes());
                    setNoteDetails(notesList);
                }
            }
        });


    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DatabaseDataManager dbManager = new DatabaseDataManager(MainActivity.this);
                switch (item.getItemId()) {
                    case R.id.showAll:
                        notesList = new ArrayList<Note>(dbManager.getAllNotes());
                        setNoteDetails(notesList);
                        return true;
                    case R.id.showCompleted:
                        notesList = new ArrayList<Note>(dbManager.getAllCompleted());
                        setNoteDetails(notesList);
                        return true;
                    case R.id.showPending:
                        notesList = new ArrayList<Note>(dbManager.getAllPending());
                        setNoteDetails(notesList);
                        return true;
                    case R.id.sortPr:
                        notesList = new ArrayList<Note>(dbManager.getAllNotes());
                        ArrayList<Note> sortList = sortUserAsc(notesList);
                        setNoteDetails(sortList);
                        return true;
                    case R.id.sorttime:
                        notesList = new ArrayList<Note>(dbManager.getAllNotes());
                        ArrayList<Note> sortListtime = order(notesList);
                        setNoteDetails(sortListtime);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.show();
    }


    public void setNoteDetails(final ArrayList<Note> details) {
        int count=0;
        for(Note note: details) {
        }
        final ListView appList = (ListView)findViewById(R.id.appList);

        ArrayListAdapter appListAdapter = new ArrayListAdapter(MainActivity.this, R.layout.activity_item, details);
        appList.setAdapter(appListAdapter);
        appListAdapter.setNotifyOnChange(true);
        appListAdapter.notifyDataSetChanged();
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final View thisView = view;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add to Favourites")
                        .setMessage("Are you sure that you want to add this app to favourites?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    public ArrayList<Note> sortUserAsc(ArrayList<Note> appsList) {
        ArrayList<Note> sortedList = new ArrayList<Note>(appsList);
        Collections.sort(sortedList);
        return sortedList;
    }

    public ArrayList<Note> sortUserDesc(ArrayList<Note> appsList) {
        ArrayList<Note> sortedList = new ArrayList<Note>(appsList);
        Collections.sort(sortedList, Collections.<Note>reverseOrder());
        return sortedList;
    }

    public ArrayList<Note> order(ArrayList<Note> notes) {

        Collections.sort(notes, new Comparator() {

            public int compare(Object o1, Object o2) {

                String x1 = ((Note) o1).getTime();
                String x2 = ((Note) o2).getTime();
                int sComp = Ptime.PtimeFormatter.getPTimeMillis(x1).compareTo(Ptime.PtimeFormatter.getPTimeMillis(x2));
                return sComp;

            }});
        return notes;
    }

}
