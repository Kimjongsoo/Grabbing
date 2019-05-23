package com.example.soo.grabbing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

import static com.example.soo.grabbing.MainActivity.a;

public class AvoidGame extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private int greenX,greenY, greenSpeed=20;
    private Paint greenPaint =new Paint();
    private int fishX=10;
    private int fishY;
    private Handler handler=new Handler();
    private final  static long Interval = 30;

    private BluetoothSPP bt;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity main = (MainActivity)getApplicationContext();

        final ViewEx2 viewEx2 = new ViewEx2(this);
        setContentView(viewEx2);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        viewEx2.invalidate();
                    }
                });
            }
        },0,Interval);


        checkDangerousPermissions();




        mMediaPlayer= MediaPlayer.create(this,R.raw.blue_moon);
        mMediaPlayer.setVolume(0.8f,0.8f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

    }


    final int REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA = 1;

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);

        }
    }//외부메모리 확인되지 않을시 멈추는거


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
            switch (requestCode) {
                case REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA:
                    playAudioFromRawResource();
                    break;
            }
        } else { // permission was denied
            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
        }
    }//외부메모리에서 allow누르면 바로 노래 재생 권한설정


    private void playAudioFromRawResource(){

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + "blue_moon");
        try {
            playAudio(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }//오류 에러 캐치 구문

    }


    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onStop() {
        super.onStop();
        killMediaPlayer();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    //비디오 출력
    private void playAudio(Uri uri) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(getApplicationContext(), uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }
}
class ViewEx2 extends View
{
    private int canvasWidth, canvasHeight;
    private int greenX,greenY, greenSpeed=25;
    private Paint greenPaint =new Paint();
    private Paint condition=new Paint();
    private Paint rnqns=new Paint();
    private Paint scorePaint=new Paint();
    private Paint text=new Paint();
    ///////////
    private Paint pilot=new Paint();

    private Paint enemy1=new Paint();
    private Paint enemy2=new Paint();
    private Paint enemy3=new Paint();
    private Paint enemy4=new Paint();
    private Paint enemy5=new Paint();
    private Paint enemy6=new Paint();
    private Paint enemy7=new Paint();
    private int pilotSpeed=3;
    private int pilotX=500;
    private int pilotY=1000;

    private int enemySpeed=20;
    private int enemyX,enemyY;

    private int enemy2Speed=15;
    private int enemy2X,enemy2Y;

    private int enemy3Speed=22;
    private int enemy3X,enemy3Y;

    private int enemy4Speed=17;
    private int enemy4X,enemy4Y;

    private int enemy5Speed=25;
    private int enemy5X,enemy5Y;

    private int enemy6Speed=13;
    private int enemy6X,enemy6Y;

    private int enemy7Speed=14;
    private int enemy7X,enemy7Y;

    private int duration=0;
    private Canvas common_canvas;

    private boolean enemyboolean1=true;
    private boolean enemyboolean2=true;
    private boolean enemyboolean3=true;
    private boolean enemyboolean4=true;
    private boolean enemyboolean5=true;
    private boolean enemyboolean6=true;
    private boolean enemyboolean7=true;



    //////////
    private int fishX=10;
    private int fishY;
    Bitmap icon=BitmapFactory.decodeResource(getResources(),R.drawable.sw);
    Bitmap background_space=BitmapFactory.decodeResource(getResources(),R.drawable.space);

//    Bitmap background=BitmapFactory.decodeResource(getResources(),R.drawable.background);
//    Bitmap twodu=BitmapFactory.decodeResource(getResources(),R.drawable.twodu);
//    Bitmap thrdu=BitmapFactory.decodeResource(getResources(),R.drawable.thrdu);
//    //    Bitmap rktma=BitmapFactory.decodeResource(getResources(),R.drawable.rktma);
//    Bitmap djRo=BitmapFactory.decodeResource(getResources(),R.drawable.djro);
//    Bitmap memory;


    private Bitmap life[] = new Bitmap[2];
    private int score, lifeCounter,scorecheck;
    private boolean touch=false;
    //    private RhythmGame rhythmGame;
    private boolean once=true;
    private boolean green=false;
    int checkcheck =5;
    int touchx,touchy;


