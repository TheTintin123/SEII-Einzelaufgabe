package com.example.aufgabe0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import java.net.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    int counter = 0;
    String calcOutputTxt, serverOutputTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView calcOutputField = findViewById(R.id.outputField);
        TextView serverOutputField = findViewById(R.id.outputServer);
        calcOutputTxt = (String) calcOutputField.getText();
        serverOutputTxt = (String) serverOutputField.getText();
    }

    public void onConnectionButtonClick(View view){
        new ClientThreader().execute();
        Toast.makeText(this,"ServerButton Clicked!", Toast.LENGTH_SHORT).show();
    }

    public void onCalcButtonClick(View view){
        progress();
        Toast.makeText(this,"CalcButton Clicked!", Toast.LENGTH_SHORT).show();
        EditText txt = findViewById(R.id.inputNumber);
        StringBuilder output = new StringBuilder();
        int[] arr = new int[8];
        for(int i = 0; i < txt.length(); i++){
            boolean isPrime = isPrime(Character.getNumericValue(txt.getText().charAt(i)));
            if(!isPrime){
                arr[i] = Character.getNumericValue(txt.getText().charAt(i));
            } else {
                arr[i] =-1;
            }
        }
        Arrays.sort(arr);
        for(int i: arr){
            if(i >= 0)
                output.append(i);
        }
        calcOutputTxt = output.toString();
    }

    private void progress(){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView calcOutputField = findViewById(R.id.outputField);
        TextView serverOutputField = findViewById(R.id.outputServer);
        final Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter*4);
                if(counter == 25){
                    calcOutputField.setText(calcOutputTxt);
                    serverOutputField.setText(serverOutputTxt);
                    timer.cancel();
                    counter = 0;
                }

            }
        };
        timer.schedule(tt,0,100);
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

    private class ClientThreader extends AsyncTask<Void,Void,Void>{

        private String response = "";
        @Override
        protected void onPreExecute() {
            progress();
        }

        @Override
        protected Void doInBackground(Void... voids){

            Socket socket = null;
            InputStreamReader in = null;
            OutputStreamWriter out = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            EditText txt = findViewById(R.id.inputNumber);

            try {
                socket = new Socket("se2-isys.aau.at",53212);

                in = new InputStreamReader(socket.getInputStream());
                out = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(in);
                bufferedWriter = new BufferedWriter(out);

                    bufferedWriter.write(txt.getText().toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    response = bufferedReader.readLine();

                    System.out.println("Server: " + response);

            } catch (IOException e){
                e.printStackTrace();

            } finally {
                try {
                    if(socket != null)
                        socket.close();

                    if(in != null)
                        in.close();

                    if(out != null)
                        out.close();

                    if(bufferedReader != null)
                        bufferedReader.close();

                    if(bufferedWriter != null)
                        bufferedWriter.close();

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            serverOutputTxt = response;
        }

    }
}