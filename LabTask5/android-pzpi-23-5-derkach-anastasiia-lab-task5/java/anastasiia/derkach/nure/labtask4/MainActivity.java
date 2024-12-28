package anastasiia.derkach.nure.labtask4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> notes;
    private List<Note> originalNotes;
    private static final int REQUEST_EDIT_NOTE = 1;
    private static final int MODE_ADD = 0;
    private static final int MODE_EDIT = 1;

    private int currentMode;
    private int selectedNoteIndex;

    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme", "light");

        if ("light".equals(theme)) {
            setTheme(R.style.AppTheme_Light);
        } else if ("dark".equals(theme)) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String fontSize = sharedPreferences.getString("font_size", "small");
        if ("large".equals(fontSize)) {
            setTheme(R.style.AppTheme_LargeFont);
        } else {
            setTheme(R.style.AppTheme_SmallFont);
        }

        noteDatabase = new NoteDatabase(this);

        loadNotesFromDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteAdapter = new NoteAdapter(this, notes, note -> {
            Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_DESCRIPTION", note.getDescription());
            intent.putExtra("NOTE_PRIORITY_LEVEL", note.getPriorityLevel().name());
            intent.putExtra("NOTE_TIME", note.getTime());
            intent.putExtra("NOTE_IMAGE_URI", note.getImageUri());
            startActivity(intent);
        }, new NoteAdapter.OnNoteMenuClickListener() {
            @Override
            public void onEditNote(int position) {
                editNoteAt(position);
            }

            @Override
            public void onDeleteNote(int position) {
                deleteNoteAt(position);
            }
        });
        recyclerView.setAdapter(noteAdapter);
        registerForContextMenu(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                view.setTag(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                Note clickedNote = notes.get(position);
                Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
                intent.putExtra("NOTE_TITLE", clickedNote.getTitle());
                intent.putExtra("NOTE_DESCRIPTION", clickedNote.getDescription());
                intent.putExtra("NOTE_PRIORITY_LEVEL", clickedNote.getPriorityLevel().name());
                intent.putExtra("NOTE_TIME", clickedNote.getTime());
                intent.putExtra("NOTE_ICON_RESOURCE_ID", clickedNote.getIconResourceId());
                startActivity(intent);
            }
        }));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadNotesFromDatabase() {
        noteDatabase.open();
        notes = noteDatabase.getAllNotes();
        originalNotes = new ArrayList<>(notes);
        noteDatabase.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotesByQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotesByQuery(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            EditText searchEditText = findViewById(R.id.editTextSearch);

            searchEditText.setVisibility(View.VISIBLE);

            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    filterNotesByQuery(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            return true;
        } else if (id == R.id.action_add_note) {
            currentMode = MODE_ADD;
            Intent intent = new Intent(this, EditNoteActivity.class);
            startActivityForResult(intent, REQUEST_EDIT_NOTE);
            return true;
        } else if (id == R.id.action_filter) {
            showPriorityFilterDialog();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void filterNotesByQuery(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : originalNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }

    private void showPriorityFilterDialog() {
        String[] priorityOptions = {getString(R.string.priority_low), getString(R.string.priority_medium), getString(R.string.priority_high)};
        new AlertDialog.Builder(this)
                .setTitle(R.string.filter_priority)
                .setItems(priorityOptions, (dialog, which) -> {
                    Note.PriorityLevel selectedPriority;
                    switch (which) {
                        case 0:
                            selectedPriority = Note.PriorityLevel.LOW;
                            break;
                        case 1:
                            selectedPriority = Note.PriorityLevel.MEDIUM;
                            break;
                        case 2:
                            selectedPriority = Note.PriorityLevel.HIGH;
                            break;
                        default:
                            return;
                    }
                    filterNotesByPriority(selectedPriority);
                })
                .show();
    }

    private void filterNotesByPriority(Note.PriorityLevel priorityLevel) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : originalNotes) {
            if (note.getPriorityLevel() == priorityLevel) {
                filteredNotes.add(note);
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        View view = findViewById(item.getItemId());
        int position = (int) view.getTag();

        int id = item.getItemId();

        if (id == R.id.action_edit) {
            editNoteAt(position);
            return true;
        } else if (id == R.id.action_delete) {
            deleteNoteAt(position);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }


    private void editNoteAt(int position) {
        selectedNoteIndex = position;
        currentMode = MODE_EDIT;

        Note noteToEdit = notes.get(position);
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_NOTE, noteToEdit);
        startActivityForResult(intent, REQUEST_EDIT_NOTE);
    }

    private void deleteNoteAt(int position) {
        Note noteToDelete = notes.get(position);
        noteDatabase.open();
        noteDatabase.deleteNote(noteToDelete);
        noteDatabase.close();

        notes.remove(position);
        originalNotes.remove(position);
        noteAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Integer position = (Integer) v.getTag();
        if (position != null) {
            menu.setHeaderTitle("Виберіть дію для нотатки");
            getMenuInflater().inflate(R.menu.menu_context, menu);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            Note updatedNote = (Note) data.getSerializableExtra(EditNoteActivity.EXTRA_NOTE);
            if (updatedNote != null) {
                noteDatabase.open();
                if (currentMode == MODE_ADD) {
                    noteDatabase.insertNote(updatedNote);
                    notes.add(updatedNote);
                    originalNotes.add(updatedNote);
                } else if (currentMode == MODE_EDIT) {
                    noteDatabase.updateNote(updatedNote);
                    notes.set(selectedNoteIndex, updatedNote);
                    originalNotes.set(selectedNoteIndex, updatedNote);
                }
                noteDatabase.close();
                noteAdapter.notifyDataSetChanged();
            }
        }
    }
}

