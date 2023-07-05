package org.server;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class ServerThread extends Thread {
    static final int MAXBUFFERSIZE = 1024;
    static Map<String, OutputStream> clientMap = Collections.synchronizedMap(new HashMap<String, OutputStream>());

    private Socket socket;
    private InputStream in; //문자열 기반의 입력을 처라히가 위한 클래스. 주로 텍스트파일이나 네트워크에서의 입력읽어오는데 사용
    private OutputStream out; //문자열 기반의 출력을 처리하기 위한 클래스. 주로 텍스트파일이나 네트워크로의 출력 담당.
    //클라이언트 객체 한명한명
    private String clientName;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            out = socket.getOutputStream(); // 클라에게 보내는 메세지
            in = socket.getInputStream(); //클라에서 오는 메세지
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String name = null;
        try {

            connectClient();

            while (true) { // 다음 대화내용 받아내기.
                byte[] buffer = new byte[MAXBUFFERSIZE];
                int length = in.read(buffer);
                Packet packet = null;
                if (length > 0) {
                    packet = new PacketConversion(buffer).byteToPacket();
                    sendAllMessage(packet.getHeader().getType(),packet.getBody().getMessage(),packet.getBody().getNickname());
                }
                name = packet.getBody().getNickname();
                String inputMsg = packet.getBody().getMessage();
                if ("quit".equals(inputMsg)){
                    sendAllMessage(ClientPacketType.DISCONNECT,"",name);
                    break; //quit가 들어오면 while문 벗어남
                }
            }
        } catch (IOException e) {
            System.out.println("[" + name + "Disconnected]");
        } finally {
            clientMap.remove(name);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " Disconnected]"); //서버관리자가 누군가 나가면 볼수있는 메세지
    }

    private void sendAllMessage(ClientPacketType clientPacketType, String message, String nickname) {
        Packet packet = new Packet(clientPacketType,message,nickname);
        byte[] data = new ByteConversion(packet).packetToByte();
        try {
            for (Map.Entry<String, OutputStream> entry : clientMap.entrySet()) {
                String receiverName = entry.getKey();
                OutputStream clientStream = entry.getValue();

                // 메시지를 보낸 클라이언트와 수신 클라이언트의 이름이 다를 때에만 메시지를 전송합니다.
                if (!receiverName.equals(nickname) || clientPacketType == ClientPacketType.DISCONNECT) {
                    try {
                        clientStream.write(data);
                        clientStream.flush();
                    } catch (IOException e) {
                        // 클라이언트와의 연결이 끊어진 경우, 해당 클라이언트를 제거합니다.
                        clientMap.remove(receiverName);
                        System.out.println("[" + receiverName + " Disconnected]");
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
    private void connectClient() throws IOException{
        byte[] buffer = new byte[MAXBUFFERSIZE];
        int length = in.read(buffer);
        if(length > 0){
            Packet packet = new PacketConversion(buffer).byteToPacket();
            if(packet.getHeader().getType() == ClientPacketType.CONNECT){
                clientName = packet.getBody().getNickname();
                if(isDuplicateName(clientName)){
                    sendErrorMessage("Name already exists. Please enter your name again.", "TEST"); //-----------------------
                    connectClient();
                }
                System.out.println("[" + clientName + " Connected]");
                clientMap.put(clientName, out);
                sendAllMessage(ClientPacketType.SERVER,clientName + " has entered.","SERVER");
            }
        }
    }
    private boolean isDuplicateName(String name){
        return clientMap.containsKey(name);
    }
    private void sendErrorMessage(String message, String nickname){
        Packet packet = new Packet(ClientPacketType.EXCEPTION, message, nickname);
        byte[] data = new ByteConversion(packet).packetToByte();
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}