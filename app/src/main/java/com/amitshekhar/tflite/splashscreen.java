package com.amitshekhar.tflite;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class splashscreen extends AppCompatActivity {


    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_splashscreen);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        new Thread(new Runnable() {
            @Override
            public void run() {
                dowork();
                startapp();
                finish();
            }
        }).start();

    }

    private void dowork() {
        for(int progress=0;progress<20;progress+=10){
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }}



    private void startapp() {
        Intent i=new Intent(splashscreen.this,Main2Activity.class);
        startActivity(i);
    }
}