package com.example.chern.poa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons Configuration
        configurePizzaBtn();
        configureExitBtn();
    }

    // Start of Order Pizza Activity
    private void configurePizzaBtn(){
        Button btnPizza = findViewById(R.id.newOrder);
        btnPizza.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, Pizza.class));
            }
        });
    }

    // Exit the App
    private void configureExitBtn(){
        Button btnExit = findViewById(R.id.exit);
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}
