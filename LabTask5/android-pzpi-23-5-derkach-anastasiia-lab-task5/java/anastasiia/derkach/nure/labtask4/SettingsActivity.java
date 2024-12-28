package anastasiia.derkach.nure.labtask4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {
    private static final String FONT_SIZE_KEY = "font_size";
    private static final String SMALL_FONT = "small";
    private static final String LARGE_FONT = "large";
    private RadioGroup themeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyThemeFromPreferences();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String currentFontSize = sharedPreferences.getString(FONT_SIZE_KEY, SMALL_FONT);

        RadioGroup radioGroup = findViewById(R.id.radioGroupFontSize);
        if (SMALL_FONT.equals(currentFontSize)) {
            ((RadioButton) findViewById(R.id.radioButtonSmall)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioButtonLarge)).setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (checkedId == R.id.radioButtonSmall) {
                editor.putString(FONT_SIZE_KEY, SMALL_FONT);
            } else if (checkedId == R.id.radioButtonLarge) {
                editor.putString(FONT_SIZE_KEY, LARGE_FONT);
            }
            editor.apply();

            applyFontSize();
        });

        themeRadioGroup = findViewById(R.id.themeRadioGroup);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = preferences.getString("theme", "light");

        if (currentTheme.equals("light")) {
            themeRadioGroup.check(R.id.radioLightTheme);
        } else {
            themeRadioGroup.check(R.id.radioDarkTheme);
        }

        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (checkedId == R.id.radioLightTheme) {
                editor.putString("theme", "light");
            } else if (checkedId == R.id.radioDarkTheme) {
                editor.putString("theme", "dark");
            }
            editor.apply();

            restartAppForThemeChange();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void applyFontSize() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void applyThemeFromPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme", "light");

        if ("light".equals(theme)) {
            setTheme(R.style.AppTheme_Light);
        } else if ("dark".equals(theme)) {
            setTheme(R.style.AppTheme_Dark);
        }
    }

    private void restartAppForThemeChange() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}