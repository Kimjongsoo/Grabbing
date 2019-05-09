package com.example.soo.grabbing;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    int i=0;

    String[] a=new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                TextView text=findViewById(R.id.textView);
                text.setText("Ax="+a[0]+" Ay="+a[1]+" Az="+a[2]+"\n"+"온도="+a[3]+"\n"+"Gx="+a[4]+" Gy="+a[5]+" Gz="+a[6] +"\n"+"Pitch="+a[7]+" Roll="+a[8]+" Yaw="+a[9]);
                if(i==9){
                    a[i]=message;
                    i=0;
                }
                else {
                    a[i] = message;
                    i++;

                }




//                Log.e("messagecheck","Ax y z ="+a[0]+a[1]+a[2]+"온도= "+a[3]+"Gx y z ="+a[4]+a[5]+a[6]);
//                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                bt.send("1", true);
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
//                bt.send("b", true);
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i=0;
//                bt.send("a", true);
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.send("2", true);
                    bt.disconnect();

                } else {
//                    bt.send("a",true);
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //블루투스 활성화할때 Intent를 통해 안드로이드 시스템에게 요청해서 안드로이드 시스템이 사용자에게 다이얼로그 창을 띄워 사용자가 선택하게 하고 사용자가 선택하면 블루투스 활성화가됌

            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);//개발자에게 다시 전달하는 정수값이라는데 자세한것은 https://developer.android.com/guide/topics/connectivity/bluetooth?hl=ko
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Text", true);
            }
        });

        Button btnSend2 = findViewById(R.id.btnSend2);
        final EditText textinfo =findViewById(R.id.textinfo);

        btnSend2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                bt.send(textinfo.getText().toString(),true);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);

        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

