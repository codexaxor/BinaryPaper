package com.codexaxor.mac.googlevisionv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class splashscreen extends AppCompatActivity {


    private ProgressBar progressBar;
    private int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        progressBar =(ProgressBar) findViewById(R.id.progressBarId);
        Thread thread =new Thread(new Runnable(){
            public void run() {


                doWork();
                startApp();


            }



        } );
        thread.start();
    }
    public void doWork(){

        for(progress=20;progress<=100;progress=progress+20) {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void startApp(){
        Intent intent =new Intent(splashscreen.this,Menu.class);
        startActivity(intent);
        finish();
    }
    protected void onStart(){
        super.onStart();
    }
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
