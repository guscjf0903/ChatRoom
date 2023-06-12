package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                System.out.println("[Client Waiting]");
                socket = serverSocket.accept(); //클라이언트 연결 수락
                //연결이 들어올때마다 새로운 소켓 생성
                //클라이언트가 접속하면 새로운 스레드 생성.
                ServerThread ServerThread = new ServerThread(socket);
                ServerThread.start();
            }
            } catch(IOException e) {
                e.printStackTrace();
            }finally {
            if(serverSocket != null){ //서버가 null이면 서버종료
                try{
                    serverSocket.close();
                    System.out.println("[Server Shutdown]");
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("[Server socket error]");
                }
            }
        }
    }

}