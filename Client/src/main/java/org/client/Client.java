package org.client;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final int MAXBUFFERSIZE = 1024;
    private static final int SERVER_PORT = 9351;
    InputStream in;
    OutputStream out;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {

        Socket socket = null;
        Packet sendPacket = null;
        Packet receivePacket;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", SERVER_PORT);
            in = socket.getInputStream();
            out = socket.getOutputStream();

            // ---------------이름 입력------------------
            String name;
            do{
                System.out.print("Please enter your name : ");
                name = scanner.nextLine();
                if(name.trim().isEmpty()){
                    System.out.println("Name connat be empty. Please enter your name again.");
                } else{
                    sendPacket = new Packet(ClientPacketType.CONNECT, "", name);
                    if(!checkNameAvailable(sendPacket)){
                        System.out.println("Name already exists. Please enter your name again.");
                    }
                }
            } while(!checkNameAvailable(sendPacket));



//            String name = scanner.nextLine();
//            sendPacket = new Packet(PacketType.CONNECT, "", name);
            Thread clientThread = new ClientThread(socket, sendPacket);
            clientThread.start();


            while (true) {
                byte[] buffer = new byte[MAXBUFFERSIZE];
                int length = in.read(buffer);
                if (length > 0) {
                    receivePacket = new PacketConversion(buffer).byteToPacket();
                    packetCheck(receivePacket); //Server나 CLient 타입이면 대화형식 바꿈
                    if (receivePacket.getHeader().getType() == ClientPacketType.DISCONNECT) { // 디스커넥트해야 될 스레드는 braek
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
            System.out.println("error d");

            System.out.println("[IOException]");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("error e");

                System.out.println("[IOException]");
            }
        }
        System.out.println("[End Chat room]");
    }
    private void packetCheck(Packet packet){
        if (packet.getHeader().getType() == ClientPacketType.SERVER || packet.getHeader().getType() == ClientPacketType.CLIENT){
            String inputName = packet.getBody().getNickname();
            String inputMsg = "["+ inputName +"] : " + packet.getBody().getMessage();
            System.out.println(inputMsg);
        }
    }

    private boolean checkNameAvailable(Packet packet){ //이름 패킷을 먼저 서버에 보내고 중복 확인.
        byte[] data = new ByteConversion(packet).packetToByte();
        if(packet.getBody().getNickname().trim().isEmpty()){
            return false;
        }
        try{
            out.write(data);
            out.flush();
            byte[] buffer = new byte[MAXBUFFERSIZE];
            int length = in.read(buffer);
            if(length > 0){
                Packet responsePacket = new PacketConversion(buffer).byteToPacket();
                return responsePacket.getHeader().getType() != ClientPacketType.EXCEPTION;
            }
        }catch (IOException e){
            System.out.println("error f");
            System.out.println("[IOException]");
        }
        return false;
    }


}

