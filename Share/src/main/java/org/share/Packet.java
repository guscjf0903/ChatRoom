package org.share;

import java.io.Serializable;

public class Packet implements Serializable { // 바디와 헤더를 한꺼번에 저장하고 있는 클래스.
    private PacketHeader header;
    private PacketBody body;


    public Packet(PacketType packetType, String message, String nickname) {
        this.header = new PacketHeader(packetType, message.length());
        if (packetType == PacketType.CLIENT) {
            this.body = new PacketBody(message, nickname);
        } else if(packetType == PacketType.SERVER){
            nickname = "SERVER";
            this.body = new PacketBody(message, nickname);
        } else if (packetType == PacketType.CONNECT) {
            this.body = new PacketBody(message, nickname);
        } else if (packetType == PacketType.DISCONNECT) {
            message = nickname + " has left the chatroom.";
            this.body = new PacketBody(message, nickname);
        }
    }

    public PacketHeader getHeader() {
        return header;
    }

    public PacketBody getBody() {
        return body;
    }
}