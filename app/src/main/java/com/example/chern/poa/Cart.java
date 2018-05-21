package com.example.chern.poa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cart extends Activity {

//    private String _EXTRA_pizza_name = "";
//    private String _EXTRA_pizza_dough = "";
//    private String _EXTRA_pizza_size = "";
//    private String[] _EXTRA_pizza_toppings = {};
//    private String _EXTRA_pizza_price = "";

    private ArrayList<String[]> _EXTRA_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        processExtra();



        // if orders are not empty
            // generate orders
        //else
            // generate empty cart
        generateOrders();

        configureNewPizzaBtn();
        configureProceedOrder();
    }

    private void configureNewPizzaBtn(){
        Button btnPizza = findViewById(R.id.btnPizza);
        btnPizza.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void configureProceedOrder(){
        Button btnPizza = findViewById(R.id.btnOrder);
        btnPizza.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(Cart.this,"Thank You for Your order!", Toast.LENGTH_LONG).show();
                // Send order's data somewhere to process
            }
        });
    }

    private void generateOrders(){

        // For every order row
            // Generate panel
            // Assign an event listener
        int counter = 0;
        if(_EXTRA_orders != null){
            for(String[] order : _EXTRA_orders){
                final int cnt = counter;

                TextView pizzaLabel = new TextView(this);
                pizzaLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                pizzaLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                pizzaLabel.setAllCaps(true);
                pizzaLabel.setText(order[0]);

                TextView pizzaBaseOptions = new TextView(this);
                pizzaBaseOptions.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                pizzaBaseOptions.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                pizzaBaseOptions.setText(String.format("%s %s", order[1], order[2]));

                TextView pizzaExtraOptions = new TextView(this);
                pizzaExtraOptions.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                pizzaExtraOptions.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                pizzaExtraOptions.setText(order[3]);


                TextView pizzaPrice = new TextView(this);
                pizzaPrice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                pizzaPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                pizzaPrice.setTypeface(null, Typeface.BOLD);
                pizzaPrice.setAllCaps(true);
                pizzaPrice.setText(order[4]);

                LinearLayout infoBody = new LinearLayout(this);
                infoBody.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100));
                infoBody.setOrientation(LinearLayout.VERTICAL);

                infoBody.addView(pizzaLabel);
                infoBody.addView(pizzaBaseOptions);
                infoBody.addView(pizzaExtraOptions);
                infoBody.addView(pizzaPrice);

                Button pizzaRemove = new Button(this);
                pizzaRemove.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                pizzaRemove.setText(R.string.btnDelete);
                pizzaRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                pizzaRemove.setTypeface(null, Typeface.BOLD);
                pizzaRemove.setTextColor(getResources().getColor(R.color.AntiqueWhite));
                pizzaRemove.setOnClickListener( new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        _EXTRA_orders.remove(cnt);
                        Intent thisIntent = getIntent();
                        finish();
                        startActivity(thisIntent);
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pizzaRemove.setBackground(getResources().getDrawable(R.drawable.btn));
                }

                LinearLayout panelBody = new LinearLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,0,0,20);
                panelBody.setLayoutParams(lp);
                panelBody.setOrientation(LinearLayout.HORIZONTAL);

                panelBody.addView(infoBody);
                panelBody.addView(pizzaRemove);

                LinearLayout placeholder = findViewById(R.id.placeholder);
                placeholder.addView(panelBody);

                ++counter;
            }
        }else{


        }
    }

    private void processExtra(){
        if( getIntent().getSerializableExtra("_orders") != null ){
            _EXTRA_orders = (ArrayList<String[]>) getIntent().getSerializableExtra("_orders");
        }
    }
}
