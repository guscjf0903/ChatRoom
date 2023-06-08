package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    public void start(){
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(8000); // 포트번호와 서버 소켓 생성
            while(true) {
                System.out.println("[클라이언트 연결대기]");
                socket = serverSocket.accept(); //클라이언트 연결 수락
                //연결이 들어올때마다 새로운 소켓 생성
                //클라이언트가 접속하면 새로운 스레드 생성.
                ReceiveThread receiveThread = new ReceiveThread(socket);
                receiveThread.start();
            }
            } catch(IOException e) {
                e.printStackTrace();
            }finally {
            if(serverSocket != null){ //서버가 null이면 서버종료
                try{
                    serverSocket.close();
                    System.out.println("[서버종료]");
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("[서버소켓통신에러]");
                }
            }
        }
    }

    class ReceiveThread extends Thread{
        static List<PrintWriter> list = //PrintWriter는 다양한 자료형을 출력하기 위한 메서드, 다중 클라이언트에게 메세지를 전송하는데 사용
                Collections.synchronizedList(new ArrayList<PrintWriter>());
        Socket socket = null;
        BufferedReader in = null; //문자열 기반의 입력을 처라히가 위한 클래스. 주로 텍스트파일이나 네트워크에서의 입력읽어오는데 사용
        PrintWriter out = null; //문자열 기반의 출력을 처리하기 위한 클래스. 주로 텍스트파일이나 네트워크로의 출력 담당.
        //클라이언트 객체 한명한명

        public ReceiveThread(Socket socket){
            this.socket = socket;
            try{
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                list.add(out);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            String name = "";
            try{
                name = in.readLine(); // 최초1회는 클라이언트 이름 수신
                System.out.println("["+ name + " 연결]");
                sendAll("["+ name +"] 님이 들어오셨습니다."); //모두에게 보내기

                while(in != null){ // 다음 대화내용 받아내기.
                    String inputMsg = in.readLine();
                    if("quit".equals(inputMsg)) break; //quit가 들어오면 while문 벗어남
                    sendAll(name + ">>" + inputMsg); //대화내용 출력
                }
            }catch (IOException e){
                System.out.println("[" + name + "접속 끊김]");
            }finally {
                sendAll("["+name+"]님이 나가셨습니다.");
                list.remove(out);
                try{
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            System.out.println("[" + name + " 연결종료]");
        }
        private void sendAll(String s){ // 모두에게 전송
            for(PrintWriter out : list){
                out.println(s);
                out.flush(); //출력 스트림에 대기중인 모든 데이터를 강제로 출력하는 메서드. (버퍼를 비워줌)
            }
        }

    }



}