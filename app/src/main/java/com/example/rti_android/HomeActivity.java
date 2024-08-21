package com.example.rti_android;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HomeActivity extends Activity {
    static List<Article> articleList = new ArrayList<>();
    static float totalCaddie = 0;
    static Article current;
    private TableRow selectedRow;
    private int quantityNeeded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button;

        initAll();

        // Set up the button to logout
        button = (Button) this.findViewById(R.id.buttonLogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncSocketAccessor.AsyncSendMessage("LOGOUT#").execute();

                initTable();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // Set up the button to load next data
        button = (Button) this.findViewById(R.id.buttonNext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deleteMessage = "CONSULT#" + (current.Id + 1) + "#";
                new AsyncSocketAccessor.AsyncSendMessage(deleteMessage).execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
            }
        });

        // Set up the button to load the previous data
        button = (Button) this.findViewById(R.id.buttonBefore);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteMessage = "CONSULT#" + (current.Id - 1) + "#";
                new AsyncSocketAccessor.AsyncSendMessage(deleteMessage).execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
            }
        });

        // Set up the button to buy
        button = (Button) this.findViewById(R.id.buttonBuy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteMessage = ("ACHAT#" + current.Id + "#" + quantityNeeded + "#");
                new AsyncSocketAccessor.AsyncSendMessage(deleteMessage).execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
            }
        });

        // Set up the button to pay
        button = (Button) this.findViewById(R.id.buttonPay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteMessage = "CONFIRMER#";
                new AsyncSocketAccessor.AsyncSendMessage(deleteMessage).execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
            }
        });

        // Set up the button to remove an item
        button = (Button) this.findViewById(R.id.buttonRemoveItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRow != null) {
                    initTotal();
                    initTable();
                    TableLayout tl = findViewById(R.id.tableLayoutCaddie);
                    int rowIndex = tl.indexOfChild(selectedRow);

                    if (rowIndex > 0 && rowIndex <= articleList.size()) {
                        Article articleToRemove = articleList.get(rowIndex - 1);
                        articleList.remove(rowIndex - 1);

                        String deleteMessage = "CANCEL#" + articleToRemove.Id + "#";
                        new AsyncSocketAccessor.AsyncSendMessage(deleteMessage).execute();
                        new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();

                        tl.removeView(selectedRow);

                        selectedRow = null;
                    }
                }
            }
        });

        // Set up the button to clear all items
        button = (Button) this.findViewById(R.id.buttonClearAll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncSocketAccessor.AsyncSendMessage("CANCEL_ALL#").execute();
                new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
            }
        });

        // Set up the button to +
        button = (Button) this.findViewById(R.id.buttonIncrease);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityNeeded < current.Quantity) {
                    quantityNeeded += 1;
                    TextView qn = findViewById(R.id.textViewQuantityNeededContent);
                    qn.setText("" + quantityNeeded);
                }
            }
        });

        // Set up the button to -
        button = (Button) this.findViewById(R.id.buttonDecrease);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityNeeded > 0) {
                    quantityNeeded -= 1;

                    TextView qn = findViewById(R.id.textViewQuantityNeededContent);
                    qn.setText("" + quantityNeeded);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new AsyncSocketAccessor.AsyncSendMessage("#CONSULT#1#").execute();
        new AsyncSocketAccessor.AsyncReceiveMessage(HomeActivity.this).execute();
    }

    public void initAll(){
        initListArticle();
        initTotal();
        initTable();
        initArticleInfo();
    }


    public void initListArticle(){
        articleList = new ArrayList<>();
    }
    public void initTotal(){
        totalCaddie = 0;
        TextView tl = findViewById(R.id.textViewTotal);
        tl.setText("total: " + totalCaddie + "€");
    }
    public void initArticleInfo(){
        TextView ta = findViewById(R.id.textViewArticleContent);
        TextView ts = findViewById(R.id.textViewStockContent);
        TextView tp = findViewById(R.id.textViewPriceContent);

        ta.setText("");
        ts.setText("");
        tp.setText("");

        ImageView img = findViewById(R.id.imageViewArticle);

        img.setImageResource(R.drawable.carottes);
    }
    public void initTable(){
        TableLayout tl = findViewById(R.id.tableLayoutCaddie);
        tl.removeAllViews();
        TableRow headerRow = new TableRow(this);
        TextView articleHeader = new TextView(this);
        TextView priceHeader = new TextView(this);
        TextView quantityHeader = new TextView(this);

        articleHeader.setText("Article");
        priceHeader.setText("Prix");
        quantityHeader.setText("Quantité");

        headerRow.addView(articleHeader);
        headerRow.addView(priceHeader);
        headerRow.addView(quantityHeader);

        headerRow.setBackgroundColor(Integer.parseInt("404040",16));
        tl.addView(headerRow);
    }

   public void setCurrentArticle(String[] split){
       Log.e("MainActivity_app",  "SET CURRENT ARTICLE ");
        current = new Article(Integer.parseInt(split[1]),split[2],Float.parseFloat(split[3]),Integer.parseInt(split[4]),split[5]);
        current.Id = Integer.parseInt(split[1]);
        current.Intitule = split[2];
        current.Prix = Float.parseFloat(split[3]);
        current.Quantity = Integer.parseInt(split[4]);
        current.Image = split[5];

        ImageView img = findViewById(R.id.imageViewArticle);
        TextView article = findViewById(R.id.textViewArticleContent);
        TextView price = findViewById(R.id.textViewPriceContent);
        TextView stock = findViewById(R.id.textViewStockContent);

        article.setText(current.Intitule);
        price.setText(String.format("%.2f", current.Prix) + "€");
        stock.setText(String.valueOf(current.Quantity));

        int imageResource = getResources().getIdentifier(current.Intitule.toLowerCase().replaceAll(" ", ""), "drawable", getPackageName());
        img.setImageResource(imageResource);
    }

    public void totalAdd(double value){
        totalCaddie += value;

        TextView totalview = findViewById(R.id.textViewTotal);
        totalview.setText(String.format("%.2f", totalCaddie) + "€");
    }

    public void addArticleToTable(Article article) {
        TableLayout tl = findViewById(R.id.tableLayoutCaddie);
        TableRow row = new TableRow(this);

        TextView articleName = new TextView(this);
        articleName.setText(article.Intitule);
        TextView articlePrice = new TextView(this);
        articlePrice.setText(String.format("%.2f", article.Prix) + "€");
        TextView articleQuantity = new TextView(this);
        articleQuantity.setText(String.valueOf(article.Quantity));

        row.addView(articleName);
        row.addView(articlePrice);
        row.addView(articleQuantity);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRow != null) {
                    selectedRow.setBackgroundColor(0);
                }
                selectedRow = row;
                row.setBackgroundColor(0xFF00FF00);
            }
        });

        tl.addView(row);
    }
}
