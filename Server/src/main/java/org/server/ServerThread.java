package org.server;

import org.share.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static org.share.ToConvert.toByteArray;
import static org.share.ToConvert.toPacket;

public class ServerThread extends Thread {
    static final int MAXBUFFERSIZE = 1024;
    //static List<OutputStream> list = //PrintWriter는 다양한 자료형을 출력하기 위한 메서드, 다중 클라이언트에게 메세지를 전송하는데 사용
            //Collections.synchronizedList(new ArrayList<OutputStream>());
    static Map<String, OutputStream> map = Collections.synchronizedMap(new HashMap<String, OutputStream>());

    Socket socket = null;
    InputStream in = null; //문자열 기반의 입력을 처라히가 위한 클래스. 주로 텍스트파일이나 네트워크에서의 입력읽어오는데 사용
    OutputStream out = null; //문자열 기반의 출력을 처리하기 위한 클래스. 주로 텍스트파일이나 네트워크로의 출력 담당.
    //클라이언트 객체 한명한명

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
        Packet serverPacket = null;
        Packet receivePacket = null;
        Packet sendAllPacket = null;
        byte[] receiveData = new byte[MAXBUFFERSIZE];
        int nReadSize = 0;
        try {
            nReadSize = in.read(receiveData);// 최초1회는 클라이언트 이름 수신
            receivePacket = toPacket(receiveData);
            if (receivePacket.getHeader().getType() == PacketType.CONNECT) {
                name = receivePacket.getBody().getNickname();
                System.out.println("[" + name + " Connected]");
                map.put(name, out);
                serverPacket = new Packet(PacketType.SERVER, name + " has entered.", "SERVER");
                sendAll(serverPacket);// 접속했다고 모두에게 보내기.
            }
            // -----------------------------------------------//
            while (in != null) { // 다음 대화내용 받아내기.
                nReadSize = in.read(receiveData);
                receivePacket = toPacket(receiveData);
                String inputMsg = receivePacket.getBody().getMessage();
                if ("quit".equals(inputMsg)) break; //quit가 들어오면 while문 벗어남
                sendAllPacket = new Packet(PacketType.CLIENT, inputMsg, name);
                sendAll(sendAllPacket); //대화내용 출력
            }
        } catch (IOException e) {
            System.out.println("[" + name + "Disconnected]");
        } finally {
            Packet DisconnectPacket = new Packet(PacketType.DISCONNECT, "", name);
            sendAll(DisconnectPacket); //누군가 나가면 클라이언트 모두가 볼수있는 메세지
            map.remove(name);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " Disconnected]"); //서버관리자가 누군가 나가면 볼수있는 메세지
    }

    private void sendAll(Packet packet) { // 모두에게 전송
        byte[] data = toByteArray(packet);
        try {

            for (Map.Entry<String, OutputStream> entry : map.entrySet()) {
                if(entry.getKey().equals(packet.getBody().getNickname())) { // 자기가 보낸 메세지는 올필요가 없기때문에 continue
                    if(packet.getHeader().getType() == PacketType.DISCONNECT) { //디스커넥트 타입은 자기자신한테도 전송해야함.
                        entry.getValue().write(data);
                    }
                    continue;
                } else{
                    entry.getValue().write(data);
                }
            }
            for (OutputStream out : map.values()) {
                out.flush(); //출력 스트림에 대기중인 모든 데이터를 강제로 출력하는 메서드. (버퍼를 비워줌)
            }
        } catch (IOException e) {
            System.out.println("SendAll Error");
        }

    }

}