package org.share;

import java.io.Serializable;

public class Packet implements Serializable { // 바디와 헤더를 한꺼번에 저장하고 있는 클래스.
    private PacketHeader header;
    private PacketBody body;

    public Packet(PacketHeader header, PacketBody body) {
        this.header = header;
        this.body = body;
    }

    public PacketHeader getHeader() {
        return header;
    }

    public PacketBody getBody() {
        return body;
    }
}