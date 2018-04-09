package swati.example.com.inclass10;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notesList;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList = new ArrayList<Note>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteDataSnapshot.getValue(Note.class);
                    notesList.add(note);
                }
                if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                    notesList = sortUserPriority(notesList);
                    setNoteDetails(notesList,"ALL");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUser:onCancelled", databaseError.toException().toString());

            }
        });
        //DatabaseDataManager dbManager = new DatabaseDataManager(MainActivity.this);
        //notesList = new ArrayList<Note>(dbManager.getAllNotes());


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

                    note.set_id(mDatabase.child("notes").push().getKey());
                    mDatabase.child("notes").child(note.get_id()).setValue(note);
                    //DatabaseDataManager dbManager = new DatabaseDataManager(MainActivity.this);
                    //dbManager.saveNote(note);
                   // notesList = new ArrayList<Note>(dbManager.getAllNotes());
                    notesList = sortUserPriority(notesList);
                    setNoteDetails(notesList,"ALL");
                }
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){

                }
                switch (item.getItemId()) {
                    case R.id.showAll:
                        if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                            ArrayList<Note> sortList1 = sortUserPriority(notesList);
                            setNoteDetails(sortList1,"ALL");
                        }
                        return true;
                    case R.id.showCompleted:
                        if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                            setNoteDetails(notesList,"C");
                        }
                        return true;
                    case R.id.showPending:
                        if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                            setNoteDetails(notesList,"P");
                        }
                        return true;
                    case R.id.sortPr:
                        if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                            ArrayList<Note> sortList = sortUserPriority(notesList);
                            setNoteDetails(sortList,"ALL");
                        }
                        return true;
                    case R.id.sorttime:
                        if(notesList!=null && !notesList.isEmpty() && notesList.size()>0){
                            ArrayList<Note> sortListtime = order(notesList);
                            setNoteDetails(sortListtime,"ALL");
                        }
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


    public void setNoteDetails(ArrayList<Note> details,String flag) {
        if(details!=null && !details.isEmpty() && details.size()>0){
            int count=0;
            ArrayList<Note> pendingList = new ArrayList<Note>();
            ArrayList<Note> completedList = new ArrayList<Note>();
            ArrayList<Note> allList = new ArrayList<Note>();
            allList.addAll(details);
            for(Note note: details) {
                Log.d("x1",note.getTime());
                if(note.getStatus().equalsIgnoreCase("Completed")){
                    completedList.add(note);
                }
                else{
                    pendingList.add(note);
                }
            }

            details = new ArrayList<Note>();
            if(flag.equalsIgnoreCase("ALL")){
                details.addAll(pendingList);
                details.addAll(completedList);
            }else if(flag.equalsIgnoreCase("C")){
                details.addAll(completedList);
            }else if(flag.equalsIgnoreCase("P")){
                details.addAll(pendingList);
            }


            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mAdapter = new RecyclerAdapter(details);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);


        }



    }

    public ArrayList<Note> sortUserAsc(ArrayList<Note> appsList) {
        ArrayList<Note> sortedList = new ArrayList<Note>(appsList);
        Collections.sort(sortedList);
        return sortedList;
    }

    public ArrayList<Note> sortUserPriority(ArrayList<Note> appsList) {
        ArrayList<Note> sortedList = new ArrayList<Note>(appsList);
        Collections.sort(sortedList, Collections.<Note>reverseOrder());
        return sortedList;
    }

    public ArrayList<Note> order(ArrayList<Note> notes) {

        Collections.sort(notes, new Comparator() {

            public int compare(Object o1, Object o2) {

                String x1 = ((Note) o1).getTime();
                String x2 = ((Note) o2).getTime();
                //Log.d("x1",x1);
                //Log.d("x2",x2);
                //int sComp = Ptime.PtimeFormatter.getPTimeMillis(x1).compareTo(Ptime.PtimeFormatter.getPTimeMillis(x2));
                int sComp = (x2).compareTo((x1));
                return sComp;

            }});
        return notes;
    }
}
