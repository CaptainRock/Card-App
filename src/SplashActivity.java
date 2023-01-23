package com.bj.enterprise.simple.simple;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread thread = new Thread(){


          @Override

          public void run(){

              try{

                  sleep(5000);

              }catch(Exception e){
                  e.printStackTrace();
              }finally{

                  Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                  startActivity(mainIntent);


              }

          }

        };
        thread.start();
    }

        @Override

        public void onPause(){

        super.onPause();
        finish();
        }

}
