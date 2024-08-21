package com.example.rti_android;

class Article {
    int Id;
    String Intitule;
    double Prix;
    int Quantity;
    String Image;

    Article(int id, String intitule, double prix, int quantity, String image) {
        Id = id;
        Intitule = intitule;
        Prix = prix;
        Quantity = quantity;
        Image = image;
    }

    Article(String intitule, double prix, int quantity) {
        Id = -1;
        Intitule = intitule;
        Prix = prix;
        Quantity = quantity;
        Image = "";
    }
}