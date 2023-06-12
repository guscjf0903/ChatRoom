package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {
    Socket socket = null;
    String name;

    Scanner scanner = new Scanner(System.in);

    public ClientThread(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }
    @Override
    public void run() {
        try {
            PrintStream out = new PrintStream(socket.getOutputStream()); // 서버에게 보내는 메세지
            out.println(name); //처음 1회 이름 전송
            out.flush();
            while (true) { //채팅방 시작.
                String outputMsg = scanner.nextLine();
                out.println(outputMsg);
                out.flush();
                if ("quit".equals(outputMsg)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
