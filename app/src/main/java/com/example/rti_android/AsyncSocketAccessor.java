package com.example.rti_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.function.Function;

public class AsyncSocketAccessor {

    public static class AsyncSendMessage extends AsyncTask<Void, Void, Void>{
        String message;
        AsyncSendMessage(String message){
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.e("MainActivity_app",  "Start task - SendMessage");
                Net.sendMessage(message);
                Log.e("MainActivity_app",  "Message sent");
            } catch (Exception e) {
                Log.e("MainActivity_app",  "Sending operation - failed : " + e.getMessage());
            }
            return null;
        }
    }

    public static class AsyncReceiveMessage extends AsyncTask<Void, Void, String>{

        Context context;

        AsyncReceiveMessage(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String reponse = "";
            try {
                Log.e("MainActivity_app",  "Start task - ReceiveMessage");
                reponse = Net.RecevoirData();
                Log.e("MainActivity_app",  "Message received");
            } catch (Exception e) {
                Log.e("MainActivity_app",  "Receiving operation - failed : " + e.getMessage());
            }
            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            super.onPostExecute(reponse);
            Log.e("MainActivity_app",  "Start PostExecute");
            Analyzer.analyze(context, reponse);
        }
    }
}
