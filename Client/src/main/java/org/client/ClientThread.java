package org.client;

import org.share.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {
    Socket socket = null;
    SendType sendpacket;

    Scanner scanner = new Scanner(System.in);

    public ClientThread(Socket socket, SendType sendType) {
        this.socket = socket;
        this.sendpacket = sendType;
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // 서버에게 객체 자체를 전송하기 위한 메서드
            out.writeObject(sendpacket.returnname()); //처음 1회 이름만 전송
            out.flush();
            while (true) { //채팅방 시작.
                sendpacket.makeMessage(scanner.nextLine());
                out.writeObject(sendpacket.sendMessage());
                out.flush();
                if ("quit".equals(sendpacket.sendMessage())){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
