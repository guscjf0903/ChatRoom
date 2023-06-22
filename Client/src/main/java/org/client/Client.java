package org.client;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        Socket socket = null;
        ObjectInputStream in;
        Packet sendPacket = null;
        Packet receivePacket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 8000);
            System.out.println("[Server connection successful]");
            System.out.print("Please enter your name : ");
            String name = scanner.nextLine();
            sendPacket = new Packet(new PacketHeader(name), new PacketBody());
            Thread clientThread = new ClientThread(socket, sendPacket);
            clientThread.start();

            in = new ObjectInputStream(socket.getInputStream()); // 서버에서 온 메세지
            while (true) {
                receivePacket = (Packet) in.readObject();
                if (receivePacket != null) {
                    String inputMsg = receivePacket.getBody().getMessage();
                    String inputname = receivePacket.getHeader().getSender();
                    if ((inputname + " has left the chatroom.").equals(inputMsg)) break; //서버에서 보낸 나가기가 뜬다면 while문 탈출
                    System.out.println("From " + inputname + ": " + inputMsg); //서버에서 보낸 메세지 읽기
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[IOException]");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[End Chat room]");
    }
}