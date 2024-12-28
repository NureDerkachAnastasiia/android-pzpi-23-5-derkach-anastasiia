package anastasiia.derkach.nure.labtask4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public NoteDatabase(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(DatabaseHelper.COLUMN_TIME, note.getTime());
        values.put(DatabaseHelper.COLUMN_ICON_RESOURCE_ID, note.getIconResourceId());
        values.put(DatabaseHelper.COLUMN_PRIORITY_LEVEL, note.getPriorityLevel().name());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, note.getDescription());
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, note.getImageUri());

        return database.insert(DatabaseHelper.TABLE_NOTES, null, values);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NOTES,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_TIME,
                        DatabaseHelper.COLUMN_ICON_RESOURCE_ID, DatabaseHelper.COLUMN_PRIORITY_LEVEL,
                        DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_IMAGE_URI},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
                int iconResourceId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ICON_RESOURCE_ID));
                Note.PriorityLevel priorityLevel = Note.PriorityLevel.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRIORITY_LEVEL)));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URI));

                Note note = new Note(title, time, iconResourceId, R.drawable.ic_priority_low, description, imageUri, priorityLevel);
                notes.add(note);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return notes;
    }

    public int updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(DatabaseHelper.COLUMN_TIME, note.getTime());
        values.put(DatabaseHelper.COLUMN_ICON_RESOURCE_ID, note.getIconResourceId());
        values.put(DatabaseHelper.COLUMN_PRIORITY_LEVEL, note.getPriorityLevel().name());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, note.getDescription());
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, note.getImageUri());

        String whereClause = DatabaseHelper.COLUMN_TITLE + "=?";
        String[] whereArgs = {note.getTitle()};

        return database.update(DatabaseHelper.TABLE_NOTES, values, whereClause, whereArgs);
    }

    public int deleteNote(Note note) {
        String whereClause = DatabaseHelper.COLUMN_TITLE + "=?";
        String[] whereArgs = {note.getTitle()};

        return database.delete(DatabaseHelper.TABLE_NOTES, whereClause, whereArgs);
    }
}
