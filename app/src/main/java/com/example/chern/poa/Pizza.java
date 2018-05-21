package com.example.chern.poa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.DialogInterface.*;

public class Pizza extends Activity implements OnClickListener, OnMultiChoiceClickListener{


    AlertDialog alert;

    //Initializing total price
    private float _PizzaBasePrice = 0;
    private float _PizzaToppingsPrice = 0;
    private float _PizzaSizeRate = 1;

    //Instancing components related to pizza
    private String[] pizzas;
    private Spinner pizzaSpinner;
    private ArrayAdapter<String> adptPizzas;
    private TextView pizzaDisplay;

    //Instancing components related to dough
    private String[] doughs;
    private Spinner doughSpinner;
    private ArrayAdapter<String> adptDough;


    /* Instancing components related to size */
    private String[] _Sizes;
    private String[] _SizeRates;
    private int _SizeSelected = 0;

    /* Instancing components related to toppings */
    private String[] _Toppings;
    private String[] _ToppingsPrices;
    private boolean[] _ToppingsSelected;

    /* Instancing prices for pizzas */
    private String[] pizPrices;
    private ArrayAdapter<String> adptPizPrice;

    //List of orders

    // intent values
    private String _INTENT_pizza_name = "Lahma Bi Ajeen";
    private String _INTENT_pizza_dough = "Neapolitan";
    private String _INTENT_pizza_size = "Small";
    private String[] _INTENT_pizza_toppings = null;
    private String _INTENT_pizza_price = "";

    private ArrayList<String[]> _INTENT_orders = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "ACTIVITY HAS BEEN RESUMED", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        /*
        * =====================================================================================
        *           Different Kinds Of Pizza
        * =====================================================================================
        * */

        //Initializing components related to pizza
        pizzaSpinner = findViewById(R.id.pizzaSpinner);
        pizzaSpinner.setPromptId(R.string.pizPrompt);
        pizzaDisplay = findViewById(R.id.pizzaDisplay);


        //Getting pizza string array
        pizzas = getResources().getStringArray(R.array.pizzas);
        pizPrices = getResources().getStringArray(R.array.pizPrices);

