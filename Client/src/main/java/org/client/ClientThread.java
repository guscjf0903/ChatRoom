package org.client;

import org.share.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

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
            byte[] data = new ByteConversion(packet).packetToByte(); // 객체에서 byte로 변환
            String name = packet.getBody().getNickname();
            OutputStream out = socket.getOutputStream(); // 서버에게 변환한 byte 전달을 위한 스트림.
            out.write(data); //처음 1회 이름과 커넥트타입 전송
            out.flush();
            while (true) { //채팅방 시작.
                message = scanner.nextLine();
                if (message != null) {
                    packet = new Packet(ClientPacketType.CLIENT, message, name);
                    data = new ByteConversion(packet).packetToByte();
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