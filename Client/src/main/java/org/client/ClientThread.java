package org.client;

import org.share.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {
    Socket socket = null;
    Packet packet;

    Scanner scanner = new Scanner(System.in);

    public ClientThread(Socket socket, Packet packet) {
        this.socket = socket;
        this.packet = packet;
    }
    @Override
    public void run() {
        try {
            String message = null;
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // 서버에게 객체 자체를 전송하기 위한 메서드
            out.writeObject(packet); //처음 1회 이름만 전송
            out.flush();
            while (true) { //채팅방 시작.
                message = scanner.nextLine();
                packet.getBody().setMessage(message);
                out.writeObject(packet);
                out.flush();
                if ("quit".equals(message)){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
