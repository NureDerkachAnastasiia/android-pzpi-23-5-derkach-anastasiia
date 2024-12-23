package anastasiia.derkach.nure.practtask3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private HandlerThread handlerThread;
    private Handler backgroundHandler;
    private Handler mainHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();

        backgroundHandler = new Handler(handlerThread.getLooper());

        mainHandler = new Handler(Looper.getMainLooper());

        TextView handlerMessageTextView = findViewById(R.id.handlerMessageTextView);
        Button startHandlerButton = findViewById(R.id.startHandlerButton);

        startHandlerButton.setOnClickListener(v -> {
            backgroundHandler.post(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainHandler.post(() -> handlerMessageTextView.setText("Task completed in background thread"));
            });
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> myDataset = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            myDataset.add("Item " + (i + 1));
        }

        MyAdapter adapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    public void showAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Діалог")
                .setMessage("Це приклад AlertDialog.")
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .show();
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                }, 2023, 8, 1);
        datePickerDialog.show();
    }

    public void showCustomDialog(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params);
            }
        });

        dialog.show();
    }
}

