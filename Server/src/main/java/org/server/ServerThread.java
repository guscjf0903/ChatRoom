package org.server;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static org.share.ToConvert.toByteArray;
import static org.share.ToConvert.toPacket;

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
            sendAllMessage(PacketType.SERVER,clientName + " has entered.","SERVER");
//            nReadSize = in.read(receiveData);// 최초1회는 클라이언트 이름 수신
//            receivePacket = toPacket(receiveData);
//            if (receivePacket.getHeader().getType() == PacketType.CONNECT) {
//                name = receivePacket.getBody().getNickname();
//                System.out.println("[" + name + " Connected]");
//                clientMap.put(name, out);
//                serverPacket = new Packet(PacketType.SERVER, name + " has entered.", "SERVER");
//                sendAll(serverPacket);// 접속했다고 모두에게 보내기.
//            }
            // -----------------------------------------------//
            while (true) { // 다음 대화내용 받아내기.
                byte[] buffer = new byte[MAXBUFFERSIZE];
                int length = in.read(buffer);
                Packet packet = null;
                if (length > 0) {
                    packet = toPacket(buffer);
                    sendAllMessage(packet.getHeader().getType(),packet.getBody().getMessage(),packet.getBody().getNickname());
                }
                name = packet.getBody().getNickname();
                String inputMsg = packet.getBody().getMessage();
                if ("quit".equals(inputMsg)){
                    sendAllMessage(PacketType.DISCONNECT,"",name);
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

    private void sendAllMessage(PacketType packetType, String message, String nickname) {
        Packet packet = new Packet(packetType,message,nickname);
        byte[] data = toByteArray(packet);
        try {
            for (Map.Entry<String, OutputStream> entry : clientMap.entrySet()) {
                String receiverName = entry.getKey();
                OutputStream clientStream = entry.getValue();

                // 메시지를 보낸 클라이언트와 수신 클라이언트의 이름이 다를 때에만 메시지를 전송합니다.
                if (!receiverName.equals(nickname) || packetType == PacketType.DISCONNECT) {
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
            Packet packet = toPacket(buffer);
            if(packet.getHeader().getType() == PacketType.CONNECT){
                clientName = packet.getBody().getNickname();
                System.out.println("[" + clientName + " Connected]");
                clientMap.put(clientName, out);
            }
        }
    }

}