    public ViewEx2(Context context) {
        super(context);
//        greenPaint.setColor(Color.GREEN);
//        greenPaint.setAntiAlias(false);
//
//        rnqns.setColor(Color.YELLOW);
//        rnqns.setAntiAlias(false);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        text.setColor(Color.WHITE);
        text.setTextSize(30);
        text.setAntiAlias(true);
/////////
        pilot.setColor(Color.BLACK);

        enemy1.setColor(Color.YELLOW);
        enemy2.setColor(Color.YELLOW);
        enemy3.setColor(Color.YELLOW);
        enemy4.setColor(Color.YELLOW);
        enemy5.setColor(Color.YELLOW);
        enemy6.setColor(Color.YELLOW);
        enemy7.setColor(Color.YELLOW);
/////


        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heartsa);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_greya);
        score = 0;
        scorecheck = 0;

        lifeCounter = 3;


        int random=(int)Math.floor(Math.random() *(1000));
        enemyX=random;

        int random2=(int)Math.floor(Math.random() *(800));
        enemy2X=random2;
    }
    protected void onDraw(Canvas canvas) {
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        super.onDraw(canvas);
        canvas.drawBitmap(background_space,0,0,null);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();
        common_canvas=canvas;
        phase();
        Log.e("hihii",String.valueOf(canvasWidth)+"aaa"+String.valueOf(canvasHeight));
//        canvas.drawText("Ax="+a[0]+" Ay="+a[1]+" Az="+a[2]+"\n"+"온도="+a[3]+"\n"+"Gx="+a[4]+" Gy="+a[5]+" Gz="+a[6] +"\n"+"Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9], 20, 500, text);
        canvas.drawText("Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9], (int)canvasWidth*2/3+6, 300, text);
        text.setTextSize(22);
        RPYsensor();
//        canvas.drawCircle(pilotX,pilotY,30,pilot);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pilot),pilotX,pilotY,pilot);
//        greenY=greenY +greenSpeed;
        enemyY=enemyY +enemySpeed;


        canvas.drawCircle(enemyX,enemyY,25,enemy1);

        enemy2Y=enemy2Y +enemy2Speed;


        canvas.drawCircle(enemy2X,enemy2Y,25,enemy2);


        if(enemyY >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;
            int random=(int)Math.floor(Math.random() *(canvasWidth));
            enemyX=random;
            enemyY=0;

            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            duration++;

        }
        if(enemy2Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy2Y=0;
            int random2=(int)Math.floor(Math.random() *(canvasWidth));
            enemy2X=random2;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }
        if(enemy2Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy2Y=0;
            int random2=(int)Math.floor(Math.random() *(canvasWidth));
            enemy2X=random2;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }

        if(enemy3Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy3Y=0;
            int random3=(int)Math.floor(Math.random() *(canvasWidth));
            enemy3X=random3;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }
        if(enemy4Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy4Y=0;
            int random4=(int)Math.floor(Math.random() *(canvasWidth));
            enemy4X=random4;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }
        if(enemy5Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy5Y=0;
            int random5=(int)Math.floor(Math.random() *(canvasWidth));
            enemy5X=random5;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }
        if(enemy6Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy6Y=0;
            int random6=(int)Math.floor(Math.random() *(canvasWidth));
            enemy6X=random6;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }
        if(enemy7Y >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            enemy7Y=0;
            int random7=(int)Math.floor(Math.random() *(canvasWidth));
            enemy7X=random7;
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);

                getContext().startActivity(gameOverIntent);

            }
            green=false;
            duration++;
        }




        if(once==true) {
                score = score + 10;
                once=false;
            }




        for(int i=0; i<3; i++)
        {
            int x= (int) (730+life[0].getWidth() *1.5 *i);
            int y= 90;

            if(i<lifeCounter)
            {
                canvas.drawBitmap(life[0], x,y,null);

            }
            else {
                canvas.drawBitmap(life[1], x,y,null);
            }
        }
        canvas.drawText("Score : "+score, 20, 150, scorePaint);
    }

    public boolean hitBallChecker(int x,int y)
    {
        if(pilotX <x && x<(pilotX+ BitmapFactory.decodeResource(getResources(),R.drawable.pilot).getWidth()) && pilotY<y && y< (pilotY+ BitmapFactory.decodeResource(getResources(),R.drawable.pilot).getHeight())){
            return true;
        }
        return false;
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            touchx=(int)event.getX();
            touchy=(int)event.getY();
            touch =true;

        }
        return true;
    }
    public void phase(){
        //시간이 지날수록 점점 난이도가 올라가게 구현 장애물 아래로 떨어지면 duration의 값 올라가는데 그 개수의 기준으로 난이도 업
        if(duration>3){
            if(enemyboolean3==true){
                enemy3Y=0;
                int random3=(int)Math.floor(Math.random() *(canvasWidth));
                enemy3X=random3;
                enemyboolean3=false;
            }
            enemy3Y=enemy3Y +enemy2Speed;
            enemy3X=enemy3X +7;
            common_canvas.drawCircle(enemy3X,enemy3Y,25,enemy3);

            if(enemyboolean4==true){
                enemy4Y=0;
                int random4=(int)Math.floor(Math.random() *(canvasWidth));
                enemy4X=random4;
                enemyboolean4=false;
            }
            enemy4Y=enemy4Y +enemy4Speed;
            enemy4X=enemy4X -3;
            common_canvas.drawCircle(enemy4X,enemy4Y,25,enemy4);
        }
        if(duration>5) {
            if(enemyboolean5==true){
                enemy5Y=0;
                int random5=(int)Math.floor(Math.random() *(canvasWidth));
                enemy5X=random5;
                enemyboolean5=false;
            }
            enemy5Y=enemy5Y +enemy4Speed;
            enemy5X=enemy5X -10;
            common_canvas.drawCircle(enemy5X,enemy5Y,25,enemy5);
        }
        if(duration>7){

            if(enemyboolean6==true){
                enemy6X=0;
                int random6=(int)Math.floor(Math.random() *(canvasWidth));
                enemy6Y=random6;
                enemyboolean6=false;
            }
            enemy6Y=enemy6Y -7;
            enemy6X=enemy6X +enemy6Speed;
            common_canvas.drawCircle(enemy6X,enemy6Y,25,enemy6);

        }
        if (duration>11){
            if(enemyboolean7==true){
                enemy7X=canvasWidth;
                int random7=(int)Math.floor(Math.random() *(canvasWidth));
                enemy7Y=random7;
                enemyboolean7=false;
            }
            enemy7Y=enemy7Y +15;
            enemy7X=enemy7X -enemy7Speed;
            common_canvas.drawCircle(enemy7X,enemy7Y,25,enemy7);

        }
    }

    public void RPYsensor(){
    Log.e("Checkexersize1","Ax="+a[0]+" Ay="+a[1]+" Az="+a[2]+"\n"+"온도="+a[3]+"\n"+"Gx="+a[4]+" Gy="+a[5]+" Gz="+a[6] +"\n"+"Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9]);
    //이두 roll값 40이상 80이하 pitch +-45 이하

        if(Float.valueOf(a[7])>0&&Float.valueOf(a[8])>-30&&Float.valueOf(a[8])<30){
            //pitch값 +되면 왼쪽  roll +-30이내
            pilotX=pilotX-pilotSpeed;
        }
        else if(Float.valueOf(a[7])<0&&Float.valueOf(a[8])>-30&&Float.valueOf(a[8])<30){
            //pitch값 -되면 오른쪽
            pilotX=pilotX+pilotSpeed;
        }
        else if(Float.valueOf(a[8])>0&&Float.valueOf(a[7])>-30&&Float.valueOf(a[7])<30){
            //roll값 +되면 아래
            pilotY=pilotY+pilotSpeed;
        }
        else if(Float.valueOf(a[8])<0&&Float.valueOf(a[7])>-30&&Float.valueOf(a[7])<30){
            //roll값 -되면 위
            pilotY=pilotY-pilotSpeed;
        }
        else if(Float.valueOf(a[7])>=30&&Float.valueOf(a[8])<=-30){
            //pitch값 +되면 왼쪽//roll값 -되면 위 ->왼쪽위
            pilotY=pilotY-pilotSpeed;
            pilotX=pilotX-pilotSpeed;
        }
        else if(Float.valueOf(a[7])<=-30&&Float.valueOf(a[8])<=-30){
            //pitch값 -되면 오른쪽//roll값 -되면 위 ->오른쪽위
            pilotY=pilotY-pilotSpeed;
            pilotX=pilotX+pilotSpeed;
        }
        else if(Float.valueOf(a[7])>=30&&Float.valueOf(a[8])>=30){
            //pitch값 +되면 왼쪽//roll값 +되면 아래 ->왼쪽아래
            pilotY=pilotY+pilotSpeed;
            pilotX=pilotX-pilotSpeed;
        }
        else if(Float.valueOf(a[7])<-30&&Float.valueOf(a[8])>30){
            //pitch값 -되면 오른쪽//roll값 +되면 아래 ->오른쪽아래
            pilotY=pilotY+pilotSpeed;
            pilotX=pilotX+pilotSpeed;
        }


}

}