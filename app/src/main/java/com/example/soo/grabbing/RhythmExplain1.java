package com.example.soo.grabbing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RhythmExplain1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhythm_explain1);

        Button rhybtn=(Button)findViewById(R.id.rhythm_start);
        rhybtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RhythmGame.class);
                startActivity(intent);
            }
        });

        Button rhybtn1=(Button)findViewById(R.id.mainpage);
        rhybtn1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton next =(ImageButton)findViewById(R.id.explain2);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RhythmExplain.class);
                startActivity(intent);
            }
        });

        ImageButton next1 =(ImageButton)findViewById(R.id.explain3);
        next1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RhythmExplain2.class);
                startActivity(intent);
            }
        });


    }

}
