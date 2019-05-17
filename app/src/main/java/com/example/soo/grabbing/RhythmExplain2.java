package com.example.soo.grabbing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RhythmExplain2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhythm_explain2);

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

        ImageButton next =(ImageButton)findViewById(R.id.explain4);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RhythmExplain1.class);
                startActivity(intent);
            }
        });

        ImageButton next1 =(ImageButton)findViewById(R.id.explain5);
        next1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RhythmExplain3.class);
                startActivity(intent);
            }
        });


    }

}
