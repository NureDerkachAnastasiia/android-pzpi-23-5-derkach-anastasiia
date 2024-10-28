package anastasiia.derkach.nure.practtask2;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    private EditText editText;
    private String KEY_TEXT = "savedText";
    private int clickCount = 0;
    private long timerBase;
    private long elapsedTime = 0;
    private boolean isRunning = false;
    private TextView tvCounter;
    private TextView tvTimer;
    private Button btnIncrement;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        editText = findViewById(R.id.editTextText);
        tvCounter = findViewById(R.id.textView3);
        tvTimer = findViewById(R.id.textView6);
        btnIncrement = findViewById(R.id.button13);
        handler = new Handler();

        btnIncrement.setOnClickListener(view -> {
            clickCount++;
            tvCounter.setText("Clicks: " + clickCount);
        });

        if(savedInstanceState != null){
            String savedText = savedInstanceState.getString(KEY_TEXT);
            editText.setText(savedText);

            clickCount = savedInstanceState.getInt("clickCount");
            elapsedTime = savedInstanceState.getLong("elapsedTime");
            isRunning = savedInstanceState.getBoolean("isRunning");

            tvCounter.setText("Clicks: " + clickCount);
            if (isRunning) {
                timerBase = SystemClock.elapsedRealtime() - elapsedTime;
                handler.post(timerRunnable);
            } else {
                tvTimer.setText("Timer: " + (elapsedTime / 1000));
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TEXT, editText.getText().toString());
        outState.putInt("clickCount", clickCount);
        outState.putLong("elapsedTime", elapsedTime);
        outState.putBoolean("isRunning", isRunning);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedText = savedInstanceState.getString(KEY_TEXT);
        if(savedText != null){
            editText.setText(savedText);
        }
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = SystemClock.elapsedRealtime() - timerBase;
            tvTimer.setText("Timer: " + (elapsedTime / 1000));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRunning) {
            timerBase = SystemClock.elapsedRealtime() - elapsedTime;
            handler.post(timerRunnable);
            isRunning = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timerRunnable);
        isRunning = false;
    }
}