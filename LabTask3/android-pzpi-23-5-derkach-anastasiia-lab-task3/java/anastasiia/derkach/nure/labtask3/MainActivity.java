package anastasiia.derkach.nure.labtask3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private double first = 0;
    private double second = 0;
    private String operator = "";
    private boolean newOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        setNumberClickListener(findViewById(R.id.button0), "0");
        setNumberClickListener(findViewById(R.id.button1), "1");
        setNumberClickListener(findViewById(R.id.button2), "2");
        setNumberClickListener(findViewById(R.id.button3), "3");
        setNumberClickListener(findViewById(R.id.button4), "4");
        setNumberClickListener(findViewById(R.id.button5), "5");
        setNumberClickListener(findViewById(R.id.button6), "6");
        setNumberClickListener(findViewById(R.id.button7), "7");
        setNumberClickListener(findViewById(R.id.button8), "8");
        setNumberClickListener(findViewById(R.id.button9), "9");
        setNumberClickListener(findViewById(R.id.buttonPoint), ".");

        setOperatorClickListener(findViewById(R.id.buttonPlus), "+");
        setOperatorClickListener(findViewById(R.id.buttonMinus), "-");
        setOperatorClickListener(findViewById(R.id.buttonTimes), "*");
        setOperatorClickListener(findViewById(R.id.buttonDivide), "/");

        findViewById(R.id.buttonEquals).setOnClickListener(v -> calculateResult());

        findViewById(R.id.buttonCancel).setOnClickListener(v -> clear());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setNumberClickListener(Button button, String number){
        button.setOnClickListener(view -> {
            String currentText = textView.getText().toString();
            if (number.equals(".")) {
                if (currentText.equals("0")) {
                    textView.setText("0.");
                    return;
                }
                if (currentText.contains(".")) {
                    return;
                }
            } else {
                if (currentText.equals("0.")) {
                    textView.append(number);
                    return;
                }
                if (currentText.equals("0")) {
                    textView.setText(number);
                    return;
                }
            }
            if (newOperation){
                textView.setText("");
                newOperation = false;
            }
            textView.append(number);
        });
    }

    private void setOperatorClickListener(Button button, String op){
        button.setOnClickListener(view -> {
            first = Double.parseDouble(textView.getText().toString());
            operator = op;
            newOperation = true;
        });
    }

    private void calculateResult () {
        if (!operator.isEmpty()) {
            second = Double.parseDouble(textView.getText().toString());
            double result = 0;
            switch (operator){
                case "+":
                    result = first + second;
                    break;
                case "-":
                    result = first - second;
                    break;
                case "*":
                    result = first * second;
                    break;
                case "/":
                    if(second != 0) {
                        result = first / second;
                    }
                    else {
                        textView.setText("Error");
                        newOperation = true;
                        return;
                    }
                    break;
            }

            textView.setText(String.valueOf(result));
            operator = "";
            newOperation = true;
        }
    }

    private void clear(){
        textView.setText("0");
        first = 0;
        second = 0;
        operator = "";
        newOperation = true;
    }
}
