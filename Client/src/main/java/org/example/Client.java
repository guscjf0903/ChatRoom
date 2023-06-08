package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    public void start(){
        Socket socket = null;
        BufferedReader in = null;
        try{
            socket = new Socket("localhost",8000);
            System.out.println("[서버와 연결되었습니다.]");

            String name = "user" + (int)(Math.random()*10);
            Thread sendThread = new SendThread(socket, name);
            sendThread.start();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(in != null){
                String inputMsg = in.readLine(); //메세지 읽기
                if(("["+name+"]님이 나가셨습니다.").equals(inputMsg)) break; //서버에서 보낸 나가기가 뜬다면 while문 탈출
                System.out.println("From:" + inputMsg);
            }
        }catch (IOException e){
            System.out.println("[서버 접속 끊김]");
        }finally {
            try{
                if(socket != null){
                    socket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("[연결 종료]");
    }

    class SendThread extends Thread{
        Socket socket = null;
        String name;

        Scanner scanner = new Scanner(System.in);
        public SendThread(Socket socket, String name){
            this.socket = socket;
            this.name = name;
        }
        @Override
        public void run(){
            try{
                //처음 1회 이름 전송
                PrintStream out = new PrintStream(socket.getOutputStream());
                out.println(name);
                out.flush();
                while (true) { //채팅방 시작.
                    String outputMsg = scanner.nextLine();
                    out.println(outputMsg);
                    out.flush();
                    if("quit".equals(outputMsg)) break;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}