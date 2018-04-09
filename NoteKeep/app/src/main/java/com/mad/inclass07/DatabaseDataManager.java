package com.mad.inclass07;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by mihai on 2/27/17.
 */

public class DatabaseDataManager {
    private Context context;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private NoteDAO noteDAO;

    public DatabaseDataManager(Context context) {
        this.context = context;
        dbOpenHelper = new DatabaseOpenHelper(this.context);

        db=dbOpenHelper.getWritableDatabase();
        noteDAO = new NoteDAO(db);

    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }

    public NoteDAO getNoteDAO(){
        return this.noteDAO;
    }

    public long saveNote(Note note){
        return this.noteDAO.save(note);
    }

    public boolean updateNote(Note note){
        return this.noteDAO.update(note);
    }

    public boolean deleteNote(Note note){
        return this.noteDAO.delete(note);
    }

    public Note getNote(long id){
        return this.noteDAO.get(id);
    }

    public List<Note> getAllNotes(){
        return this.noteDAO.getAll();
    }

    public List<Note> getAllCompleted(){
        return this.noteDAO.getAllCompleted();
    }
    
    public List<Note> getAllPending(){
        return this.noteDAO.getAllPending();
    }


}
