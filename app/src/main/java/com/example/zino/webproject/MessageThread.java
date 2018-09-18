package com.example.zino.webproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/* 메세지를 주고 받는 역할의 객체*/
public class MessageThread extends Thread{
    BufferedReader buffr;/*버퍼처리된 문자기반의 입력스트림*/
    BufferedWriter buffw;/*버퍼처리된 문자기반의 출력스트림*/
    Socket socket;/*접속에 성공한 소켓을 넘겨받자..왜?? 스트림을 뽑아 쓰려고...*/
    boolean flag=true;
    Handler handler;

    public MessageThread(Socket socket , Handler handler) {
        this.socket=socket;
        this.handler=handler;

        try {
            buffr = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            buffw= new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*메세지 전송 메서드 : 입*/
    public void send(String msg){
        try {
            buffw.write(msg+"\n");
            buffw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    };


    /*메세지 청취 메서드: 귀*/
    public void listen(){
        try {
            String msg=buffr.readLine();
            /*핸들러에게 요청을 하면서 청취했던 메세지를 같이 보내주자*/
            /*메세지를 담는 객체*/
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            message.setData(bundle);

            handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (flag){
            listen();
        }
    }
}
