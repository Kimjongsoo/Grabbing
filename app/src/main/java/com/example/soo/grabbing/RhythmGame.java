package com.example.soo.grabbing;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static android.graphics.BitmapFactory.decodeResource;
import static com.example.soo.grabbing.MainActivity.a;

public class RhythmGame extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private int greenX,greenY, greenSpeed=20;
    private Paint greenPaint =new Paint();
    private int fishX=10;
    private int fishY;
    private Handler handler=new Handler();
    private final  static long Interval = 30;
//    DisplayMetrics dm;
//    public int dwidth;
//    public int dheight;
private BluetoothSPP bt;
    int i=0;

//    String[] a=new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity main = (MainActivity)getApplicationContext();

/////

        final ViewEx viewEx = new ViewEx(this);
        setContentView(viewEx);
//        dm = getApplicationContext().getResources().getDisplayMetrics();
//        dwidth = dm.widthPixels;
//        dheight = dm.heightPixels;
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        viewEx.invalidate();
                    }
                });
            }
        },0,Interval);
//        setContentView(R.layout.activity_rhythm_game);


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
    //raw 에 있는 gitan 을 실행하는 uri






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
class ViewEx extends View
{
    private int canvasWidth, canvasHeight;
    private int greenX,greenY, greenSpeed=15;
    private Paint greenPaint =new Paint();
    private Paint condition=new Paint();
    private Paint rnqns=new Paint();
    private Paint scorePaint=new Paint();
    private Paint text=new Paint();
    private int fishX=10;
    private int fishY;
    Bitmap icon=BitmapFactory.decodeResource(getResources(),R.drawable.sw);
    Bitmap background=BitmapFactory.decodeResource(getResources(),R.drawable.background);
    Bitmap twodu=BitmapFactory.decodeResource(getResources(),R.drawable.twodu);
    Bitmap thrdu=BitmapFactory.decodeResource(getResources(),R.drawable.thrdu);
    Bitmap rktma=BitmapFactory.decodeResource(getResources(),R.drawable.rktma);
    Bitmap memory;

    private Bitmap life[] = new Bitmap[2];
    private int score, lifeCounter,scorecheck;
    private boolean touch=false;
//    private RhythmGame rhythmGame;
    private boolean once=true;
    int checkcheck =5;
    int touchx,touchy;


    public ViewEx(Context context)
    {
        super(context);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        condition.setColor(Color.RED);
        condition.setAntiAlias(false);

        rnqns.setColor(Color.YELLOW);
        rnqns.setAntiAlias(false);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        text.setColor(Color.BLACK);
        text.setTextSize(30);
        text.setAntiAlias(true);



        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heartsa);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_greya);
        score=0;
        scorecheck=0;

        lifeCounter =3;




        Random ran=new Random();
        int rnum=ran.nextInt(3);
        

        if(rnum==0) {
            greenX = 115;
            memory=twodu;
            checkcheck=0;
        }
        else if(rnum==1) {
            greenX = 475;
            memory=thrdu;
            checkcheck=1;
        }
        else if(rnum==2) {
            greenX = 835;
            memory=rktma;
            checkcheck=2;
        }
        //메인엑티비티에서 변수참고하기
//        MainActivity main = (MainActivity)getApplicationContext;

//       Log.e("senser","Ax="+main.a[0]+" Ay="+main.a[1]+" Az="+main.a[2]+"\n"+"온도="+main.a[3]+"\n"+"Gx="+main.a[4]+" Gy="+main.a[5]+" Gz="+main.a[6] +"\n"+"Pitch="+main.a[7]+" Roll="+main.a[8]+" Yaw="+main.a[9]);
    //어깨  roll값 하락  -70보다 아래  acZ값 상승 예상 0>
//       if( Integer.valueOf(main.a[8])<-70 && Integer.valueOf(main.a[2])>3800){}
    //이두 roll값 상승 70보다 위 acZ 하락 예상 0보다 아래
