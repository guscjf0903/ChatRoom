package org.client;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final int MAXBUFFERSIZE = 1024;
    private static final int SERVER_PORT = 9351;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {

        Socket socket = null;
        InputStream in;
        Packet sendPacket;
        Packet receivePacket;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", SERVER_PORT);
            System.out.print("Please enter your name : ");
            String name = scanner.nextLine();
            sendPacket = new Packet(PacketType.CONNECT, "", name);
            Thread clientThread = new ClientThread(socket, sendPacket);
            clientThread.start();


            in = socket.getInputStream();
            while (true) {
                byte[] buffer = new byte[MAXBUFFERSIZE];
                int length = in.read(buffer);
                if (length > 0) {
                    receivePacket = new PacketConversion(buffer).byteToPacket();
                    packetCheck(receivePacket); //Server나 CLient 타입이면 대화형식 바꿈
                    if (receivePacket.getHeader().getType() == PacketType.DISCONNECT) { // 디스커넥트해야 될 스레드는 braek
                        if(receivePacket.getBody().getNickname().equals(name)){
                            break;
                        }
                        String inputMsg = receivePacket.getBody().getMessage();
                        String inputname = "SERVER";
                        System.out.println("[" + inputname + "] : " + inputMsg);
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
                System.out.println("[IOException]");
            }
        }
        System.out.println("[End Chat room]");
    }
    private void packetCheck(Packet packet){
        if (packet.getHeader().getType() == PacketType.SERVER || packet.getHeader().getType() == PacketType.CLIENT){
            String inputName = packet.getBody().getNickname();
            String inputMsg = "["+ inputName +"] : " + packet.getBody().getMessage();
            System.out.println(inputMsg);
        }
    }


}

