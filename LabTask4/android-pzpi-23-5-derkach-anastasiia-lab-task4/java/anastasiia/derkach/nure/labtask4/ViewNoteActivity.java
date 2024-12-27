package anastasiia.derkach.nure.labtask4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_note);

        TextView timeView = findViewById(R.id.noteDateTime);
        Intent intent = getIntent();
        String title = intent.getStringExtra("NOTE_TITLE");
        String description = intent.getStringExtra("NOTE_DESCRIPTION");
        long time = intent.getLongExtra("NOTE_TIME", -1);
        if (time != -1) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedTime = sdf.format(new Date(time));
            timeView.setText(formattedTime);
        } else {
            timeView.setText("Час не встановлено!");
        }
        String priorityLevelString = intent.getStringExtra("NOTE_PRIORITY_LEVEL");
        int iconResourceId = intent.getIntExtra("NOTE_ICON_RESOURCE_ID", -1);

        Note.PriorityLevel priorityLevel = Note.PriorityLevel.valueOf(priorityLevelString);

        TextView titleView = findViewById(R.id.noteTitle);
        TextView descriptionView = findViewById(R.id.noteDescription);

        ImageView priorityIconView = findViewById(R.id.priorityIcon);
        ImageView noteImageView = findViewById(R.id.noteImage);

        titleView.setText(title);
        descriptionView.setText(description);

        switch (priorityLevel) {
            case LOW:
                priorityIconView.setImageResource(R.drawable.ic_priority_low);
                break;
            case MEDIUM:
                priorityIconView.setImageResource(R.drawable.ic_priority_middle);
                break;
            case HIGH:
                priorityIconView.setImageResource(R.drawable.ic_priority_high);
                break;
        }

        if (iconResourceId != -1) {
            noteImageView.setImageResource(iconResourceId);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}