//        if( Integer.valueOf(main.a[8])>70 && Integer.valueOf(main.a[2])<-1000){}
//삼두 yaw값 하락 y 값 하락인가 그럴거임 헷갈리니까 그냥 Gz값 증가
//        if( Integer.valueOf(main.a[9])>2000 && Integer.valueOf(main.a[1])<-3000){}
    //checkcheck대신에 쓰면 될거같음
}
    protected void onDraw(Canvas canvas) {
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        super.onDraw(canvas);
        canvas.drawBitmap(background,0,0,null);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();
        Log.e("hihi",String.valueOf(canvasWidth)+"aaa"+String.valueOf(canvasHeight));
        canvas.drawText("Ax="+a[0]+" Ay="+a[1]+" Az="+a[2]+"\n"+"온도="+a[3]+"\n"+"Gx="+a[4]+" Gy="+a[5]+" Gz="+a[6] +"\n"+"Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9], 20, 500, text);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        greenY=greenY +greenSpeed;
        if(greenY >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            greenY=0;
//
//            int random=(int)Math.floor(Math.random() *(canvasWidth));
//            int random3_1=(int)((Math.random() *(canvasWidth))+1)/3;
//            int random3_2=(int)(Math.floor(Math.random() *(canvasWidth)))*2/3;

            Random ran=new Random();
            int rnum=ran.nextInt(3);

            if(rnum==0) {
                greenX = (int) canvasWidth / 6 - icon.getWidth() / 2;
                memory=twodu;
                checkcheck=0;
            }
            else if(rnum==1) {
                greenX = (int) canvasWidth / 2 - icon.getWidth() / 2;
                memory=thrdu;
                checkcheck=1;
            }
            else if(rnum==2) {
                greenX = (int) canvasWidth * 5 / 6 - icon.getWidth() / 2;
                memory=rktma;
                checkcheck=2;
            }
            Log.e("check",(String.valueOf((int)canvasWidth/6-icon.getWidth()/2))+"."+(String.valueOf((int)canvasWidth/2-icon.getWidth()/2))+"."+(String.valueOf((int)canvasWidth*5/6-icon.getWidth()/2)));

//            condition.setAlpha(255);
            once=true;

            if (lifeCounter==0)
            {
                Toast.makeText(getContext(),"Game over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent =new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);
                getContext().startActivity(gameOverIntent);

            }
        }
        canvas.drawBitmap(memory,greenX,greenY,greenPaint);
        canvas.drawLine(50,canvasHeight-300,canvasWidth-50,canvasHeight-300,greenPaint);
        canvas.drawLine(50,canvasHeight-600,canvasWidth-50,canvasHeight-600,greenPaint);

        canvas.drawLine((int)canvasWidth/3,0,(int)canvasWidth/3,canvasHeight,rnqns);
        canvas.drawLine((int)canvasWidth*2/3,0,(int)canvasWidth*2/3,canvasHeight,rnqns);



//        if (touch)
//        {
////            Log.e("check1",String.valueOf(condition.getAlpha()));
//            if(once==true) {
//                condition.setColor(Color.RED);
//                canvas.drawCircle(50, canvasHeight - 650, 55, condition);
////
////                if (hitrhythmChecker(greenX, greenY)) {
////                    condition.setColor(Color.BLACK);
////                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
////                    canvas.drawText("Good",50,canvasHeight - 650,condition);
////                    condition.setTextSize(200);
////                    score = score + 10;
//////                    condition.setAlpha(50);
//////                    Log.e("check2",String.valueOf(condition.getAlpha()));
////                    once=false;
////                }
//                if (Checkexersize()==0&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_2du(touchx,touchy)){
//                    condition.setColor(Color.BLACK);
//                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
//                    canvas.drawText("Good",50,canvasHeight - 650,condition);
//                    condition.setTextSize(200);
//                    score = score + 10;
////                    condition.setAlpha(50);
////                    Log.e("check2",String.valueOf(condition.getAlpha()));
//                    once=false;
//                }
//                if (Checkexersize()==1&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_3du(touchx,touchy)){
//                    condition.setColor(Color.BLACK);
//                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
//                    canvas.drawText("Good",50,canvasHeight - 650,condition);
//                    condition.setTextSize(200);
//                    score = score + 10;
////                    condition.setAlpha(50);
////                    Log.e("check2",String.valueOf(condition.getAlpha()));
//                    once=false;
//                }
//                if (Checkexersize()==2&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_rktma(touchx,touchy)){
//                    condition.setColor(Color.BLACK);
//                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
//                    canvas.drawText("Good",50,canvasHeight - 650,condition);
//                    condition.setTextSize(200);
//                    score = score + 10;
////                    condition.setAlpha(50);
////                    Log.e("check2",String.valueOf(condition.getAlpha()));
//                    once=false;
//                }
//
//            }
//            touch=false;
//        }
//        else {
//            condition.setColor(Color.RED);
//            canvas.drawCircle(50,canvasHeight-650,50,condition);
//            touch =false;
//        }

//            Log.e("check1",String.valueOf(condition.getAlpha()));
            if(once==true) {
                condition.setColor(Color.RED);
                canvas.drawCircle(50, canvasHeight - 650, 55, condition);

                if (Checkexersize()==0&&hitrhythmChecker_2du(greenX,greenY)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }
                if (Checkexersize()==1&&hitrhythmChecker_3du(greenX,greenY)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }
                if (Checkexersize()==2&&hitrhythmChecker_rktma(greenX,greenY)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }

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


    public boolean hitrhythmChecker(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_2du(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && x<(int)canvasWidth/3){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_3du(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && (int)canvasWidth/3<=x&& x<(int)canvasWidth*2/3){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_rktma(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && x>(int)canvasWidth*2/3){
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

    public int Checkexersize(){
        MainActivity main=new MainActivity();
Log.e("Checkexersize","okok");
Log.e("Checkexersize1","Ax="+a[0]+" Ay="+a[1]+" Az="+a[2]+"\n"+"온도="+a[3]+"\n"+"Gx="+a[4]+" Gy="+a[5]+" Gz="+a[6] +"\n"+"Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9]);
        //이두 roll값 40이상 pitch +-45 이하
       if( Float.valueOf(a[8])<100&&Float.valueOf(a[8])>20 &&Float.valueOf(a[7])>-45&&Float.valueOf(a[7])<45&& Float.valueOf(a[1])>1500){
           return 0;
       }
        //어깨 roll-180 -160 140 180 pitch +- 45
        if( -180<Float.valueOf(a[8])&&Float.valueOf(a[8])<-160&& Float.valueOf(a[7])>-45&&Float.valueOf(a[7])<45|| 130<Float.valueOf(a[8])&&Float.valueOf(a[8])<180&& Float.valueOf(a[7])>-45&&Float.valueOf(a[7])<45){
           return 1;
        }
        //삼두 pitch -50 <  0    roll 50 150
        if(true){
//            Float.valueOf(a[7])>-50&&Float.valueOf(a[7])<0&& Float.valueOf(a[7])<-40 && 50<Float.valueOf(a[8])&&Float.valueOf(a[8])<150
           return 2;
        }
        else return 3;
        //checkcheck대신에 쓰면 될거같음
    }

}


