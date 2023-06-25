package org.client;

import org.share.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import static org.share.ToConvert.toByteArray;

public class ClientThread extends Thread {
    Socket socket = null;
    Packet packet;

    Scanner scanner = new Scanner(System.in);

    public ClientThread(Socket socket, Packet packet) {
        this.socket = socket;
        this.packet = packet; // 처음 커넥트 타입을 패킷.
    }

    @Override
    public void run() {
        try {
            String message = null;
            byte[] data = toByteArray(packet);
            String name = packet.getBody().getNickname();
            OutputStream out = socket.getOutputStream(); // 서버에게 객체 자체를 전송하기 위한 메서드
            out.write(data); //처음 1회 이름과 커넥트타입 전송
            out.flush();
            while (true) { //채팅방 시작.
                message = scanner.nextLine();
                if (message != null) {
                    packet = new Packet(PacketType.CLIENT, message, name);
                    data = toByteArray(packet);
                    out.write(data);
                    out.flush();
                    if ("quit".equals(message)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}