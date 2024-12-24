package anastasiia.derkach.nure.practtask4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, ageEditText;
    private TextView messageTextView;
    private SharedPreferences sharedPreferences;

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;

    private EditText editTextInput;
    private TextView fileContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        messageTextView = findViewById(R.id.messageTextView);
        Button saveButton = findViewById(R.id.saveButton);
        messageTextView.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        EditText editTextName = findViewById(R.id.editTextName2);
        EditText editTextAge = findViewById(R.id.ageEditText2);
        Button buttonAddUser = findViewById(R.id.saveButton2);
        recyclerView = findViewById(R.id.recyclerViewUsers);

        editTextInput = findViewById(R.id.editTextInput);
        fileContentTextView = findViewById(R.id.fileContentTextView);
        Button writeToFileButton = findViewById(R.id.writeToFileButton);
        Button readFromFileButton = findViewById(R.id.readFromFileButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();

        loadUserData();

        saveButton.setOnClickListener(v -> {
            saveUserData();
        });

        buttonAddUser.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            int age = Integer.parseInt(editTextAge.getText().toString());

            databaseHelper.addUser(name, age);
            loadUsers();
        });

        writeToFileButton.setOnClickListener(v -> {
            String textToSave = editTextInput.getText().toString();
            writeToFile(textToSave);
        });

        readFromFileButton.setOnClickListener(v -> readFromFile());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUsers() {
        userList = databaseHelper.getAllUsers();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);
    }

    private void saveUserData() {
        String name = nameEditText.getText().toString();
        String ageText = ageEditText.getText().toString();

        if (name.isEmpty() || ageText.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповність усі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageText);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putInt("age", age);
        editor.apply();

        messageTextView.setText("Дані збережено! Ім'я: " + name + ", Вік: " + age);
        messageTextView.setVisibility(View.VISIBLE);
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", 0);

        if (!name.isEmpty()) {
            nameEditText.setText(name);
        }
        if (age != 0) {
            ageEditText.setText(String.valueOf(age));
        }
    }

    private void writeToFile(String data) {
        try {
            FileOutputStream fos = openFileOutput("myfile.txt", MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        try {
            FileInputStream fis = openFileInput("myfile.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            fis.close();
            fileContentTextView.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

