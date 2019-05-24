package com.example.soo.grabbing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AvoidGameExplain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avoid_game_explain);



        Button avoidbtn=(Button)findViewById(R.id.avoid_start);
        avoidbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AvoidGame.class);
                startActivity(intent);
            }
        });

        Button maingo=(Button)findViewById(R.id.avoidgmaeexplain_to_mainpage);
        maingo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
