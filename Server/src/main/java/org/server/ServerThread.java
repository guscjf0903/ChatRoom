package org.server;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerThread extends Thread {
    static List<ObjectOutputStream> list = //PrintWriter는 다양한 자료형을 출력하기 위한 메서드, 다중 클라이언트에게 메세지를 전송하는데 사용
            Collections.synchronizedList(new ArrayList<ObjectOutputStream>());
    Socket socket = null;
    ObjectInputStream in = null; //문자열 기반의 입력을 처라히가 위한 클래스. 주로 텍스트파일이나 네트워크에서의 입력읽어오는데 사용
    ObjectOutputStream out = null; //문자열 기반의 출력을 처리하기 위한 클래스. 주로 텍스트파일이나 네트워크로의 출력 담당.
    //클라이언트 객체 한명한명

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream()); // 클라에게 보내는 메세지
            in = new ObjectInputStream(socket.getInputStream()); //클라에서 오는 메세지
            list.add(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String name = null;
        Packet serverPacket = null;
        Packet receivedPacket = null;
        serverPacket = new Packet(new PacketHeader("Server"), new PacketBody()); // 서버의 전체공지 패킷 따로 생성
        try {
            receivedPacket = (Packet) in.readObject(); // 최초1회는 클라이언트 이름 수신
            name = receivedPacket.getHeader().getSender(); // 닉네임 가져오기.
            System.out.println("[" + name + " Connected]");
            serverPacket.getBody().setMessage(name + " has entered.");
            sendAll(serverPacket); //모두에게 보내기

            while (in != null) { // 다음 대화내용 받아내기.
                receivedPacket = (Packet) in.readObject();
                String inputMsg = receivedPacket.getBody().getMessage();
                if ("quit".equals(inputMsg)) break; //quit가 들어오면 while문 벗어남
                sendAll(receivedPacket); //대화내용 출력
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[" + name + "Disconnected]");
        } finally {
            serverPacket.getBody().setMessage(name + " has left the chatroom.");
            sendAll(serverPacket); //누군가 나가면 클라이언트 모두가 볼수있는 메세지
            list.remove(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " Disconnected]"); //서버관리자가 누군가 나가면 볼수있는 메세지
    }

    private void sendAll(Packet packet) { // 모두에게 전송
        try {
            for (ObjectOutputStream out : list) {
                out.writeObject(packet);
            }
            for (ObjectOutputStream out : list) {
                out.flush(); //출력 스트림에 대기중인 모든 데이터를 강제로 출력하는 메서드. (버퍼를 비워줌)
            }
        } catch (IOException e) {
            System.out.println("SendAll Error");
        }

    }

}