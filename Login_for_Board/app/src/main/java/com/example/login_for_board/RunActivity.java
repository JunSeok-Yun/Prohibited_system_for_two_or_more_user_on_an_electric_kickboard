package com.example.login_for_board;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.TimerTask;

import static android.os.SystemClock.sleep;

public class RunActivity extends AppCompatActivity {

    TextView tv_id; // 사용자 id 출력 text view
    TextView recieveText; // 서버로부터 받아온 메세지 출력 text view
    TextView ip_text;
    TextView port_tx;

    Button btn_logout; // 로그아웃 버튼
    Button btn_ride; // 탑승 버튼
    Socket socket;
    PrintWriter sendWriter;
    private String HOST_IP = "192.168.137.202";
    private int port = 50000;
    private String data;

    @Override protected void onStop(){
        super.onStop();
        try {
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){ // 앱 시작시 초기화 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitity_run);

        tv_id = findViewById(R.id.tv_ID);
        recieveText = findViewById(R.id.revTextView);
        btn_logout = findViewById(R.id.out_button);
        btn_ride = findViewById(R.id.ride_button);



        Intent intent = getIntent();
        String userID = intent.getStringExtra("ID");
        int userWeight = intent.getIntExtra("weight",50);
        String U_W = String.valueOf(userWeight); // 문자열로 형변환

        tv_id.setText(""+userID);

        //탑승 버튼을 눌렀을 때
        btn_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyClientTask myClientTask = new MyClientTask("192.168.137.202",50000,U_W);
                myClientTask.execute();
            }
        });




        //로그아웃 버튼을 눌렀을때
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder ad = new AlertDialog.Builder(RunActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("로그아웃");
                ad.setMessage("로그아웃 하시겠습니까?");

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Intent intent = new Intent(RunActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
                ad.show();


            }
        });

    }

    public class MyClientTask extends AsyncTask<Void,Void,Void>
    {
        String dstAddress;
        int dstport;
        String response = "";
        String myMessage = "";

        MyClientTask(String addr, int port, String message)
        {
            dstAddress = addr;
            dstport = port;
            myMessage = message;

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            myMessage = myMessage.toString();
            try{
                socket = new Socket(dstAddress,dstport);
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buf = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                while((bytesRead = inputStream.read(buf)) != -1)
                {
                    response = "서버의 응답: " ;

                    //byteArrayOutputStream.fill(buf, (byte)0);
                    byteArrayOutputStream.write(buf,0,bytesRead);
                    response = byteArrayOutputStream.toString("UTF-8");
                    recieveText.setText(response);
                    sleep(1000);
                    recieveText.setText(null);
                    byteArrayOutputStream.reset();


                }

            } catch (UnsupportedEncodingException e) {
                response = "UnknownHostException: " + e.toString();
            } catch (UnknownHostException e) {
               response = "IOException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (socket != null){
                    try {
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            return null;

        }

        @Override
        protected void  onPostExecute(Void result)
        {
            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }

}