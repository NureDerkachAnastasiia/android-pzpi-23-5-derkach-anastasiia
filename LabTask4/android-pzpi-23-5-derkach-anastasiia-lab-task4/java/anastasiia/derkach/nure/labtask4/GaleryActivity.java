package anastasiia.derkach.nure.labtask4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class GaleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_galery);

        RecyclerView recyclerView = findViewById(R.id.gallery);

        // Список иконок для галереи
        List<Integer> iconResourceIds = Arrays.asList(
                R.drawable.ic_note_clock,
                R.drawable.ic_note_anchor,
                R.drawable.ic_note_person,
                R.drawable.ic_note_agriculture
        );

        // Устанавливаем адаптер
        GalleryAdapter adapter = new GalleryAdapter(iconResourceIds, resourceId -> {
            // Возвращаем выбранную иконку
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_icon_id", resourceId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
