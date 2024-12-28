package anastasiia.derkach.nure.labtask4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra_note";
    private Note currentNote;
    private int selectedIconId = R.drawable.ic_note_person;

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
        setContentView(R.layout.activity_edit_note);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String fontSize = sharedPreferences.getString("font_size", "small");
        if ("large".equals(fontSize)) {
            setTheme(R.style.AppTheme_LargeFont);
        } else {
            setTheme(R.style.AppTheme_SmallFont);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE)) {
            currentNote = (Note) intent.getSerializableExtra(EXTRA_NOTE);
            populateFields(currentNote);
        } else {
            currentNote = null;
        }

        findViewById(R.id.buttonOK).setOnClickListener(v -> saveNote());
        findViewById(R.id.buttonImage).setOnClickListener(v -> openGallery());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void populateFields(Note note) {
        ((EditText) findViewById(R.id.editTextName)).setText(note.getTitle());
        ((EditText) findViewById(R.id.editTextDesc)).setText(note.getDescription());

        switch (note.getPriorityLevel()) {
            case HIGH:
                ((RadioButton) findViewById(R.id.radioButtonHigh)).setChecked(true);
                break;
            case MEDIUM:
                ((RadioButton) findViewById(R.id.radioButtonMedium)).setChecked(true);
                break;
            case LOW:
                ((RadioButton) findViewById(R.id.radioButtonLow)).setChecked(true);
                break;
        }

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(note.getTime());

        TimePicker timePicker = findViewById(R.id.timePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(note.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        ImageView imageView = findViewById(R.id.imageView);
        String imageUri = note.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUri);
                imageView.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.ic_note_person);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_note_person);
        }
    }

    private void saveNote() {
        String title = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        String description = ((EditText) findViewById(R.id.editTextDesc)).getText().toString();

        CalendarView calendarView = findViewById(R.id.calendarView);
        long selectedDate = calendarView.getDate();

        TimePicker timePicker = findViewById(R.id.timePicker);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long finalTimeInMillis = calendar.getTimeInMillis();

        Note.PriorityLevel priorityLevel = Note.PriorityLevel.LOW;
        if (((RadioButton) findViewById(R.id.radioButtonMedium)).isChecked()) {
            priorityLevel = Note.PriorityLevel.MEDIUM;
        } else if (((RadioButton) findViewById(R.id.radioButtonHigh)).isChecked()) {
            priorityLevel = Note.PriorityLevel.HIGH;
        }

        Note updatedNote = new Note(
                title,
                finalTimeInMillis,
                selectedIconId,
                R.drawable.ic_priority_low,
                description,
                currentNote != null ? currentNote.getImageUri() : "",
                priorityLevel
        );

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EditNoteActivity.EXTRA_NOTE, updatedNote);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private static final int REQUEST_CODE_GALLERY = 1;

    private void openGallery() {
        Intent intent = new Intent(this, GaleryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            selectedIconId = data.getIntExtra("selected_icon_id", R.drawable.ic_note_person);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageResource(selectedIconId);
        }
    }
}

