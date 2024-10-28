package anastasiia.derkach.nure.practtask2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: called");

        Button button = findViewById(R.id.button11);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    public void buttonClick(View view) {
        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(Color.BLUE);
    }

    public void button1Click(View view) {
        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(Color.RED);
    }

    public void button2Click(View view) {
        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(Color.BLACK);
    }

    public void button3Click(View view) {
        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(Color.GREEN);
    }

    public void button5Click(View view) {
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText("Text changed");
    }

    public void button6Click(View view){
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Button 1 clicked");
    }

    public void button7Click(View view){
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Button 2 clicked");
    }

    public void button8Click(View view){
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Button 3 clicked");
    }

    public void button9Click(View view){
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Button 4 clicked");
    }

    public void button10Click(View view){
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Button 5 clicked");
    }

    public void button12Click(View view){
        finish();
    }
}