package org.client;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.share.ToConvert.toPacket;

public class Client {
    static final int MAXBUFFERSIZE = 1024;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {

        Socket socket = null;
        InputStream in = null;
        Packet sendPacket = null;
        Packet receivePacket = null;
        byte[] receiveData = new byte[MAXBUFFERSIZE];
        int nReadSize = 0;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 9351);
            System.out.print("Please enter your name : ");
            String name = scanner.nextLine();
            sendPacket = new Packet(PacketType.CONNECT, "", name);
            Thread clientThread = new ClientThread(socket, sendPacket);
            clientThread.start();


            in = socket.getInputStream();
            while (true) {
                nReadSize = in.read(receiveData);
                receivePacket = toPacket(receiveData);

                if (receivePacket != null) {
                    if (receivePacket.getHeader().getType() == PacketType.SERVER || receivePacket.getHeader().getType() == PacketType.CLIENT) { //서버에서 보낸 메세지 읽기
                        String inputMsg = receivePacket.getBody().getMessage();
                        String inputname = receivePacket.getBody().getNickname();
                        System.out.println("From " + inputname + ": " + inputMsg);
                    } else if (receivePacket.getHeader().getType() == PacketType.DISCONNECT) { // 연결해제 메세지 전송받음
                        if(receivePacket.getBody().getNickname().equals(name)){
                            break;
                        }
                        String inputMsg = receivePacket.getBody().getMessage();
                        String inputname = "SERVER";
                        System.out.println("From " + inputname + ": " + inputMsg);
                    }
                }
            }
        } catch (IOException e) {
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