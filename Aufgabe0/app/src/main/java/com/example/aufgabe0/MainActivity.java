package com.example.aufgabe0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view){
        Toast.makeText(this,"Button Clicked!", Toast.LENGTH_SHORT).show();
        EditText txt = findViewById(R.id.inputNumber);
        StringBuilder output = new StringBuilder();
        int[] arr = new int[8];
        for(int i = 0; i < txt.length(); i++){
            boolean isPrime = isPrime(Character.getNumericValue(txt.getText().charAt(i)));
            if(!isPrime){
                arr[i] = Character.getNumericValue(txt.getText().charAt(i));
            } else {
                arr[i] = -1;
            }
        }
        Arrays.sort(arr);
        for(int i: arr){
            if(i >= 0)
                output.append(i);
        }
        TextView outputField = findViewById(R.id.outputField);
        outputField.setText(output);
    }

    public boolean isPrime(int digit) {
        if (digit <= 1) {
            return false;
        }
        for (int i = 2; i <= digit/2; ++i) {
            if (digit % i == 0) {
                return false;
            }
        }
        return true;
    }
}