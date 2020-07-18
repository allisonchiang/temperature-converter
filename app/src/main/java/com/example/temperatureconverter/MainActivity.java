/**
 *  Author: Allison Chiang
 *  CSC472 Android App Development
 *  Assignment 1 - Temperature Converter
 *  October 5, 2019
 */

package com.example.temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;

    private TextView inputTempType, outputTempType;
    private EditText inputTemp;
    private TextView outputTemp;
    private TextView outputHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    // save data before screen rotation in bundle
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTempType = findViewById(R.id.inputTempType);
        outputTempType = findViewById(R.id.outputTempType);
        outputHistory = findViewById(R.id.conversionHistoryText);
        outputHistory.setMovementMethod(new ScrollingMovementMethod());

        RadioButton buttonFC = findViewById(R.id.radioButtonFC);
        RadioButton buttonCF = findViewById(R.id.radioButtonCF);

        inputTemp = findViewById(R.id.inputTemp);
        outputTemp = findViewById(R.id.outputTemp);

        // When restarting the app, load the shared preference of conversion type (F to C / C to F).
        // If no preference was indicated, select the default radio button conversion of F to C.
        myPrefs = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        int savedRadioIndex = myPrefs.getInt("SAVED_RADIO_BUTTON", R.id.radioButtonFC);   // default is F to C
        if (savedRadioIndex == R.id.radioButtonFC) {
            buttonFC.setChecked(true);
            inputTempType.setText("Fahrenheit Degrees:");
            outputTempType.setText("Celsius Degrees:");
        } else if (savedRadioIndex == R.id.radioButtonCF) {
            buttonCF.setChecked(true);
            inputTempType.setText("Celsius Degrees:");
            outputTempType.setText("Fahrenheit Degrees:");
        }

        // save bundle state
//        if (savedInstanceState == null)
//            Toast.makeText(this, "Bundle NULL", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(this, "Bundle NOT NULL", Toast.LENGTH_LONG).show();

    }

    // Note: using the id is better, since it is more likely that the id stays constant over time
    public void radioButtonClicked(View v) {   // View is a radio button group
        switch (v.getId()) {
            case R.id.radioButtonFC:
                inputTempType.setText("Fahrenheit Degrees:");
                outputTempType.setText("Celsius Degrees:");
                Log.d(TAG, "radioButtonClicked: " + R.id.radioButtonFC);
                Toast.makeText(this, "option F to C selected", Toast.LENGTH_SHORT).show(); // floating message
                break;
            case R.id.radioButtonCF:
                inputTempType.setText("Celsius Degrees:");
                outputTempType.setText("Fahrenheit Degrees:");
                Log.d(TAG, "radioButtonClicked: " + R.id.radioButtonCF);
                Toast.makeText(this, "option C to F selected", Toast.LENGTH_SHORT).show(); // floating message
                break;
            default:
                Log.d(TAG, "radioButtonClicked: UNKNOWN VIEW SELECTED");
                break;
        }

        outputTemp.setText(""); // when new radio button selected, clear the output temperature to avoid confusion

        prefsEditor = myPrefs.edit();
        prefsEditor.putInt("SAVED_RADIO_BUTTON", v.getId());
        prefsEditor.apply();
    }

    // converts and displays the temperature and adds to history when the convert button is clicked
    public void convertTemp(View v) {
        String inputTempString = inputTemp.getText().toString();
        String outputTempString;
        Log.d(TAG, "convertTemp: converting input temperature of " + inputTempString);
        double inputTempDouble = Double.parseDouble(inputTempString);
        double outputTempDouble;
        String newOutputString;

        if (inputTempType.getText().toString().equals("Fahrenheit Degrees:")) {
            outputTempDouble = (inputTempDouble - 32.0) / 1.8;
            newOutputString = String.format("F to C: %.1f F ---> %.1f C", inputTempDouble, outputTempDouble);
        } else {
            outputTempDouble = (inputTempDouble * 1.8) + 32.0;
            newOutputString = String.format("C to F: %.1f C ---> %.1f F", inputTempDouble, outputTempDouble);
        }

        // format and display the output temperature to one decimal place
        outputTempString = String.format("%.1f", outputTempDouble);
        outputTemp.setText(outputTempString);

        // update the conversion history with the new conversion on the first line
        String outputString = outputHistory.getText().toString();
        outputString = newOutputString + "\n" + outputString;
        outputHistory.setText(outputString);
    }

    // clears the conversion history box when the clear button is clicked
    public void clearHistory(View v) {
        Log.d(TAG, "clearHistory: History cleared");
        outputHistory.setText("");
    }

    // save the conversion history text so it will be displayed properly after screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", outputHistory.getText().toString());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    // restore the conversion history text after screen rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        outputHistory.setText(savedInstanceState.getString("HISTORY"));
    }
}