package com.example.rti_android;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Analyzer {
    public static Void analyze(Context context , String message){
        String[] split = message.split("#");

        switch(split[0]){
            case "LOGIN":
                Log.e("MainActivity_app",  "Start LOGIN: " + message);
                    switch(split[1]){
                        case "OK":
                            int id = Integer.parseInt(split[2]);
                            //Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();

                            Dialog.showAlertDialog(context, "Etat de connexion", "Connexion réussie !\nId : " + id, (ctx) -> {
                                Intent intent = new Intent(ctx, HomeActivity.class);
                                startActivity(ctx,intent,null);
                                return null;
                            });
                            break;
                        case "KO":
                            Dialog.showAlertDialog(context, "Connexion échouée !","Etat de connexion", null);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(context,intent,null);
                            break;
                    }
                break;

        case "CONSULT":
                Log.e("MainActivity_app",  "Start CONSULT: " + message);
                System.out.println("id = " + split[1]);
                if(Integer.parseInt(split[1]) != 0)
                {
                    Log.e("MainActivity_app","consult: " + split[1] + " - " + split[2]);
                    HomeActivity homeActivity = (HomeActivity) context;
                    homeActivity.setCurrentArticle(split);
                }
                break;

        case "ACHAT":
            Log.e("MainActivity_app",  "Start ACHAT: " + message);
            int id = Integer.parseInt(split[1]);
            if(id != -1 && id != 0){
                Dialog.showAlertDialog(context,"Success", "Achat reussi !", null);
                HomeActivity homeActivity = (HomeActivity) context;
                homeActivity.initTable();
                homeActivity.initTotal();
                new AsyncSocketAccessor.AsyncSendMessage("CADDIE#").execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(context).execute();
            }
            else{
                if(id == 0){
                    Dialog.showAlertDialog(context,"Failed", "Quantité insuffisante!", null);
                }
                else {
                    Dialog.showAlertDialog(context,"Failed", "Le caddie est plein!", null);
                }
            }
            break;

        case "CADDIE":
            Log.e("MainActivity_app",  "Start CADDIE: " + message);
        {
            HomeActivity homeActivity = (HomeActivity) context;
            homeActivity.initTable();
            homeActivity.initTotal();
            for (int i = 1; i < split.length - 1; i += 4) {
                String intitule_ = split[i + 1];
                double prix_ = Float.parseFloat(split[i + 2]);
                int quantite_ = Integer.parseInt(split[i + 3]);

                homeActivity.addArticleToTable(new Article(intitule_, prix_, quantite_));
                homeActivity.totalAdd(quantite_ * prix_);
            }
        }
            break;

        case "CANCEL":
            Log.e("MainActivity_app",  "Start CANCEL: " + message);
            System.out.println(split[1]);
            if(split[1].equals("OK"))
            {
                new AsyncSocketAccessor.AsyncSendMessage("CADDIE#").execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(context).execute();
            }
            else {
                Dialog.showAlertDialog(context, "Failed", "L'article n'a pas pu etre retiré", null);
            }
                break;

        case "CANCEL_ALL":
                Log.e("MainActivity_app",  "Start CANCEL_ALL: " + message);
            {
                HomeActivity homeActivity = (HomeActivity) context;
                homeActivity.initTable();
                homeActivity.initTotal();
            }
            break;

        case "CONFIRMER":
                Log.e("MainActivity_app",  "Start CONFIRMER: " + message);
            {
                HomeActivity homeActivity = (HomeActivity) context;
                homeActivity.initTable();
                homeActivity.initTotal();
            }

            if(Integer.parseInt(split[1]) != -1)
            {
                Dialog.showAlertDialog(context,"Success", "Commande validée", null);
            }
            else
            {
                Dialog.showAlertDialog(context,"Failed", "Commande non validée", null);
            }
            break;
        }
        return null;
    }
}