        //Setting up adapter with information
        adptPizzas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pizzas);
        adptPizzas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Displaying item inside spinner
        pizzaSpinner.setAdapter(adptPizzas);
        pizzaSpinner.setOnItemSelectedListener( new Spinner.OnItemSelectedListener(){

            @SuppressLint("DefaultLocale")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _INTENT_pizza_name = pizzas[position];

                _PizzaBasePrice = Float.parseFloat(pizPrices[position]);
                // Display price of the current chosen pizza
                pizzaDisplay.setText(String.format("Price: %.2f $", _PizzaBasePrice));
                ChangePrice(_PizzaBasePrice, _PizzaToppingsPrice, _PizzaSizeRate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * =====================================================================================
         *           Kinds Of Dough
         * =====================================================================================
         * */

        //Initializing components related to dough
        doughSpinner = findViewById(R.id.doughSpinner);

        //Getting dough string array
        doughs = getResources().getStringArray(R.array.doughs);

        //Setting up adapter with information
        adptDough = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, doughs);
        adptDough.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Displaying item inside spinner
        doughSpinner.setAdapter(adptDough);
        doughSpinner.setOnItemSelectedListener( new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _INTENT_pizza_dough = doughs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /* Initialize variety of sizes */
        _Sizes = getResources().getStringArray(R.array.sizes);
        _SizeRates = getResources().getStringArray(R.array.sizeRate);


        /* Initialize variety of toppings */
        _Toppings = getResources().getStringArray(R.array.toppings);
        _ToppingsPrices = getResources().getStringArray(R.array.toppingsPrices);
        _ToppingsSelected = new boolean[_Toppings.length];
        ResetToppings();

        SetupSizeBtn();
        SetupToppingsBtn();
        SetupAddPizzaBtn();
    }

    private void SetupSizeBtn(){
        final Button selectBtn = findViewById(R.id.sizes);
        selectBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(Pizza.this);
                db.setCancelable(false);

                db.setTitle(R.string.ttlSize).
                        setPositiveButton(R.string.actionSelect, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView lw = ((AlertDialog)dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                SetPizzaSize(checkedItem);
                                selectBtn.setText(String.valueOf(checkedItem));

                                _INTENT_pizza_size = String.valueOf(checkedItem);
                            }
                        }).
                        setNegativeButton(R.string.actionCancel, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setSingleChoiceItems(_Sizes, _SizeSelected, Pizza.this);

                alert = db.create();
                alert.show();
            }
        });
    }

    private void SetupToppingsBtn(){
        final Button selectBtn = findViewById(R.id.toppings);
        selectBtn.setOnClickListener( new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder db1 = new AlertDialog.Builder(Pizza.this);
                db1.setCancelable(false);
                db1.setTitle(R.string.ttlTopping).
                        setPositiveButton(R.string.actionSelect, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Set toppings for current pizza
                                SetPizzaToppings();
                                int toppingsCounter = 0;
                                for (boolean selected : _ToppingsSelected){ if (selected) ++toppingsCounter; }

                                _INTENT_pizza_toppings = new String[toppingsCounter];
                                int index = 0;
                                for(int i = 0; i < _ToppingsSelected.length; ++i){
                                    if(_ToppingsSelected[i]){
                                        _INTENT_pizza_toppings[index] = _Toppings[i];
                                        ++index;
                                    }
                                }

                                selectBtn.setText("[" + String.valueOf(toppingsCounter) + "]");
                            }
                        }).
                        setNegativeButton(R.string.actionReset, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Reset
                                _INTENT_pizza_toppings = null;
                                ResetToppings();
                                dialog.dismiss();
                            }
                        }).
                        setMultiChoiceItems(_Toppings, _ToppingsSelected, Pizza.this);

                alert = db1.create();
                alert.show();
            }
        });
    }

    private void SetupAddPizzaBtn(){
        // Send data on pizza to next activity
        Button btnAdd = findViewById(R.id.totalP);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Pizza.this, Cart.class);

                String _INTENT_pizza_toppings_string;
                if(_INTENT_pizza_toppings != null){
                    String separator = ", ";
                    StringBuilder tops = new StringBuilder();
                    for(String topping : _INTENT_pizza_toppings){
                        tops.append(separator).append(topping);
                    }
                    _INTENT_pizza_toppings_string = tops.substring(separator.length());
                }else{
                    _INTENT_pizza_toppings_string = getResources().getString(R.string.ttlNoExtraTop);
                }

                _INTENT_orders.add(new String[]{_INTENT_pizza_name, _INTENT_pizza_size, _INTENT_pizza_dough, _INTENT_pizza_toppings_string, _INTENT_pizza_price});
                intent.putExtra("_orders", _INTENT_orders);

                startActivity(intent);
            }
        });
    }

    private void SetPizzaSize(Object checkedItem){
        _SizeSelected = Arrays.asList(_Sizes).indexOf(checkedItem);
        _PizzaSizeRate = Float.parseFloat(_SizeRates[_SizeSelected]);
        ChangePrice(_PizzaBasePrice, _PizzaToppingsPrice, _PizzaSizeRate);
    }

    private void SetPizzaToppings(){
        _PizzaSizeRate = Float.parseFloat(_SizeRates[_SizeSelected]);
        _PizzaToppingsPrice = 0;
        for (int i = 0; i < _ToppingsSelected.length; ++i) {
            if(_ToppingsSelected[i]){
                _PizzaToppingsPrice += Float.parseFloat(_ToppingsPrices[i]);
            }
        }

        ChangePrice(_PizzaBasePrice, _PizzaToppingsPrice, _PizzaSizeRate);
    }

    private void ResetToppings(){
        _PizzaToppingsPrice = 0;
        Button toppingsButton = findViewById(R.id.toppings);
        toppingsButton.setText(R.string.ttlDefaultToppingsCount);
        for (int i = 0; i < _ToppingsSelected.length; i++){
            _ToppingsSelected[i] = false;
        }
        ChangePrice(_PizzaBasePrice, _PizzaToppingsPrice, _PizzaSizeRate);
    }

    private void ChangePrice(float pizzaPrice, float toppingsPrice, float rate){
        float overall = 0.f;
        overall += pizzaPrice;

        overall += toppingsPrice;

        overall *= rate;

        // set price to calculated overall price
        SetOverallPrice(overall);
    }

    @SuppressLint("DefaultLocale")
    private void SetOverallPrice(float pizzaPrice){
        TextView totalP = findViewById(R.id.totalP);
        totalP.setText(String.format("Add Pizza - %.2f $", pizzaPrice));

        _INTENT_pizza_price = String.format("%.2f $", pizzaPrice);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {}

    @Override
    public void onClick(DialogInterface dialog, int which) {}
}
